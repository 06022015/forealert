package com.forealert.intf.dto;

import com.forealert.intf.entity.type.PrivilegeType;
import com.forealert.intf.entity.type.Role;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/31/17
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "permission")
public class UserPermissionDTO {

    private Role role;
    private Boolean admin;
    private Set<PrivilegeType> privileges;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Set<PrivilegeType> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<PrivilegeType> privileges) {
        this.privileges = privileges;
    }
}
