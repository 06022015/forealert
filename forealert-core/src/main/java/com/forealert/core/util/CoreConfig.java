package com.forealert.core.util;

import com.forealert.core.notification.NotificationFactory;
import com.forealert.intf.bean.ForeAlertBean;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/11/17
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class CoreConfig {

    public CoreConfig(ForeAlertBean bean) {
        initializeS3Client(bean);
        initializePushNotification(bean);

    }

    private void initializeS3Client(ForeAlertBean bean) {
        ForeAlertS3Client.create(bean.getProperties().getProperty("amazon.s3.client.bucket"),
                bean.getProperties().getProperty("amazon.s3.client.key"),
                bean.getProperties().getProperty("amazon.s3.client.secret.key"),
                bean.getProperties().getProperty("amazon.s3.client.region"));
    }

    private void initializePushNotification(ForeAlertBean bean) {
        NotificationFactory.create(bean);
    }
}
