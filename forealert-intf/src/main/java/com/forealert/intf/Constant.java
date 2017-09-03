package com.forealert.intf;

import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/22/17
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Constant {

    static int DEFAULT_MESSAGE_EXPIRY_TIME_IN_MINUTES = 24*60; // 24 HOURS
    static int DEFAULT_CLIENTS_ACTIVE_TIME_IN_MINUTES = 120; // 60 minutes
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /*Couch base*/
    static String BUCKET_NAME = "forealert";

    /*Error code*/
    static Integer DEFAULT_ERROR_CODE = 500;
    static Integer NO_RECORD_FOUND = 404;



    /*Push config*/
    static String PUSH_MODE_PAYLOAD = "mode";

    String TIME_ZONE = "Etc/UTC";

}
