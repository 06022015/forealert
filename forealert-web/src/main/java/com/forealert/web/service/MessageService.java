package com.forealert.web.service;

import com.forealert.intf.api.core.MessageBL;
import com.forealert.intf.dto.FileDTO;
import com.forealert.intf.dto.ForeAlertStatus;
import com.forealert.intf.entity.MessageEntity;
import com.forealert.intf.util.StringUtil;
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
 * Date: 7/22/17
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/v1/message")
@Produces({MediaType.APPLICATION_JSON})
public class MessageService {

    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Context
    private SecurityContext securityContext;

    @Autowired
    private MessageBL messageBL;

    @GET
    @Path("/{messageId}")
    public Response get(@PathParam("messageId")String id) {
        MessageEntity message = messageBL.getMessage(id);
        return Response.ok(message).build();
    }

    @POST
    @Path("/report/issue")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response reportIssue(@FormDataParam("attributes") String attributes, @FormDataParam("file")FormDataBodyPart body) {
        logger.debug(attributes);
        MessageEntity message =  new Gson().fromJson(attributes, MessageEntity.class);
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(message, status);
        if(status.hasError()){
           return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        List<FileDTO>  files = new ArrayList<FileDTO>();
        if(null != body){
            for(BodyPart part : body.getParent().getBodyParts()){
                if(null != part && null !=part.getContentDisposition()
                        && StringUtil.isNotBlank(part.getContentDisposition().getFileName()) && null != part.getEntity()){
                    files.add(new FileDTO(part.getContentDisposition().getFileName(),
                            part.getEntityAs(InputStream.class), part.getContentDisposition().getType()));
                }
            }
        }
        messageBL.reportIssue(message, files);
        return Response.ok().entity(message.getId()).build();
    }

    @POST
    @Path("/reply")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reply(@FormDataParam("attributes") String attributes, @FormDataParam("file")FormDataBodyPart body) {
        logger.debug(attributes);
        MessageEntity message =  new Gson().fromJson(attributes, MessageEntity.class);
        ForeAlertStatus status = new ForeAlertStatus();
        ValidatorUtil.validate(message, status);
        if(status.hasError()){
           return Response.status(HttpStatus.BAD_REQUEST.value()).entity(status).build();
        }
        List<FileDTO>  files = new ArrayList<FileDTO>();
        if(null != body){
            for(BodyPart part : body.getParent().getBodyParts()){
                if(null != part && null !=part.getContentDisposition()
                        && StringUtil.isNotBlank(part.getContentDisposition().getFileName()) && null != part.getEntity()){
                    files.add(new FileDTO(part.getContentDisposition().getFileName(),
                            part.getEntityAs(InputStream.class), part.getContentDisposition().getType()));
                }
            }
        }
        messageBL.replyToIssue(message, files);
        return Response.ok().entity(message.getId()).build();
    }


    /*@GET
    @Path("/{messageId}/reply")
    public Response getMessageReply(@PathParam("messageId")Long id) {
        return Response.ok().entity("reply").build();
    }*/

    @DELETE
    @Path("/{messageId}")
    public Response delete(@PathParam("messageId")Long id) {
        return Response.ok().entity("Delete").build();
    }

}
