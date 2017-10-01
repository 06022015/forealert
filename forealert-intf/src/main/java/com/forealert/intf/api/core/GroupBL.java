package com.forealert.intf.api.core;

import com.forealert.intf.dto.FileDTO;
import com.forealert.intf.dto.UserPermissionDTO;
import com.forealert.intf.entity.EmojiEntity;
import com.forealert.intf.entity.GroupAttachmentEntity;
import com.forealert.intf.entity.GroupEntity;
import com.forealert.intf.entity.GroupMemberEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/20/17
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroupBL {

    void save(String userId, GroupEntity group);
    
    GroupEntity getGroupById(String groupId);

    GroupEntity getUserGroup(String userId, String groupId);
    
    List<GroupAttachmentEntity> getAttachment(String groupId);
    
    GroupAttachmentEntity getAttachmentById(String groupId, String fileId);

    void save(GroupAttachmentEntity groupAttachment, List<FileDTO> fileDTOs);

    List<EmojiEntity> getEmoji(String groupId);

    void addGroupMember(String groupId, String userId, UserPermissionDTO userPermissionDTO);

    void changeMemberPermission(String groupId, String userId, UserPermissionDTO userPermissionDTO);
    
    void removeGroupMember(String groupId, String userUUId);
    
    List<GroupMemberEntity>  getGroupMember(String groupId);
    
}
