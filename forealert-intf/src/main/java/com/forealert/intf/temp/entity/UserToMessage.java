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
@XmlRootElement(name = "UserToMessage")
@DynamoDBTable(tableName="User_To_Message")
public class UserToMessage {


	private String userToMessageId;
	private String userUuid;
	private String messageId;
	private String messageSplitMonth;
	private String createdDate;
	private String lastModifiedDate;
	
	@DynamoDBAutoGeneratedKey
	@DynamoDBHashKey(attributeName="UMsgId")
	public String getUserToMessageId() {
		return userToMessageId;
	}
	public void setUserToMessageId(String userToMessageId) {
		this.userToMessageId = userToMessageId;
	}
	
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "UUuid-Msm-index", attributeName = "UUuid")
	@DynamoDBRangeKey(attributeName="UUuid")
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "UUuid-Msm-index", attributeName = "Msm")
	public String getMessageSplitMonth() {
		return messageSplitMonth;
	}
	public void setMessageSplitMonth(String messageSplitMonth) {
		this.messageSplitMonth = messageSplitMonth;
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
	@Override
	public String toString() {
		return "UserToMessage [userToMessageId=" + userToMessageId + ", userUuid=" + userUuid + ", messageId="
				+ messageId + ", messageSplitMonth=" + messageSplitMonth + ", createdDate=" + createdDate
				+ ", lastModifiedDate=" + lastModifiedDate + "]";
	}
	
	
}