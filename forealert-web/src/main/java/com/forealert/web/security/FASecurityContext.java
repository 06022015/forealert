package com.forealert.web.security;

import com.forealert.intf.entity.type.PrivilegeType;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class FASecurityContext implements SecurityContext {
    
    private static String AUTH_SCHEME = "JWT";

    private FAPrincipal principal;
    private boolean      isSecure;
    private Set<String> roles = new HashSet<String>();
    private Set<PrivilegeType>  privilegeTypes;

    public FASecurityContext(FAPrincipal principal, boolean secure) {
        isSecure = secure;
        this.principal = principal;

    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isUserInRole(String s) {
        return roles.contains(s);
    }

    @Override
    public boolean isSecure() {
        return this.isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return AUTH_SCHEME;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    public void addRole(String role){
        if(null == getRoles())
            setRoles(new HashSet<String>());
        getRoles().add(role);
    }
}
