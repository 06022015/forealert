package com.forealert.core.notification;

import com.forealert.intf.api.repository.UserRepository;
import com.forealert.intf.bean.ForeAlertBean;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/9/17
 * Time: 11:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class FCMPushNotification implements PushNotification{

    private static Logger logger = LoggerFactory.getLogger(FCMPushNotification.class);
    private static PushNotification INSTANCE = null;

    private Properties properties;
    private UserRepository userRepository;

    private FCMPushNotification(ForeAlertBean bean) {
        this.properties = bean.getProperties();
        this.userRepository = bean.getUserRepository();
    }

    public static PushNotification getInstance(ForeAlertBean bean){
        if(null == INSTANCE){
            synchronized (PushyPushNotification.class){
                if(null == INSTANCE)
                    INSTANCE = new FCMPushNotification(bean);
            }
        }
        return INSTANCE;
    }

    public void push(PushMessage pushMessage) throws InterruptedException {
        JSONObject payload = preparePayload(pushMessage);
        for(int i=0;i< pushMessage.getMultiple(); i++){
            JSONObject response = send(payload);
            if(null != response && response.getInt("code")== 200 && response.getJSONObject("content").getInt("failure")>0){
                JSONArray results = response.getJSONObject("content").getJSONArray("results");
                for(int j=0; j< results.length(); j++){
                    JSONObject result = results.getJSONObject(i);
                    if(result.has("error") && result.getString("error").equalsIgnoreCase("MismatchSenderId")){
                        /*String prefix = isDevType?"GFCM:0:" :"GFCM:1:";
                        User u  = InfoExchangeDBClient.findUserByUserUuid(prefix + tokens.get(j));
                        u.setTobePurged(true);
                        InfoExchangeDBClient.save(u);*/
                    }
                }
            }
        }
    }

    public JSONObject preparePayload(PushMessage pushMessage){
        JSONObject root = new JSONObject();
        JSONObject data = new JSONObject();
        if(null != pushMessage.getCustom()){
            for(String key : pushMessage.getCustom().keySet()){
                data.put(key, pushMessage.getCustom().get(key));
            }
        }
        JSONObject notification = new JSONObject();
        notification.put("body", pushMessage.getMessage());
        notification.put("title", "FOREalert Message");
        notification.put("sound", "default");
        notification.put("badge", "2");
        root.put("data", data);
        root.put("notification", notification);
        JSONArray registrationIds = new JSONArray(pushMessage.getFcmTokens());
        root.put("registration_ids", registrationIds);
        //root.put("to", tokens.get(0));
        //root.put("priority", 10);
        root.put("content_available", true);
        return root;
    }


    private JSONObject send(JSONObject payload){
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        JSONObject fcmRes =null;
        try{
            HttpPost request = new HttpPost(this.properties.getProperty("fcm.endpoint"));
            request.setHeader("Content-type", MediaType.APPLICATION_JSON.toString());
            request.setHeader("Authorization", "Key="+this.properties.getProperty("fcm.key"));
            System.out.println(payload.toString());
            StringEntity params = new StringEntity(payload.toString());
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            String pushResult = EntityUtils.toString(response.getEntity());
            System.out.println("result........ code:- "+ response.getStatusLine().getStatusCode()+ " Content:- "+pushResult);
            JSONObject fcmContent = new JSONObject(pushResult);
            fcmRes = new JSONObject();
            fcmRes.put("code", response.getStatusLine().getStatusCode());
            fcmRes.put("content", fcmContent);
        } catch (IOException e) {
            logger.error("FCM Client error:- "+e.getMessage());
        } finally {
            try {
                if(null != httpClient)
                    httpClient.close();
            } catch (IOException e) {
                logger.error("Unable to close FCM client connection:- " + e.getMessage());
            }
        }
        return fcmRes;
    }

    @Override
    public void disconnect() {

    }
}
