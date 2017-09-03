package com.forealert.core.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forealert.core.notification.CustomPayload;
import com.forealert.core.notification.PushMessage;
import com.forealert.core.notification.PushMode;
import com.forealert.core.task.SendNotificationTask;
import com.forealert.core.util.ForeAlertHelper;
import com.forealert.intf.Constant;
import com.forealert.intf.bean.GeoPoint;
import com.forealert.core.util.FileHandler;
import com.forealert.intf.api.core.MessageBL;
import com.forealert.intf.api.repository.MessageRepository;
import com.forealert.intf.api.repository.UserRepository;
import com.forealert.intf.bean.ForeAlertBean;
import com.forealert.intf.dto.FileDTO;
import com.forealert.intf.entity.*;
import com.forealert.intf.entity.type.*;
import com.forealert.intf.exception.ForeAlertException;
import com.forealert.intf.exception.NoRecordFoundException;
import com.forealert.intf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageBLImpl implements MessageBL {

    private static Logger logger = LoggerFactory.getLogger(MessageBLImpl.class);

    private ExecutorService pnTaskExecutor;

    private MessageRepository messageRepository;

    private UserRepository userRepository;

    public MessageBLImpl(ForeAlertBean bean) {
        this.messageRepository = bean.getMessageRepository();
        this.userRepository = bean.getUserRepository();
        pnTaskExecutor = Executors.newFixedThreadPool(Integer.parseInt(bean.getProperties().getProperty("forealert.push.notification.task.size")));
    }

    public void reportIssue(MessageEntity message, List<FileDTO> files) {
        Double radius = 5.0;
        List<UserEntity> nearByUsers;
        do {
            GeoPoint geoPoint = new GeoPoint(message.getMessageLocation());
            nearByUsers = userRepository.findNearByUser(geoPoint, Role.ADMIN_3);
            radius += 5;
        } while (nearByUsers.size() <= 5 && radius <= 25);
        /*if(null == nearByUsers || nearByUsers.size()<=0)
            return;*/
        //String imageURI = uploadFile(files);
        //message.setImageURL(imageURI);
        String overlayCoordinatesUrl = FileHandler.uploadOverlayCoordinates(message.getOverlayCoordinates(), ForeAlertHelper.generateUniqueKey());
        message.setOverlayCoordinatesUrl(overlayCoordinatesUrl);
        UserEntity sender = updateUserPosition(message.getSenderLocation(), message.getSenderUUId(), message.getApp(), message.getDevice());
        message.setSenderId(sender.getId());
        messageRepository.save(message);
        PushMessage pushMessage = prepare(message, PushMode.C2A);
        for (UserEntity nearByUser : nearByUsers) {
            UserMessageEntity userMessage = new UserMessageEntity();
            userMessage.setMessageId(message.getId());
            userMessage.setUserId(nearByUser.getId());
            userMessage.setUserUUId(nearByUser.getUuId());
            userMessage.setSenderId(sender.getId());
            messageRepository.save(userMessage);
            pushMessage.addToken(nearByUser.getUuId());
        }
        sendNotification(pushMessage);
    }

    public void replyToIssue(MessageEntity message, List<FileDTO> files) {
        UserEntity user = updateUserPosition(message.getSenderLocation(), message.getSenderUUId(), message.getApp(), message.getDevice());
        if(user.hasRole(Role.ADMIN_3,Role.ADMIN_4) && !user.hasPrivilege(PrivilegeType.SEND))
            throw new SecurityException("You are not authorized for this operation");
        MessageEntity parentMessage = null;
        if (StringUtil.isNotBlank(message.getParentId())) {
            parentMessage = messageRepository.getById(message.getParentId());
            if (null == parentMessage)
                throw new NoRecordFoundException("Message not found for message id " + message.getParentId());
            if (parentMessage.isReplyBlocked()) {
                throw new ForeAlertException("Message reply blocked for message id " + message.getParentId());
            }
        }
        String imageURI = uploadFile(files);
        message.setImageURL(imageURI);
        String overlayCoordinatesUrl = FileHandler.uploadOverlayCoordinates(message.getOverlayCoordinates(), ForeAlertHelper.generateUniqueKey());
        message.setOverlayCoordinatesUrl(overlayCoordinatesUrl);
        messageRepository.save(message);
        if (user.hasRole(Role.ADMIN_3, Role.ADMIN_4)) {
            handleAdminMessage(message, user, parentMessage);
        } else {
            handleUserMessage(message, user);
        }
    }

    private void handleAdminMessage(MessageEntity message, UserEntity sender, MessageEntity parentMessage){
        PushMessage pushMessage = prepare(message);
        if (!message.isUserGEOMessage() && !message.isGroupGEOMessage() && message.getMessageType().equals(MessageType.GROUP_CHAT)) {
            //Creating group chat  from Admin
            List<GroupMemberEntity> groupAdmins;
            if (null != message.getGroupIds() && message.getGroupIds().size() > 0) {
                groupAdmins = userRepository.findGroupAdmin(message.getGroupIds());
            } else {  //TODO: need to throw exception
                groupAdmins = userRepository.findUerGroup(message.getSenderId());
            }
            for (GroupMemberEntity groupMember : groupAdmins) {
                UserMessageEntity userMessage = new UserMessageEntity();
                userMessage.setMessageId(message.getId());
                userMessage.setSenderId(sender.getId());
                userMessage.setGroupId(groupMember.getGroupId());
                userMessage.setUserId(groupMember.getUserId());
                messageRepository.save(userMessage);
                UserEntity member = userRepository.getUserById(groupMember.getUserId());
                pushMessage.addToken(member.getToken());
            }
            pushMessage.putCustom(Constant.PUSH_MODE_PAYLOAD, PushMode.A2AG.name());
            //TODO: send push notification
        } else {
            //Message to individual near by user  from Admin
            List<UserEntity> nearByUsers = new ArrayList<UserEntity>();
            PushMode pushMode = PushMode.A2CN;
            if (null != parentMessage) {
                if (message.getStatus().equals(MessageStatus.R)) {
                    parentMessage.setReplyBlocked(true);
                    messageRepository.update(parentMessage);
                    pushMode = PushMode.A2CO;
                } else {
                    if (null != message.getRecipientIds() && message.getRecipientIds().size() > 0) {
                        nearByUsers.addAll(userRepository.findUserByIds(message.getRecipientIds()));
                        pushMode = PushMode.A2UR;
                    }
                    nearByUsers.add(userRepository.getUserById(parentMessage.getSenderId()));
                }
            }
            /*if (!pushMode.equals(PushMode.A2UR)) {
                nearByUsers.addAll(findNearByAdmin(message.getMessageLocation()));
            }*/
            for (UserEntity nearByUser : nearByUsers) {
                UserMessageEntity userMessage = new UserMessageEntity();
                userMessage.setSenderId(sender.getId());
                userMessage.setMessageId(message.getId());
                userMessage.setUserId(nearByUser.getId());
                messageRepository.save(userMessage);
                pushMessage.addToken(nearByUser.getToken());
            }
            pushMessage.putCustom(Constant.PUSH_MODE_PAYLOAD, pushMode.name());
            //TODO: send push notification
        }
        sendNotification(pushMessage);
    }

    private void handleUserMessage(MessageEntity message, UserEntity sender){
        PushMessage pushMessage = prepare(message);
        //Sending Client message to near by user.
        if (StringUtil.isBlank(message.getParentId())) {
            throw new SecurityException("You are not authorized for this operation");
        }
        List<UserEntity> users;
        PushMode pushMode;
        if (null != message.getRecipientIds() && message.getRecipientIds().size() > 0) {
            users = userRepository.findUserByIds(message.getRecipientIds());
            pushMode = PushMode.C2A_1;
        } else {
            users = findNearByAdmin(message.getMessageLocation());
            pushMode = PushMode.C2A_n;
        }
        for (UserEntity nearByUser : users) {
            UserMessageEntity userMessage = new UserMessageEntity();
            userMessage.setMessageId(message.getId());
            userMessage.setUserId(nearByUser.getId());
            userMessage.setUserUUId(nearByUser.getUuId());
            userMessage.setSenderId(sender.getId());
            messageRepository.save(userMessage);
            pushMessage.addToken(nearByUser.getUuId());
        }
        pushMessage.putCustom(Constant.PUSH_MODE_PAYLOAD, pushMode.name());
        sendNotification(pushMessage);
        //TODO: send push notification

        //TODO: need to handle reply
        /*PushMessage messageSenderPM = null;
        try {
            messageSenderPM = (PushMessage) pushMessage.clone();
        } catch (CloneNotSupportedException e) {
           messageSenderPM = prepare(message);
        }
        messageSenderPM.setTokens(null);
        messageSenderPM.addToken(user.getUuId());
        messageSenderPM.putCustom(Constant.PUSH_MODE_PAYLOAD, PushMode.C2A.name());
        sendNotification(messageSenderPM);*/
        //TODO: send push notification
    }


    private String uploadFile(List<FileDTO> files) {
        StringBuilder imageURLBuilder = new StringBuilder();
        if (null != files) {
            for (FileDTO file : files) {
                imageURLBuilder.append(FileHandler.uploadFileToS3UsingTM(file.getContent(), file.getName())).append(";");
            }
        }
        return imageURLBuilder.toString();
    }

    private List<UserEntity> findNearByUser(Location location, Role... roles) {
        GeoPoint geoPoint = new GeoPoint(location);
        return userRepository.findNearByUser(geoPoint, roles);
    }

    private List<UserEntity> findNearByAdmin(Location location) {
        return findNearByUser(location, Role.ADMIN_3, Role.ADMIN_4);
    }

    private PushMessage prepare(MessageEntity message, PushMode pushMode) {
        PushMessage pushMessage = prepare(message);
        pushMessage.putCustom(Constant.PUSH_MODE_PAYLOAD, pushMode.name());
        return pushMessage;
    }

    private PushMessage prepare(MessageEntity message) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setDeviceType(message.getDevice());
        pushMessage.setMessage(message.getMessage());
        String custom = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            custom = mapper.writeValueAsString(new CustomPayload(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Custom payload convert as string failed:- " + e.getMessage());
            custom = "FAILED";
        }
        pushMessage.putCustom("CUSTOM", custom);
        return pushMessage;
    }

    private UserEntity updateUserPosition(Location location, String userUUId, AppEnum app, DeviceType device) {
        UserEntity user = userRepository.findUserByUUId(userUUId);
        if (null == user) {
            user = new UserEntity();
            user.setApp(app);
            user.setUuId(userUUId);
            user.setDevice(device);
            user.setRole(Role.USER);
        }
        user.setUpdatedAt(new Date());
        user.setLocation(location);
        userRepository.save(user);
        return user;
    }

    public MessageEntity getMessage(String id) {
        MessageEntity message = messageRepository.getById(id);
        if (null == message)
            throw new NoRecordFoundException("No record found for message id:- " + id);
        return message;
    }

    public void deleteMessage(String id) {

    }

    private void sendNotification(PushMessage pushMessage) {
        //TODO: send notification
        this.pnTaskExecutor.submit(new SendNotificationTask(pushMessage));
    }
}
