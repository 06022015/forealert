package com.forealert.core.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/4/17
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class ForeAlertHelper {


    public static String generateUniqueKey() {
        String ts = String.valueOf(System.currentTimeMillis());
        String rand = UUID.randomUUID().toString();
        return DigestUtils.sha256Hex(ts + rand);
    }

}
