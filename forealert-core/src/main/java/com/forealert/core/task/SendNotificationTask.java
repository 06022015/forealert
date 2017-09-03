package com.forealert.core.task;

import com.forealert.core.notification.NotificationFactory;
import com.forealert.core.notification.PushMessage;
import com.forealert.core.notification.PushNotification;
import com.forealert.intf.bean.ForeAlertBean;
import com.forealert.intf.entity.type.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class SendNotificationTask implements Callable {

    private static Logger logger = LoggerFactory.getLogger(SendNotificationTask.class);

    private PushMessage message;

    public SendNotificationTask(PushMessage message) {
        this.message = message;
        //this.pushNotification = NotificationFactory.INSTANCE.getNotificationService(message.getDeviceType());
    }

    public Object call() throws Exception {
        if(message.getFcmTokens().size()>0)
            NotificationFactory.INSTANCE.getNotificationService(DeviceType.ANDROID).push(message);
        if(message.getIosTokens().size()>0)
            NotificationFactory.INSTANCE.getNotificationService(DeviceType.IOS).push(message);
        return "SUCCESS";
    }
}
