package com.forealert.core.notification;

import com.forealert.intf.bean.ForeAlertBean;
import com.forealert.intf.entity.type.DeviceType;
import com.forealert.intf.exception.ForeAlertException;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/28/17
 * Time: 9:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class NotificationFactory {

    public static PushNotification notificationForIOS = null;
    public static PushNotification notificationForAndroid = null;
    public static NotificationFactory INSTANCE = null;

    private NotificationFactory(ForeAlertBean bean) {
        init(bean);
    }

    private void init(ForeAlertBean bean) {
        if (null == notificationForIOS) {
            synchronized (NotificationFactory.class) {
                if (null == notificationForIOS)
                    notificationForIOS = PushyPushNotification.getInstance(bean);
            }
        }
        if (null == notificationForAndroid) {
            synchronized (NotificationFactory.class) {
                if (null == notificationForAndroid)
                    notificationForAndroid = FCMPushNotification.getInstance(bean);
            }
        }
    }

    public PushNotification getNotificationService(DeviceType deviceType) {
        if (deviceType.equals(DeviceType.IOS))
            return notificationForIOS;
        if (deviceType.equals(DeviceType.ANDROID))
            return notificationForAndroid;
        else
            throw new ForeAlertException("Notification service is not configured for device:- " + deviceType);
    }

    public static NotificationFactory create(ForeAlertBean bean) {
        if (null == INSTANCE) {
            synchronized (NotificationFactory.class) {
                if (null == INSTANCE)
                    INSTANCE = new NotificationFactory(bean);
            }
        }
        return INSTANCE;
    }

    public void disconnect() {
        notificationForAndroid.disconnect();
        notificationForIOS.disconnect();
    }
}
