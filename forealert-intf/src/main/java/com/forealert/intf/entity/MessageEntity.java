package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.forealert.intf.entity.type.*;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/26/17
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "message")
@Document(expiry = 0)
public class MessageEntity extends Base{

    public static String TYPE = "Message";

    @Field
    private String title;
    @Field
    private String message;
    @Field
    private AppEnum app;
    @Field
    private String senderId;
    @Field
    private int expiryTime;
    @Field
    private Location messageLocation;
    @Field
    private Location senderLocation;
    @Field
    private MessageStatus status;
    @Field
    private boolean replyBlocked;
    @Field
    private MessageType messageType;
    @Field
    private String parentId;
    @Field
    private String overlayCoordinatesUrl;
    @Field
    private boolean isUserGEOMessage;
    @Field
    private boolean isGroupGEOMessage;
    @Field
    private boolean isAnonymousMessage;
    @Field
    private DeviceType device;
    @Field
    private String imageURL;

    private String overlayCoordinates;
    private String senderUUId;
    private List<String> recipientIds;
    private List<String> groupIds;

    public MessageEntity() {
        setTypeKey(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AppEnum getApp() {
        return app;
    }

    public void setApp(AppEnum app) {
        this.app = app;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(int expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Location getMessageLocation() {
        return messageLocation;
    }

    public void setMessageLocation(Location messageLocation) {
        this.messageLocation = messageLocation;
    }

    public Location getSenderLocation() {
        return senderLocation;
    }

    public void setSenderLocation(Location senderLocation) {
        this.senderLocation = senderLocation;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public boolean isReplyBlocked() {
        return replyBlocked;
    }

    public void setReplyBlocked(boolean replyBlocked) {
        this.replyBlocked = replyBlocked;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOverlayCoordinatesUrl() {
        return overlayCoordinatesUrl;
    }

    public void setOverlayCoordinatesUrl(String overlayCoordinatesUrl) {
        this.overlayCoordinatesUrl = overlayCoordinatesUrl;
    }

    public boolean isUserGEOMessage() {
        return isUserGEOMessage;
    }

    public void setUserGEOMessage(boolean userGEOMessage) {
        isUserGEOMessage = userGEOMessage;
    }

    public boolean isGroupGEOMessage() {
        return isGroupGEOMessage;
    }

    public void setGroupGEOMessage(boolean groupGEOMessage) {
        isGroupGEOMessage = groupGEOMessage;
    }

    public boolean isAnonymousMessage() {
        return isAnonymousMessage;
    }

    public void setAnonymousMessage(boolean anonymousMessage) {
        isAnonymousMessage = anonymousMessage;
    }

    public DeviceType getDevice() {
        return device;
    }

    public void setDevice(DeviceType device) {
        this.device = device;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getOverlayCoordinates() {
        return overlayCoordinates;
    }

    public void setOverlayCoordinates(String overlayCoordinates) {
        this.overlayCoordinates = overlayCoordinates;
    }

    public String getSenderUUId() {
        return senderUUId;
    }

    public void setSenderUUId(String senderUUId) {
        this.senderUUId = senderUUId;
    }

    public List<String> getRecipientIds() {
        return recipientIds;
    }

    public void setRecipientIds(List<String> recipientIds) {
        this.recipientIds = recipientIds;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public String getTypeKey() {
        return TYPE;
    }

}
