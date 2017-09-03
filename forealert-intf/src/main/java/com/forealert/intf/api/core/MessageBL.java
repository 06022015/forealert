package com.forealert.intf.api.core;

import com.forealert.intf.dto.FileDTO;
import com.forealert.intf.entity.MessageEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MessageBL {

    void reportIssue(MessageEntity message, List<FileDTO> files);

    void replyToIssue(MessageEntity message, List<FileDTO> files);

    MessageEntity getMessage(String id);

    void deleteMessage(String id);

}
