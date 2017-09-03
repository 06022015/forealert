package com.forealert.web.security;

import java.security.Principal;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FAPrincipal implements Principal {
    
    private String userId;
    private String email;
    private String mobile;
    private String role;

    public FAPrincipal(String email, String mobile, String role) {
        this.email = email;
        this.mobile = mobile;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getRole() {
        return role;
    }
}
