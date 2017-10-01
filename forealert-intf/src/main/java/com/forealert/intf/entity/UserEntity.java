package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.forealert.intf.entity.type.AppEnum;
import com.forealert.intf.entity.type.DeviceType;
import com.forealert.intf.entity.type.PrivilegeType;
import com.forealert.intf.entity.type.Role;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/26/17
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "user")
@Document(expiry = 0)
public class UserEntity extends Base{

    public static String TYPE = "FUser";
    
    @Field
    private String uuId;
    @Field
    private String name;
    @Field
    private String email;
    @Field
    private String username;
    @Field
    private String mobile;
    @Field
    private String profileURL;
    @Field
    private Role role = Role.USER;
    @Field
    private DeviceType device;
    @Field
    private AppEnum app;
    @Field
    private Location location;
    @Field
    private boolean toPurge;
    @Field
    private boolean active = true;
    @Field
    private String googleUserId;
    @Field
    private String token;
    @Field
    private boolean loggedOut = false;
    @Field
    private boolean trackingOff = false;
    @Field
    private Set<PrivilegeType> privileges;

    public UserEntity() {
        setTypeKey(TYPE);
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public DeviceType getDevice() {
        return device;
    }

    public void setDevice(DeviceType device) {
        this.device = device;
    }

    public AppEnum getApp() {
        return app;
    }

    public void setApp(AppEnum app) {
        this.app = app;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isToPurge() {
        return toPurge;
    }

    public void setToPurge(boolean toPurge) {
        this.toPurge = toPurge;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTypeKey() {
        return TYPE;
    }

    public String getGoogleUserId() {
        return googleUserId;
    }

    public void setGoogleUserId(String googleUserId) {
        this.googleUserId = googleUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        this.loggedOut = loggedOut;
    }

    public boolean isTrackingOff() {
        return trackingOff;
    }

    public void setTrackingOff(boolean trackingOff) {
        this.trackingOff = trackingOff;
    }

    public Set<PrivilegeType> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<PrivilegeType> privileges) {
        this.privileges = privileges;
    }

    public void addPrivilege(PrivilegeType privilegeType){
         if(null == getPrivileges())
             setPrivileges(new HashSet<PrivilegeType>());
        if(!getPrivileges().contains(privilegeType)){
            getPrivileges().add(privilegeType);
        }
    }

    public Boolean hasPrivilege(PrivilegeType privilegeType){
        return null != getPrivileges() && getPrivileges().contains(privilegeType);
    }

    public Boolean hasRole(Role... roles){
        boolean hasRole = false;
        for(Role role : roles){
            if(role.equals(getRole())){
                hasRole = true;
                break;
            }
        }
        return hasRole;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uuId='" + uuId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", profileURL='" + profileURL + '\'' +
                ", role=" + role +
                ", device=" + device +
                ", app=" + app +
                ", location=" + location +
                ", toPurge=" + toPurge +
                ", active=" + active +
                ", googleUserId='" + googleUserId + '\'' +
                ", token='" + token + '\'' +
                ", loggedOut=" + loggedOut +
                ", trackingOff=" + trackingOff +
                ", privileges=" + privileges +
                '}';
    }
}
