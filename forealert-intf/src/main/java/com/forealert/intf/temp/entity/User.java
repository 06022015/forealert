package com.forealert.intf.temp.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.forealert.intf.Constant;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;


@JsonSerialize(include = Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "InfoExchangeAttributes")
@DynamoDBTable(tableName="Users")
public class User {
	
	private String userUuid;
	private InfoExchangeAppEnum appId;
	private Double latitude;
	private Double longitude;
	private Double altitude;
	private InfoExchangeAppRoleEnum appUserRole;
	private InfoExchangeAppDeviceTypeEnum deviceType;
	private String createdDate;
	private String lastModifiedDate;
	private Set<String> messageIds;
	private String adminId;
	private String token;
	private String refreshToken;
	private String googleUserId;
	private String email;
	private String userName;
	private String profileUrl;
	private String phoneNo;
	private boolean isLoggedOut;
	private boolean toPurge;
	
	public User() {}
	public User(InfoExchangeAppEnum appId,  String userUuid) {
		this.userUuid = userUuid;
		this.appId = appId;
	}
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "ApId-LAT-index", attributeName = "ApId")
	@DynamoDBHashKey(attributeName="ApId")
	public String getAppId() {
		if(appId == null) return InfoExchangeAppEnum.UN.name();
		
		return appId.name();
	}
	public void setAppId( String appId) {
		this.appId =  (appId == null ? InfoExchangeAppEnum.UN : InfoExchangeAppEnum.valueOf(appId));;
	}
	
	@DynamoDBRangeKey(attributeName="UUuid")
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	
	@DynamoDBIndexRangeKey(globalSecondaryIndexNames = {"ApId-LAT-index" , "ApURole-LAT-index"}, attributeName = "LAT")
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@DynamoDBAttribute(attributeName="LONGT")
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@DynamoDBAttribute(attributeName="ALT")
	public Double getAltitude() {
		return altitude;
	}
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
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
	
	@DynamoDBAttribute(attributeName="CRD") 
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		if(createdDate == null) createdDate = new Date();
        Constant.dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.createdDate = Constant.dateFormatter.format(createdDate);
	}
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "ApURole-LAT-index", attributeName = "ApURole")
	@DynamoDBAttribute(attributeName="ApURole")
	public String getAppUserRole() {
		if(appUserRole == null) return InfoExchangeAppRoleEnum.UN.name();
		return appUserRole.name();
	}
	public void setAppUserRole(String appUserRole) {
		this.appUserRole = (appUserRole == null ? InfoExchangeAppRoleEnum.UN : InfoExchangeAppRoleEnum.valueOf(appUserRole));
	}
	
	@DynamoDBAttribute(attributeName="DVTy")
	public String getDeviceType() {
		if(deviceType == null) return InfoExchangeAppDeviceTypeEnum.UN.name();
		return deviceType.name();
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = (deviceType == null ? InfoExchangeAppDeviceTypeEnum.UN : InfoExchangeAppDeviceTypeEnum.valueOf(deviceType));
	}
	
	@DynamoDBIgnore
	public Set<String> getMessageIds() {
		String userUuid = this.getUserUuid().startsWith("PUSH:") ? this.getUserUuid().substring(7) : this.getUserUuid();
        //TODO: Need to revisit below db query. Commented temporarily
		List<UserToMessage> userToMessages = null;//InfoExchangeDBClient.findUserToMessages(userUuid);
		if(!CollectionUtils.isNullOrEmpty(userToMessages)) {
			messageIds = new HashSet<String>();
			for(UserToMessage userToMessageIds : userToMessages) {
				messageIds.add(userToMessageIds.getMessageId());
			}
		}
		return messageIds;
	}
	public void setMessageIds(Set<String> userToMessageId) {
		if(userToMessageId == null) return;
		
		UserToMessage userToMessage = new UserToMessage();
		userToMessage.setCreatedDate(new Date());
		userToMessage.setLastModifiedDate(new Date());
		userToMessage.setMessageId(userToMessageId.iterator().next());
		userToMessage.setMessageSplitMonth((Calendar.getInstance().get(Calendar.MONTH) + 1 ) + "_" + Calendar.getInstance().get(Calendar.YEAR));
		userToMessage.setUserUuid(this.getUserUuid().startsWith("PUSH:") ? this.getUserUuid().substring(7) : this.getUserUuid());
        //TODO: Need to revisit below db query. Commented temporarily
		//InfoExchangeDBClient.save(userToMessage);
	}
	
	@DynamoDBAttribute(attributeName="AId")
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	
	@DynamoDBAttribute(attributeName="Token")
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	@DynamoDBAttribute(attributeName="Email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@DynamoDBAttribute(attributeName="RToken")
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	@DynamoDBAttribute(attributeName="GUsId")
	public String getGoogleUserId() {
		return googleUserId;
	}
	public void setGoogleUserId(String googleUserId) {
		this.googleUserId = googleUserId;
	}
	
	@DynamoDBAttribute(attributeName="UNm")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@DynamoDBAttribute(attributeName="PUrl")
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	@DynamoDBAttribute(attributeName="PHN")
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@DynamoDBAttribute(attributeName="isLogOut")
	public boolean isLoggedOut() {
		return isLoggedOut;
	}
	public void setLoggedOut(boolean isLoggedOut) {
		this.isLoggedOut = isLoggedOut;
	}
	@DynamoDBAttribute(attributeName="toPurge")
	public boolean isTobePurged() {
		return toPurge;
	}
	public void setTobePurged(boolean toPurge) {
		this.toPurge = toPurge;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		result = prime * result
				+ ((userUuid == null) ? 0 : userUuid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (appId != other.appId)
			return false;
		if (userUuid == null) {
			if (other.userUuid != null)
				return false;
		} else if (!userUuid.equals(other.userUuid))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Users [userUuid=" + userUuid + ", appId="
				+ appId + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", appUserRole=" + appUserRole + ", deviceType=" + deviceType
				+ ", createdDate=" + createdDate + ", lastModifiedDate="
				+ lastModifiedDate + ", messageIds=" + messageIds
				+ ", adminId="+adminId 
				+", email="+email
				+", phone="+phoneNo
				+ super.toString() + "]";
	}
	
	
}
