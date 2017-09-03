package com.forealert.repository.impl;

import com.forealert.intf.api.repository.MessageRepository;
import com.forealert.intf.entity.MessageEntity;
import com.forealert.intf.entity.UserMessageEntity;
import com.forealert.intf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/22/17
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageRepositoryImpl extends ForeAlterRepository implements MessageRepository {

    private Logger logger = LoggerFactory.getLogger(MessageRepositoryImpl.class);


    public MessageRepositoryImpl(CouchbaseTemplate couchbaseTemplate) {
        super(couchbaseTemplate);
    }

    public MessageEntity getById(String id) {
        return getCouchBaseTemplate().findById(id, MessageEntity.class);
    }

    @Override
    public void saveAll(List<UserMessageEntity> userMessages) {
        for(UserMessageEntity userMessage : userMessages){
            save(userMessage);
        }
    }
}
