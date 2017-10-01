package com.forealert.intf.api.repository;

import com.forealert.intf.dto.MessageDTO;
import com.forealert.intf.entity.MessageEntity;
import com.forealert.intf.entity.UserMessageEntity;
import com.forealert.intf.entity.type.MessageStatus;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/22/17
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MessageRepository extends FARepository{

    MessageEntity getById(String id);

    void saveAll(List<UserMessageEntity> userMessages);

    MessageDTO getMessageRecipient(String messageId);

    MessageDTO getMessageRecipient(String messageId, String groupId);

    List<MessageDTO> getUserMessage(String userId, long startTimeInMillis, MessageStatus... statuses);

}
