package com.forealert.web.service;

import com.codahale.metrics.annotation.Timed;
import com.forealert.intf.api.core.UserBL;
import com.forealert.intf.dto.ForeAlertStatus;
import com.forealert.intf.dto.UserPermissionDTO;
import com.forealert.intf.entity.Location;
import com.forealert.intf.entity.UserEntity;
import com.forealert.web.security.FASecured;
import com.forealert.web.security.FASecurity;
import com.forealert.web.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/v1/user")
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private FASecurity security;

    @Autowired
    private UserBL userBL;
    
    @POST
    @Path("/authenticate")
    public Response authenticate( @FormParam("mobile") String mobile){
      return  Response.status(HttpStatus.OK.value()).header(HttpHeaders.AUTHORIZATION, security.getToken(mobile, "USER")).build();
    }
    
    @GET
    @FASecured
    @Path("/logout")
    public Response logout(@QueryParam("uuid") String uuid, @QueryParam("googleUserId")String googleUserId){
        userBL.logout(uuid, googleUserId);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(UserEntity user){
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(user, status);
        if(status.hasError()){
           return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        userBL.save(user);
        return  Response.status(HttpStatus.OK.value()).entity(user).build();
    }

    //TODO: Need to find way to update user info.
    @PUT
    @Path("/{userId}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateUser(@PathParam("userId")String userId) {
        logger.info(userId);
        UserEntity user = userBL.getUserById(userId);
        return Response.ok().entity(user).build();
    }

    @PUT
    @Path("/{userId}/position")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updatePosition(@PathParam("userId")String userId, Location location) {
        logger.info(userId);
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(location, status);
        if(status.hasError()){
            return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        userBL.updatePosition(location, userId);
        return Response.status(HttpStatus.ACCEPTED.value()).entity(userId).build();
    }

    @GET
    @Path("/{userId}")
    public Response getUserById(@PathParam("userId")String userId){
        logger.info(userId);
        UserEntity user = userBL.getUserById(userId);
        return Response.ok().entity(user).build();
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUserById(@PathParam("userId")String userId){
        logger.info(userId);
        userBL.deleteUser(userId);
        return Response.ok().build();
    }

    @GET
    @Path("/purge")
    public Response purge(@QueryParam("userUuid")String uuid){
        //TODO: will revisit.
        return Response.ok().build();
    }


    @PUT
    @Path("/{userId}/admin/approve")
    public Response adminApprove(@PathParam("userId")String userId, UserPermissionDTO userPermissionDTO){
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(userPermissionDTO, status);
        if(status.hasError()){
            return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        userBL.adminApprove(userId, userPermissionDTO);
        return Response.accepted().build();
    }


    @PUT
    @Path("/{userId}/admin/unapprove")
    public Response adminUnApprove(@PathParam("userId")String userId){
        userBL.adminUnApprove(userId);
        return Response.accepted().build();
    }


    @POST
    @Timed
    @Path("/nearby")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response nearByUser(Location location){
        List<UserEntity> nearByUsers = userBL.getNearByUser(location);
        return  Response.status(HttpStatus.OK.value()).entity(nearByUsers).build();
    }

}
