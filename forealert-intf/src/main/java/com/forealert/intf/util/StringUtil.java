package com.forealert.intf.util;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/3/17
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    
    public static boolean isBlank(String text){
        return null == text || "".equals(text);
    }

    public static boolean isNotBlank(String text){
        return null != text && !"".equals(text);
    }

    public static boolean isEmpty(String text){
        return "".equals(text);
    }

    public static boolean isNotEmpty(String text){
        return !"".equals(text);
    }
    
}
