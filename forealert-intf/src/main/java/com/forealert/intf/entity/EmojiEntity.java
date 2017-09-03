package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.forealert.intf.entity.type.AppEnum;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/26/17
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "emoji")
@Document(expiry = 0)
public class EmojiEntity extends Base{

    public static String TYPE = "Emoji";

    @Field
    private String title;
    @Field
    private String iconUrl;
    @Field
    private String message;
    @Field
    private Locale locale = Locale.US;
    @Field
    private AppEnum app;
    @Field
    private String groupId;
    @Field
    private Integer priority = 1;

    public EmojiEntity() {
        setTypeKey(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public AppEnum getApp() {
        return app;
    }

    public void setApp(AppEnum app) {
        this.app = app;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }
}
