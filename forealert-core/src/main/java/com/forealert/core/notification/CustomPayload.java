package com.forealert.core.notification;

import com.forealert.intf.entity.Location;
import com.forealert.intf.entity.MessageEntity;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/5/17
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomPayload {

    private boolean isMessageRepliesBlocked;
    private boolean isUserGEOMessage;
    private boolean isGroupGEOMessage;
    private Location targetLocation;
    
    private String senderUuid;
    private String title;
    private String messageId;
    private String message;
    private String status;
    private String imageUrl;
    private String overlayCoordinatesUrl;
    private Set<String> groupIds;
    private String fileType;
    private String fileDescription;
    private String fileLocationUrl;
    private String fileIconUrl;
    private String fileTitle;
    private Set<String> fileIds;

    public CustomPayload() {
    }

    public CustomPayload(MessageEntity message) {
        this.senderUuid = message.getSenderUUId();
        this.title = message.getTitle();
        this.messageId = message.getId();
        this.message = message.getMessage();
        this.status = message.getStatus().name();
        //this.imageUrl = message.get
        this.overlayCoordinatesUrl = message.getOverlayCoordinatesUrl();
        this.isGroupGEOMessage = message.isGroupGEOMessage();
        this.isUserGEOMessage = message.isUserGEOMessage();
        this.isMessageRepliesBlocked = message.isReplyBlocked();
    }

    public boolean isMessageRepliesBlocked() {
        return isMessageRepliesBlocked;
    }

    public void setMessageRepliesBlocked(boolean messageRepliesBlocked) {
        isMessageRepliesBlocked = messageRepliesBlocked;
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

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public String getSenderUuid() {
        return senderUuid;
    }

    public void setSenderUuid(String senderUuid) {
        this.senderUuid = senderUuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverlayCoordinatesUrl() {
        return overlayCoordinatesUrl;
    }

    public void setOverlayCoordinatesUrl(String overlayCoordinatesUrl) {
        this.overlayCoordinatesUrl = overlayCoordinatesUrl;
    }

    public Set<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Set<String> groupIds) {
        this.groupIds = groupIds;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getFileLocationUrl() {
        return fileLocationUrl;
    }

    public void setFileLocationUrl(String fileLocationUrl) {
        this.fileLocationUrl = fileLocationUrl;
    }

    public String getFileIconUrl() {
        return fileIconUrl;
    }

    public void setFileIconUrl(String fileIconUrl) {
        this.fileIconUrl = fileIconUrl;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public Set<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(Set<String> fileIds) {
        this.fileIds = fileIds;
    }
}
