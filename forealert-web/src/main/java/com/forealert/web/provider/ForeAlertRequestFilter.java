package com.forealert.web.provider;

import com.forealert.web.security.FAPrincipal;
import com.forealert.web.security.FASecured;
import com.forealert.web.security.FASecurity;
import com.forealert.web.security.FASecurityContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Provider
@FASecured
@Priority(Priorities.AUTHENTICATION)
public class ForeAlertRequestFilter implements ContainerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(ForeAlertRequestFilter.class);

    @Autowired
    private FASecurity security;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        //Access denied for all
        if(method.isAnnotationPresent(DenyAll.class)){
            throw  new ForbiddenException();
        }
        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(null == authorizationHeader)
            throw new SignatureException("Authorization header not available");
        Claims claims = validateToken(authorizationHeader);
        FAPrincipal principal = buildPrincipal(claims);
        FASecurityContext securityContext = new FASecurityContext(principal,containerRequestContext.getSecurityContext().isSecure());
        securityContext.addRole(principal.getRole());
        //Verify user access
        boolean isRoleMatched = false;
        if(method.isAnnotationPresent(RolesAllowed.class)){
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
            //Is user valid?
            for(String role : rolesSet){
                if(securityContext.isUserInRole(role)){
                    isRoleMatched = true;
                    break;
                }
            }
        }
        if(!isRoleMatched)
            throw  new ForbiddenException();
        containerRequestContext.setSecurityContext(securityContext);
    }


    private Claims validateToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(security.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    private FAPrincipal buildPrincipal(Claims claims){
        String userName = claims.getSubject();
        String role = (String) claims.get("role");
        String mobile = (String)claims.get("mobile");
        FAPrincipal faPrincipal = new FAPrincipal(userName, mobile, role);
        faPrincipal.setUserId(claims.getId());
        return faPrincipal;
    }

}
