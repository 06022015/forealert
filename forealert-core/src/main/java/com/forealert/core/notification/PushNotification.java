package com.forealert.core.notification;

import io.netty.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PushNotification {

    void push(PushMessage pushMessage) throws InterruptedException;

    void disconnect();

}
