package com.forealert.web.service;

import com.forealert.intf.api.core.GroupBL;
import com.forealert.intf.dto.FileDTO;
import com.forealert.intf.dto.ForeAlertStatus;
import com.forealert.intf.dto.UserPermissionDTO;
import com.forealert.intf.entity.EmojiEntity;
import com.forealert.intf.entity.GroupAttachmentEntity;
import com.forealert.intf.entity.GroupEntity;
import com.forealert.intf.util.StringUtil;
import com.forealert.web.security.FAPrincipal;
import com.forealert.web.security.FASecured;
import com.forealert.web.security.FASecurityContext;
import com.forealert.web.util.ValidatorUtil;
import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/13/17
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/v1/group")
public class GroupService {

    private Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Context
    private FASecurityContext securityContext;

    @Autowired
    private GroupBL groupBL;

    @GET
    public Response getUserGroup(){
        return Response.ok().entity(new GroupEntity()).build();
    }

    @GET
    @Path("/{groupId}")
    public Response getGroup(@PathParam("groupId")String groupId){
        GroupEntity group = groupBL.getGroupById(groupId);
         return Response.ok().entity(group).build();
    }

    @DELETE
    @Path("/{groupId}")
    public Response deleteGroup(@PathParam("groupId")String groupId){
        return Response.ok().entity(new GroupEntity()).build();
    }

    @POST
    @FASecured
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGroup(GroupEntity group){
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(group, status);
        if(status.hasError()){
            return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        String userId = ((FAPrincipal)securityContext.getUserPrincipal()).getUserId();
        groupBL.save(userId, group);
        return  Response.status(HttpStatus.OK.value()).entity(group).build();
    }

    @GET
    @Path("/{groupId}/files")
    public Response getGroupFiles(@PathParam("groupId")String groupId){
        List<GroupAttachmentEntity> groupAttachments = groupBL.getAttachment(groupId);
        return Response.ok().entity(groupAttachments).build();
    }

    @GET
    @Path("/{groupId}/file/{fileId}")
    public Response getFile(@PathParam("groupId")String groupId, @PathParam("fileId")String fileId){
        List<GroupAttachmentEntity> groupAttachments = groupBL.getAttachment(groupId);
        return Response.ok().entity(groupAttachments).build();
    }

    @POST
    @Path("/{groupId}/files")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response groupFiles(@PathParam("groupId")String groupId, @FormDataParam("attributes") String attributes,
                               @FormDataParam("file")FormDataBodyPart body){
        logger.debug(attributes);
        GroupAttachmentEntity groupAttachment =  new Gson().fromJson(attributes, GroupAttachmentEntity.class);
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(groupAttachment, status);
        if(status.hasError()){
            return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        groupAttachment.setGroupId(groupId);
        List<FileDTO> files = new ArrayList<FileDTO>();
        if(null != body){
            for(BodyPart part : body.getParent().getBodyParts()){
                if(null != part && null !=part.getContentDisposition()
                        && StringUtil.isNotBlank(part.getContentDisposition().getFileName()) && null != part.getEntity()){
                    files.add(new FileDTO(part.getContentDisposition().getFileName(),
                            part.getEntityAs(InputStream.class), part.getContentDisposition().getType()));
                }
            }
        }
        groupBL.save(groupAttachment, files);
        return Response.ok().entity(groupAttachment.getId()).build();
    }

    @GET
    @Path("/{groupId}/icons")
    public Response getGroupIcons(@PathParam("groupId")String groupId){
        List<EmojiEntity> emojies = groupBL.getEmoji(groupId);
        return Response.ok().entity(emojies).build();
    }

    @GET
    @Path("/{groupId}/members")
    public Response getGroupMembers(@PathParam("groupId")String groupId){
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/{groupId}/member/{userUUId}")
    public Response addGroupMembers(@PathParam("groupId")String groupId, @PathParam("userUUId")String userUUId,
                                    UserPermissionDTO userPermissionDTO){
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(userPermissionDTO, status);
        if(status.hasError()){
            return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        groupBL.changeMemberPermission(groupId, userUUId, userPermissionDTO);
        return Response.accepted().build();
    }


    @PUT
    @Path("/{groupId}/member/{userUUId}")
    public Response changeUserPermission(@PathParam("groupId")String groupId, @PathParam("userUUId")String userUUId,
                                    UserPermissionDTO userPermissionDTO){
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(userPermissionDTO, status);
        if(status.hasError()){
            return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        groupBL.changeMemberPermission(groupId, userUUId, userPermissionDTO);
        return Response.accepted().build();
    }

    @DELETE
    @Path("/{groupId}/member/{userUUId}")
    public Response removeGroupMembers(@PathParam("groupId")String groupId, @PathParam("userUUId")String userUUId){
        groupBL.removeGroupMember(groupId, userUUId);
        return Response.accepted().build();
    }
}
