package com.forealert.intf.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.forealert.intf.entity.GroupEntity;
import com.forealert.intf.entity.MessageEntity;
import com.forealert.intf.entity.UserEntity;
import com.forealert.intf.entity.type.UserMessageStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "user_message")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MessageDTO {

    private UserEntity sender;
    private MessageEntity parentMessage;
    private MessageEntity message;
    private GroupEntity group;
    private UserEntity receiver;
    private UserMessageStatus status;
    private MessageDTO lastReply;
    private List<Recipient> recipients;

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public MessageEntity getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(MessageEntity parentMessage) {
        this.parentMessage = parentMessage;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public UserMessageStatus getStatus() {
        return status;
    }

    public void setStatus(UserMessageStatus status) {
        this.status = status;
    }

    public MessageDTO getLastReply() {
        return lastReply;
    }

    public void setLastReply(MessageDTO lastReply) {
        this.lastReply = lastReply;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public void addRecipient(UserEntity user, UserMessageStatus  status){
        if(null == getRecipients())
            setRecipients(new ArrayList<Recipient>());
        getRecipients().add(new Recipient(user, status));
    }

    static class Recipient{
        private UserEntity user;
        private UserMessageStatus status;

        Recipient(UserEntity user, UserMessageStatus status) {
            this.user = user;
            this.status = status;
        }

        public UserEntity getUser() {
            return user;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public UserMessageStatus getStatus() {
            return status;
        }

        public void setStatus(UserMessageStatus status) {
            this.status = status;
        }
    }
}
