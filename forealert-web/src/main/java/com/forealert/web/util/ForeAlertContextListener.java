package com.forealert.web.util;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.forealert.core.notification.NotificationFactory;
import com.forealert.core.notification.PushyPushNotification;
import com.forealert.core.util.ForeAlertS3Client;
import com.forealert.intf.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/17/17
 * Time: 8:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForeAlertContextListener implements ServletContextListener {
    private static Logger logger = LoggerFactory.getLogger(ForeAlertContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Initialized... ");
        TimeZone.setDefault(TimeZone.getTimeZone(Constant.TIME_ZONE));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Bucket bucket =    (Bucket)ApplicationContextUtil.getApplicationContext().getBean("bucket");
        DefaultCouchbaseEnvironment environment =    (DefaultCouchbaseEnvironment)ApplicationContextUtil.getApplicationContext().getBean("couchbaseEnv");
        CouchbaseCluster couchbaseCluster =    (CouchbaseCluster)ApplicationContextUtil.getApplicationContext().getBean("couchbaseCluster");
        bucket.close();
        environment.shutdownAsync().toBlocking().single();
        environment.shutdown();
        couchbaseCluster.disconnect();
        ForeAlertS3Client.INSTANCE.disconnect();
        NotificationFactory.INSTANCE.disconnect();
    }
}
