package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.forealert.intf.entity.type.UserMessageStatus;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/26/17
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "user_message")
@Document(expiry = 0)
public class UserMessageEntity extends Base{

    public static String TYPE = "UserMessage";

    @Field
    private String userId;

    @Field
    private String userUUId;

    @Field
    private String messageId;

    @Field
    private String groupId;

    @Field
    private String senderId;
    
    @Field
    private UserMessageStatus status = UserMessageStatus.SEND;

    public UserMessageEntity() {
        setTypeKey(TYPE);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserUUId() {
        return userUUId;
    }

    public void setUserUUId(String userUUId) {
        this.userUUId = userUUId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public UserMessageStatus getStatus() {
        return status;
    }

    public void setStatus(UserMessageStatus status) {
        this.status = status;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }
}
