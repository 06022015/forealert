package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/26/17
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "group")
@Document(expiry = 0)
public class GroupEntity extends Base{

    public static String TYPE = "Group";

    @Field
    private String name;
    @Field
    private String createdBy;
    @Field
    private Location location;
    @Field
    private String nameUpdatedBy;
    @Field
    private ByteBuffer attachment;
    @Field
    private String attachedBy;
    @Field
    private String parentGroupId;

    public GroupEntity() {
        setTypeKey(TYPE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNameUpdatedBy() {
        return nameUpdatedBy;
    }

    public void setNameUpdatedBy(String nameUpdatedBy) {
        this.nameUpdatedBy = nameUpdatedBy;
    }

    public ByteBuffer getAttachment() {
        return attachment;
    }

    public void setAttachment(ByteBuffer attachment) {
        this.attachment = attachment;
    }

    public String getAttachedBy() {
        return attachedBy;
    }

    public void setAttachedBy(String attachedBy) {
        this.attachedBy = attachedBy;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }
}
