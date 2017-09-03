package com.forealert.intf.temp.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.forealert.intf.Constant;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.TimeZone;

@JsonSerialize(include = Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "Admin_Role_Priviliges")
@DynamoDBTable(tableName="Admin_Role_Priviliges")
public class AdminRolePriviliges {

	private String roleId;
	private String roleName;
	private boolean messageReadPrivilige;
	private boolean messageSendPrivilige;
	private boolean messageNotifyPrivilige;
	private String createdDate;
	private String lastModifiedDate;
	
	@DynamoDBHashKey(attributeName="RId")
	@DynamoDBAutoGeneratedKey
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@DynamoDBAttribute(attributeName="RNm")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	@DynamoDBAttribute(attributeName="MRP")
	public boolean isMessageReadPrivilige() {
		return messageReadPrivilige;
	}
	public void setMessageReadPrivilige(boolean messageReadPrivilige) {
		this.messageReadPrivilige = messageReadPrivilige;
	}
	
	@DynamoDBAttribute(attributeName="MSP")
	public boolean isMessageSendPrivilige() {
		return messageSendPrivilige;
	}
	public void setMessageSendPrivilige(boolean messageSendPrivilige) {
		this.messageSendPrivilige = messageSendPrivilige;
	}
	
	@DynamoDBAttribute(attributeName="MNP")
	public boolean isMessageNotifyPrivilige() {
		return messageNotifyPrivilige;
	}
	public void setMessageNotifyPrivilige(boolean messageNotifyPrivilige) {
		this.messageNotifyPrivilige = messageNotifyPrivilige;
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
		return "AdminRolePriviliges [roleId=" + roleId + ", roleName="
				+ roleName +  ", messageReadPrivilige="
				+ messageReadPrivilige + ", messageSendPrivilige="
				+ messageSendPrivilige + ", messageNotifyPrivilige="
				+ messageNotifyPrivilige + ", createdDate=" + createdDate
				+ ", lastModifiedDate=" + lastModifiedDate + "]";
	}

	
	
	
}
