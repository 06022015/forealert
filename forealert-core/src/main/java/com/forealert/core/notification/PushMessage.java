package com.forealert.core.notification;

import com.forealert.intf.entity.type.DeviceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class PushMessage implements Cloneable{

    private List<String> tokens;
    private List<String> iosTokens;
    private List<String> fcmTokens;
    private DeviceType deviceType;
    private String message;
    private Boolean silent = false;
    private Map<String,String> custom;
    private String title;
    private String soundFileName;
    private int multiple = 1;

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
    
    public void addToken(String token){
        if(null == getTokens())
            setFcmTokens(new ArrayList<String>());
        if(null == getIosTokens())
            setIosTokens(new ArrayList<String>());
        if(token.startsWith("PUSH"))
            getIosTokens().add(token.substring(7));
        if(token.startsWith("GFCM"))
            getIosTokens().add(token.substring(7));
    }

    public List<String> getIosTokens() {
        return iosTokens;
    }

    public void setIosTokens(List<String> iosTokens) {
        this.iosTokens = iosTokens;
    }

    public List<String> getFcmTokens() {
        return fcmTokens;
    }

    public void setFcmTokens(List<String> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSilent() {
        return silent;
    }

    public void setSilent(Boolean silent) {
        this.silent = silent;
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, String> custom) {
        this.custom = custom;
    }
    
    public void putCustom(String key, String value){
        if(null == getCustom())
            setCustom(new HashMap<String, String>());
        getCustom().put(key,value);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSoundFileName() {
        return soundFileName;
    }

    public void setSoundFileName(String soundFileName) {
        this.soundFileName = soundFileName;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }
}
