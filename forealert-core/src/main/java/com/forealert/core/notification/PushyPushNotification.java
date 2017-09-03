package com.forealert.core.notification;

import com.forealert.intf.api.repository.UserRepository;
import com.forealert.intf.bean.ForeAlertBean;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ApnsClientBuilder;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 9:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class PushyPushNotification implements PushNotification {

    private static Logger logger = LoggerFactory.getLogger(PushyPushNotification.class);
    public static PushyPushNotification INSTANCE = null;

    private ApnsClient client;
    private Properties properties;
    private UserRepository userRepository;

    private PushyPushNotification(ForeAlertBean bean) {
        this.properties = bean.getProperties();
        this.userRepository = bean.getUserRepository();
        init();
    }

    public static PushyPushNotification getInstance(ForeAlertBean bean){
        if(null == INSTANCE){
            synchronized (PushyPushNotification.class){
                if(null == INSTANCE)
                    INSTANCE = new PushyPushNotification(bean);
            }
        }
        return INSTANCE;
    }

    private void init(){
        try {
            InputStream p12InputStream = this.getClass().getClassLoader().getResourceAsStream(properties.getProperty("push.ios.p12"));
            logger.debug("p 12:- " + p12InputStream);
            client = new ApnsClientBuilder().setClientCredentials(p12InputStream, properties.getProperty("push.ios.password")).build();
            Future<Void> connectFutureDev = client.connect(ApnsClient.DEVELOPMENT_APNS_HOST);
            connectFutureDev.await();
            logger.debug("started pushy connection for PROD");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to initialize apns service " + e);
        }
    }

    public ApnsClient getClient() {
        return client;
    }

    public void push(PushMessage pushMessage) throws InterruptedException {
        for (int i = 0; i < pushMessage.getMultiple(); i++) {
            for (String srcToken : pushMessage.getIosTokens()) {
                final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
                payloadBuilder.setAlertTitle(properties.getProperty("push.alert.title"));
                payloadBuilder.setSoundFileName(properties.getProperty("push.sound.file.name"));
                payloadBuilder.setAlertBody(pushMessage.getMessage());
                final String token = TokenUtil.sanitizeTokenString(srcToken);
                if (null != pushMessage.getCustom()) {
                    for (final Map.Entry<String, String> entry : pushMessage.getCustom().entrySet()) {
                        payloadBuilder.addCustomProperty(entry.getKey(), entry.getValue());
                    }
                }
                final String payload = payloadBuilder.buildWithDefaultMaximumLength();
                SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, properties.getProperty("push.notification.topic"), payload);
                try {
                    final java.util.concurrent.Future<PushNotificationResponse<SimpleApnsPushNotification>> notificationFuture = client.sendNotification(pushNotification);
                    final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = notificationFuture.get();
                    if (!pushNotificationResponse.isAccepted()) {
                        logger.debug("Notification rejected by the APNs gateway: " +
                                pushNotificationResponse.getRejectionReason() + " " +
                                pushNotificationResponse.getPushNotification().getToken());
                        if (null != pushNotificationResponse.getTokenInvalidationTimestamp()) {
                            logger.debug("\t…and the token is invalid as of " +
                                    pushNotificationResponse.getTokenInvalidationTimestamp());
                            //TODO: need to handle tobe purged case
                            //userRepository.save();
                        }
                    }
                    /*if (pushNotificationResponse.isAccepted()) {
                        //	logger.info("Push notification accepted by APNs gateway." +pushNotificationResponse.getPushNotification().getToken());
                    } else {
                        logger.info("Notification rejected by the APNs gateway: " +  pushNotificationResponse.getRejectionReason() +" "+pushNotificationResponse.getPushNotification().getToken());

                        if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                            logger.info("\t…and the token is invalid as of " +
                                    pushNotificationResponse.getTokenInvalidationTimestamp());
                            *//*String prefix = isDevType?"PUSH:0:" :"PUSH:1:";
                            User u  = InfoExchangeDBClient.findUserByUserUuid(prefix+pushNotificationResponse.getPushNotification().getToken());
                            u.setTobePurged(true);
                            InfoExchangeDBClient.save(u);*//*
                        }
                    }*/
                } catch (ExecutionException e) {
                    logger.debug("Failed to send push notification." + e);
                    e.printStackTrace();
                    if (e.getCause() instanceof ClientNotConnectedException) {
                        logger.info("Waiting for client to reconnect…");
                        client.getReconnectionFuture().await();
                    }
                }

            }
        }
    }

    @Override
    public void disconnect() {
        this.client.disconnect();
    }
}
