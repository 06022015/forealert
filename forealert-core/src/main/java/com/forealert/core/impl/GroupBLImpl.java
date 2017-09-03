package com.forealert.core.impl;

import com.forealert.core.util.FileHandler;
import com.forealert.intf.api.core.GroupBL;
import com.forealert.intf.api.repository.GroupRepository;
import com.forealert.intf.api.repository.UserRepository;
import com.forealert.intf.bean.ForeAlertBean;
import com.forealert.intf.dto.FileDTO;
import com.forealert.intf.dto.UserPermissionDTO;
import com.forealert.intf.entity.*;
import com.forealert.intf.entity.type.PrivilegeType;
import com.forealert.intf.exception.NoRecordFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/20/17
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupBLImpl implements GroupBL {

    private static Logger logger = LoggerFactory.getLogger(GroupBLImpl.class);

    private GroupRepository groupRepository;
    
    private UserRepository userRepository;

    public GroupBLImpl(ForeAlertBean bean) {
         this.groupRepository = bean.getGroupRepository();
        this.userRepository = bean.getUserRepository();
    }

    public void save(String userId, GroupEntity group) {
        group.setCreatedBy(userId);
        groupRepository.save(group);
        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(group.getId());
        member.setAdmin(true);
        member.addPrivilege(PrivilegeType.SEND);
        member.setUserId(userId);
        groupRepository.save(member);
    }

    @Override
    public GroupEntity getGroupById(String groupId) {
        GroupEntity group = groupRepository.getById(groupId);
        if(null == group)
            throw new NoRecordFoundException("No group found for group id:- "+ groupId);
        return group;
    }

    public GroupEntity getUserGroup(String userId, String groupId) {
        return null;
    }

    @Override
    public List<GroupAttachmentEntity> getAttachment(String groupId) {
        return groupRepository.getGroupAttachment(groupId);
    }

    @Override
    public GroupAttachmentEntity getAttachmentById(String groupId, String fileId) {
        GroupAttachmentEntity groupAttachment = groupRepository.getAttachmentById(groupId, fileId);
        if(null == groupAttachment)
            throw new NoRecordFoundException("Group attachment found for group id:- "+ groupId +" and file id:- "+ fileId);
        return groupAttachment;
    }

    @Override
    public void save(GroupAttachmentEntity groupAttachment, List<FileDTO> fileDTOs) {
        GroupEntity group = groupRepository.getById(groupAttachment.getGroupId());
        if (null == group)
            throw new NoRecordFoundException("No group found for group id:- " + groupAttachment.getGroupId());
        String imagePath = uploadFile(fileDTOs);
        groupAttachment.setURL(imagePath);
        groupRepository.save(groupAttachment);
    }

    @Override
    public List<EmojiEntity> getEmoji(String groupId) {
        return groupRepository.getEmojis(groupId);
    }

    @Override
    public void addGroupMember(String groupId, String userUUId, UserPermissionDTO userPermissionDTO) {
        GroupEntity group = groupRepository.getById(groupId);
        if(null == group)
            throw new NoRecordFoundException("No user found for group id:- "+ groupId);
        UserEntity user = userRepository.findUserByUUId(userUUId);
        if(null == user)
            throw new NoRecordFoundException("No user found for user UUID:- "+ userUUId);
        GroupMemberEntity groupMember = new GroupMemberEntity();
        groupMember.setUserId(user.getId());
        groupMember.setGroupId(groupId);
        if(null != userPermissionDTO.isAdmin())
            groupMember.setAdmin(userPermissionDTO.isAdmin());
        if(null != userPermissionDTO.getPrivileges() && userPermissionDTO.getPrivileges().size()>0){
            for(PrivilegeType privilegeType : userPermissionDTO.getPrivileges())
                groupMember.addPrivilege(privilegeType);
        }
        groupRepository.save(groupMember);
    }

    @Override
    public void changeMemberPermission(String groupId, String userUUId, UserPermissionDTO userPermissionDTO) {
        UserEntity user = userRepository.findUserByUUId(userUUId);
        if(null == user)
            throw new NoRecordFoundException("No user found for user UUID:- "+ userUUId);
        GroupMemberEntity groupMember = groupRepository.getGroupMember(groupId, user.getId());
        if(null == groupMember)
            throw new NoRecordFoundException("No member found for group id:- "+ groupId+ " and user id:- "+ user.getId());
        if(null != userPermissionDTO.isAdmin())
            groupMember.setAdmin(userPermissionDTO.isAdmin());
        if(null != userPermissionDTO.getPrivileges() && userPermissionDTO.getPrivileges().size()>0){
             for(PrivilegeType privilegeType : userPermissionDTO.getPrivileges())
                 groupMember.addPrivilege(privilegeType);
        }
        groupRepository.save(groupMember);
    }

    @Override
    public void removeGroupMember(String groupId, String userUUId) {
        UserEntity user = userRepository.findUserByUUId(userUUId);
        if(null == user)
            throw new NoRecordFoundException("No user found for user UUID:- "+ userUUId);
        GroupMemberEntity groupMember = groupRepository.getGroupMember(groupId, user.getId());
        if(null == groupMember)
            throw new NoRecordFoundException("No member found for group id:- "+ groupId+ " and user id:- "+ user.getId());
        groupRepository.remove(groupMember);
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

}
