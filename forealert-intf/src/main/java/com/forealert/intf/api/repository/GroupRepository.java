package com.forealert.intf.api.repository;

import com.forealert.intf.entity.EmojiEntity;
import com.forealert.intf.entity.GroupAttachmentEntity;
import com.forealert.intf.entity.GroupEntity;
import com.forealert.intf.entity.GroupMemberEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/20/17
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GroupRepository extends FARepository{
    
    
    List<GroupAttachmentEntity> getGroupAttachment(String groupId);
    
    GroupAttachmentEntity getAttachmentById(String groupId, String attachmentId);

    GroupEntity getById(String groupId);

    List<EmojiEntity> getEmojis(String groupId);
    
    GroupMemberEntity getGroupMember(String groupId, String userId);

    List<GroupMemberEntity> getGroupMemberDetail(String groupId);


}
