package com.forealert.repository.impl;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.dsl.Alias;
import com.couchbase.client.java.query.dsl.Expression;
import com.couchbase.client.java.query.dsl.Sort;
import com.forealert.intf.Constant;
import com.forealert.intf.api.repository.MessageRepository;
import com.forealert.intf.dto.MessageDTO;
import com.forealert.intf.entity.*;
import com.forealert.intf.entity.type.MessageStatus;
import com.forealert.intf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.ArrayList;
import java.util.Calendar;
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
    
    public MessageDTO getMessageRecipient(String messageId){
        Statement statement = Select.select(UserMessageEntity.TYPE + ", " + UserEntity.TYPE + ", " + MessageEntity.TYPE + ", sender,  META(" + UserMessageEntity.TYPE + ").id as _ID, META(" + UserMessageEntity.TYPE + ").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(UserMessageEntity.TYPE)))
                .join(Expression.i(Constant.BUCKET_NAME).as(MessageEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`messageId"))
                .join(Expression.i(Constant.BUCKET_NAME).as(UserEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`userId"))
                .join(Expression.i(Constant.BUCKET_NAME).as("sender"))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`senderId"))
                .where(Expression.i(UserMessageEntity.TYPE+"`.`typeKey").eq(Expression.s(UserMessageEntity.TYPE))
                        .and(Expression.i(UserMessageEntity.TYPE + "`.`messageId").eq(Expression.s(messageId))));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        N1qlQueryResult queryResult = getCouchBaseTemplate().queryN1QL(query);
        MessageDTO messageDTO = null;
        int i=0;
        if (queryResult.finalSuccess()) {
            messageDTO = new MessageDTO();
            List<N1qlQueryRow> allRows = queryResult.allRows();
            for (N1qlQueryRow row : allRows) {
                JsonObject json = row.value();
                String id = json.getString(getCouchBaseTemplate().SELECT_ID);
                JsonObject um = json.getObject(UserMessageEntity.TYPE);
                UserMessageEntity userMessage = getGson().fromJson(um.toString(), UserMessageEntity.class);
                userMessage.setId(id);
                UserEntity receiver = getGson().fromJson(json.getObject(UserEntity.TYPE).toString(),UserEntity.class);
                receiver.setId(userMessage.getUserId());
                messageDTO.addRecipient(receiver, userMessage.getStatus());
                if(i==0){
                    MessageEntity message = getGson().fromJson(json.getObject(MessageEntity.TYPE).toString(), MessageEntity.class);
                    message.setId(userMessage.getMessageId());
                    messageDTO.setMessage(message);
                    UserEntity sender = getGson().fromJson(json.getObject("sender").toString(),UserEntity.class);
                    sender.setId(userMessage.getSenderId());
                    messageDTO.setSender(sender);
                }
                i++;
            }
        }
        return messageDTO;
    }

    public MessageDTO getMessageRecipient(String messageId, String groupId){
        Statement statement = Select.select(UserMessageEntity.TYPE + ", " + UserEntity.TYPE + ", " + GroupEntity.TYPE + ", " + MessageEntity.TYPE + ", sender,  META(" + UserMessageEntity.TYPE + ").id as _ID, META(" + UserMessageEntity.TYPE + ").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(UserMessageEntity.TYPE)))
                .join(Expression.i(Constant.BUCKET_NAME).as(MessageEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`messageId"))
                .join(Expression.i(Constant.BUCKET_NAME).as(UserEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`userId"))
                .join(Expression.i(Constant.BUCKET_NAME).as(GroupEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`groupId"))
                .join(Expression.i(Constant.BUCKET_NAME).as("sender"))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`senderId"))
                .where(Expression.i(UserMessageEntity.TYPE + "`.`typeKey").eq(Expression.s(UserMessageEntity.TYPE))
                        .and(Expression.i(UserMessageEntity.TYPE + "`.`messageId").eq(Expression.s(messageId)))
                        .and(Expression.i(UserMessageEntity.TYPE + "`.`groupId").isNotMissing())
                        .and(Expression.i(UserMessageEntity.TYPE + "`.`groupId").eq(Expression.s(groupId))));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        N1qlQueryResult queryResult = getCouchBaseTemplate().queryN1QL(query);
        MessageDTO messageDTO = null;
        int i=0;
        if (queryResult.finalSuccess()) {
            List<N1qlQueryRow> allRows = queryResult.allRows();
            messageDTO = new MessageDTO();
            for (N1qlQueryRow row : allRows) {
                JsonObject json = row.value();
                String id = json.getString(getCouchBaseTemplate().SELECT_ID);
                JsonObject um = json.getObject(UserMessageEntity.TYPE);
                UserMessageEntity userMessage = getGson().fromJson(um.toString(), UserMessageEntity.class);
                userMessage.setId(id);
                UserEntity receiver = getGson().fromJson(json.getObject(UserEntity.TYPE).toString(),UserEntity.class);
                receiver.setId(userMessage.getUserId());
                messageDTO.addRecipient(receiver, userMessage.getStatus());
                if(i==0){
                    MessageEntity message = getGson().fromJson(json.getObject(MessageEntity.TYPE).toString(), MessageEntity.class);
                    message.setId(userMessage.getMessageId());
                    messageDTO.setMessage(message);
                    UserEntity sender = getGson().fromJson(json.getObject("sender").toString(),UserEntity.class);
                    sender.setId(userMessage.getSenderId());
                    messageDTO.setSender(sender);
                    GroupEntity group = getGson().fromJson(json.getObject(GroupEntity.TYPE).toString(),GroupEntity.class);
                    group.setId(userMessage.getGroupId());
                    messageDTO.setGroup(group);
                }
                i++;
            }
        }
        return messageDTO;
    }

    public List<MessageDTO> getUserMessage(String userId, long startTimeInMillis, MessageStatus... statuses){
        logger.info("Calling user message");
        Statement statement = Select.select(UserMessageEntity.TYPE + ", " + UserEntity.TYPE + ", " + MessageEntity.TYPE + ", sender,  _ID, _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(UserMessageEntity.TYPE))).useIndex("user_message_index")
                .join(Expression.i(Constant.BUCKET_NAME).as(MessageEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`messageId"))
                .join(Expression.i(Constant.BUCKET_NAME).as(UserEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`userId"))
                .join(Expression.i(Constant.BUCKET_NAME).as("sender"))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`senderId"))
                .where(Expression.i(UserMessageEntity.TYPE + "`.`typeKey").eq(Expression.s(UserMessageEntity.TYPE))
                        .and(Expression.par(Expression.i(UserMessageEntity.TYPE + "`.`userId").eq(Expression.s(userId))
                                .or(Expression.i(UserMessageEntity.TYPE + "`.`senderId").eq(Expression.s(userId)))))
                        .and(Expression.i(UserMessageEntity.TYPE + "`.`updatedAt").gte(startTimeInMillis))
                        .and(Expression.i(MessageEntity.TYPE + "`.`status").in(JsonArray.from(objectAsList(statuses))))
                        .and(Expression.par(Expression.i(MessageEntity.TYPE + "`.`parentId").isMissing()
                                .or(Expression.i(MessageEntity.TYPE + "`.`parentId").isNull()))))
                .groupBy(Expression.i(UserMessageEntity.TYPE + "`.`messageId`, `" + UserMessageEntity.TYPE + "`.`senderId"))
                .letting(Alias.alias(UserMessageEntity.TYPE, UserMessageEntity.TYPE),
                        Alias.alias(MessageEntity.TYPE, MessageEntity.TYPE), Alias.alias(UserEntity.TYPE, UserEntity.TYPE),
                        Alias.alias("sender", "sender"), Alias.alias("_ID", "META(" + UserMessageEntity.TYPE + ").id "),
                        Alias.alias("_CAS", "META(" + UserMessageEntity.TYPE + ").cas"))
                .orderBy(Sort.desc(Expression.i(UserMessageEntity.TYPE+"`.`updatedAt")));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        N1qlQueryResult queryResult = getCouchBaseTemplate().queryN1QL(query);
        logger.info("Got data from couchbase");
        List<MessageDTO> messageDTOs = new ArrayList<MessageDTO>();
        if (queryResult.finalSuccess()) {
            List<N1qlQueryRow> allRows = queryResult.allRows();
            for (N1qlQueryRow row : allRows) {
                MessageDTO messageDTO = new MessageDTO();
                JsonObject json = row.value();
                String id = json.getString(getCouchBaseTemplate().SELECT_ID);
                JsonObject um = json.getObject(UserMessageEntity.TYPE);
                UserMessageEntity userMessage = getGson().fromJson(um.toString(), UserMessageEntity.class);
                userMessage.setId(id);
                MessageEntity message = getGson().fromJson(json.getObject(MessageEntity.TYPE).toString(), MessageEntity.class);
                message.setId(userMessage.getMessageId());
                messageDTO.setMessage(message);
                UserEntity sender = getGson().fromJson(json.getObject("sender").toString(), UserEntity.class);
                sender.setId(userMessage.getSenderId());
                messageDTO.setSender(sender);
                if(StringUtil.isNotEmpty(userMessage.getGroupId())){
                    GroupEntity group = getCouchBaseTemplate().findById(userMessage.getGroupId(), GroupEntity.class);
                    messageDTO.setGroup(group);
                }
                messageDTO.setLastReply(getMessageLastReply(message.getId(), userId, userMessage.getGroupId()));
                messageDTO.setStatus(userMessage.getStatus());
                messageDTOs.add(messageDTO);
            }
        }
        logger.info("processing complete");
        return messageDTOs;
    }

    public MessageDTO getMessageLastReply(String messageId, String userId, String groupId){
        List<MessageDTO> messageDTOs = getMessageReply(messageId, userId, groupId, 1);
        return null != messageDTOs && messageDTOs.size()>0? messageDTOs.get(0):null;
    }
    
    public List<MessageDTO> getMessageReply(String messageId, String userId, String groupId, int limit){
        Expression expression = Expression.i(UserMessageEntity.TYPE+"`.`typeKey").eq(Expression.s(UserMessageEntity.TYPE))
                .and(Expression.i(MessageEntity.TYPE+"`.`typeKey").eq(Expression.s(MessageEntity.TYPE)))
                .and(Expression.i("sender`.`typeKey").eq(Expression.s(UserEntity.TYPE)))
                .and(Expression.i(UserMessageEntity.TYPE+"`.`messageId").in(Expression.sub(Select.select(" RAW META(pMessage).id")
                        .from(Expression.i(Constant.BUCKET_NAME).as(Expression.i("pMessage")))
                        .where(Expression.i("pMessage`.`typeKey").eq(Expression.s(MessageEntity.TYPE))
                                .and(Expression.i("pMessage.parentId").isNotMissing()
                                        .and(Expression.i("pMessage.parentId").isNotNull())
                                        .and(Expression.i("pMessage.parentId").eq(Expression.s(messageId))))))))
                .and(Expression.par(Expression.i(UserMessageEntity.TYPE+"`.`userId").eq(Expression.s(userId))
                        .or(Expression.i(UserMessageEntity.TYPE+"`.`senderId").eq(Expression.s(userId)))));
        if(StringUtil.isNotEmpty(groupId))
           expression =  expression.and(Expression.i(UserMessageEntity.TYPE+"`.`groupId").isNotMissing()
                   .and(Expression.i(UserMessageEntity.TYPE+"`.`groupId").isNotNull())
                   .and(Expression.i(UserMessageEntity.TYPE+"`.`groupId").eq(Expression.s(groupId))));

        Statement statement = Select.select(UserMessageEntity.TYPE + ", " + MessageEntity.TYPE +
                ", sender,  META(" + UserMessageEntity.TYPE + ").id as _ID, META(" + UserMessageEntity.TYPE + ").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(UserMessageEntity.TYPE)))
                .join(Expression.i(Constant.BUCKET_NAME).as(MessageEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`messageId"))
                .join(Expression.i(Constant.BUCKET_NAME).as("sender"))
                .onKeys(Expression.i(UserMessageEntity.TYPE + "`.`senderId"))
                .where(expression)
                .orderBy(Sort.desc(Expression.i(UserMessageEntity.TYPE+"`.`updatedAt")))
                .limit(limit);
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        N1qlQueryResult queryResult = getCouchBaseTemplate().queryN1QL(query);
        List<MessageDTO> messageDTOs = new ArrayList<MessageDTO>();
        if (queryResult.finalSuccess()) {
            List<N1qlQueryRow> allRows = queryResult.allRows();
            for (N1qlQueryRow row : allRows) {
                MessageDTO messageDTO = new MessageDTO();
                JsonObject json = row.value();
                String id = json.getString(getCouchBaseTemplate().SELECT_ID);
                JsonObject um = json.getObject(UserMessageEntity.TYPE);
                UserMessageEntity userMessage = getGson().fromJson(um.toString(), UserMessageEntity.class);
                userMessage.setId(id);
                MessageEntity message = getGson().fromJson(json.getObject(MessageEntity.TYPE).toString(), MessageEntity.class);
                message.setId(userMessage.getMessageId());
                messageDTO.setMessage(message);
                UserEntity sender = getGson().fromJson(json.getObject("sender").toString(), UserEntity.class);
                sender.setId(userMessage.getSenderId());
                messageDTO.setSender(sender);
                if(StringUtil.isNotEmpty(userMessage.getGroupId())){
                    GroupEntity group = getCouchBaseTemplate().findById(userMessage.getGroupId(), GroupEntity.class);
                    messageDTO.setGroup(group);
                }
                messageDTO.setStatus(userMessage.getStatus());
                messageDTOs.add(messageDTO);
            }
        }
        return messageDTOs;
    }
    
    private Long getTimeBefore(int days){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTimeInMillis();
    }
    
    
    public static void main(String args[]){
        MessageRepositoryImpl messageRepository = new MessageRepositoryImpl(null);
        //messageRepository.getUserMessage("FUser:148513",messageRepository.getTimeBefore(30), MessageStatus.A);
        messageRepository.getMessageLastReply("Message:3", "FUser:148513", "Group:0");
    }
}
