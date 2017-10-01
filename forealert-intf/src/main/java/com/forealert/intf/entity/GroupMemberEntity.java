package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forealert.intf.entity.type.PrivilegeType;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/26/17
 * Time: 8:48 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "group_member")
@Document(expiry = 0)
public class GroupMemberEntity extends Base{

    public static String TYPE = "GroupMember";

    @Field
    private String groupId;
    @Field
    private String userId;
    @Field
    private Set<PrivilegeType> privilege;
    @Field
    private boolean admin;

    private UserEntity user;

    public GroupMemberEntity() {
        setTypeKey(TYPE);
    }

    @JsonIgnore
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @JsonIgnore
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Set<PrivilegeType> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Set<PrivilegeType> privilege) {
        this.privilege = privilege;
    }

    public void addPrivilege(PrivilegeType privilegeType){
        if(null == getPrivilege())
            setPrivilege(new HashSet<PrivilegeType>());
        getPrivilege().add(privilegeType);
    }

    public boolean hasPrivilege(PrivilegeType privilegeType){
        if(null == getPrivilege()){
            return getPrivilege().contains(privilegeType);
        }
        return false;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }
}
