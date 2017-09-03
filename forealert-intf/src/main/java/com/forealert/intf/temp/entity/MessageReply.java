package com.forealert.intf.temp.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.forealert.intf.Constant;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.TimeZone;

@JsonSerialize(include = Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "MessageReply")
@DynamoDBTable(tableName="Message_Reply")
public class MessageReply {
	
	private String messageReplyId;
	private String messageId;
	private String receiverUuid;
	private String senderUuid;
	private String messageText;
	private String messageStatus; // New - Read
	private String imageUrl;
	private String createdDate;
	private String lastModifiedDate;
	private String overlayCoordinatesUrl;
	private String uniqueHash;
	
	@DynamoDBAttribute(attributeName="OvlyUrl")
	public String getOverlayCoordinatesUrl() {
		return overlayCoordinatesUrl;
	}

	public void setOverlayCoordinatesUrl(String overlayCoordinatesUrl) {
		this.overlayCoordinatesUrl = overlayCoordinatesUrl;
	}

	public MessageReply() {}
	
	public MessageReply(String messageReplyId, String messageId) {
		this.messageReplyId = messageReplyId;
		this.messageId = messageId;
	}
	
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "MsgId-RUuid-index", attributeName = "MsgId")
	@DynamoDBRangeKey(attributeName="MsgId")
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "MsgId-RUuid-index", attributeName = "RUuid")
	public String getReceiverUuid() {
		return receiverUuid;
	}
	public void setReceiverUuid(String receiverUuid) {
		this.receiverUuid = receiverUuid;
	}
	
	@DynamoDBAttribute(attributeName="SUuid")
	public String getSenderUuid() {
		return senderUuid;
	}
	public void setSenderUuid(String senderUuid) {
		this.senderUuid = senderUuid;
	}
	
	@DynamoDBAutoGeneratedKey
	@DynamoDBHashKey(attributeName="MsgRId")
	public String getMessageReplyId() {
		return messageReplyId;
	}
	public void setMessageReplyId(String messageReplyId) {
		this.messageReplyId = messageReplyId;
	}
	
	
	@DynamoDBAttribute(attributeName="uqId")
	public String getUniqueId() {
        //TODO: Need to revisit. Commented temporarily
		return "";//+PNMessage.getUniqueHash(imageUrl, messageId, messageText, overlayCoordinatesUrl);
	}
	public void setUniqueId(String uniqueHash) {
		this.uniqueHash = uniqueHash;
	}
	
	@DynamoDBAttribute(attributeName="MTxt")
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	
	@DynamoDBAttribute(attributeName="MSts")
	public String getStatus() {
		return messageStatus;
	}
	public void setStatus(String status) {
		this.messageStatus = status;
	}
	
	@DynamoDBAttribute(attributeName="IUrl")
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@DynamoDBAttribute(attributeName="CRD")
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		if(createdDate == null) createdDate = new Date();
		Constant.dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.createdDate = Constant.dateFormatter.format(createdDate);
	}
	
	@DynamoDBAttribute(attributeName="LMD")
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		if(lastModifiedDate == null) lastModifiedDate = new Date();

        Constant.dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.lastModifiedDate = Constant.dateFormatter.format(lastModifiedDate);
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	

}