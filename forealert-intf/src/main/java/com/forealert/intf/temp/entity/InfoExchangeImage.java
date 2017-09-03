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
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.TimeZone;

@JsonSerialize(include = Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "InfoExchangeAttributes")
@DynamoDBTable(tableName="InfoExchangeImage")
public class InfoExchangeImage {
	
	private String imageId;
	private String senderUuid;
	private ByteBuffer image;
	private String createdDate;
	
	@DynamoDBHashKey(attributeName="imageId")
	@DynamoDBAutoGeneratedKey
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	@DynamoDBAttribute(attributeName="senderUuid")
	public String getSenderUuid() {
		return senderUuid;
	}
	public void setSenderUuid(String senderUuid) {
		this.senderUuid = senderUuid;
	}
	
	@DynamoDBAttribute(attributeName="image")
	public ByteBuffer getImage() {
		return image;
	}
	public void setImage(ByteBuffer image) {
		this.image = image;
	}
	
	@DynamoDBAttribute(attributeName="createdDate")
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date date) {
		if(date == null) date = new Date();
		
		Constant.dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		String createdDate = Constant.dateFormatter.format(date);
		this.createdDate = createdDate;
	}
	

}
