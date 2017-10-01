package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
    private String parentGroupId;

    private List<GroupMemberEntity> member;

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

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }


    public List<GroupMemberEntity> getMember() {
        return member;
    }

    public void setMember(List<GroupMemberEntity> member) {
        this.member = member;
    }

    public void addMember(GroupMemberEntity groupMember){
        if(null == getMember())
            setMember(new ArrayList<GroupMemberEntity>());
        getMember().add(groupMember);
    }

    public String getTypeKey() {
        return TYPE;
    }
}
