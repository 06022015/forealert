package com.forealert.repository.impl;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.dsl.Expression;
import com.forealert.intf.Constant;
import com.forealert.intf.api.repository.GroupRepository;
import com.forealert.intf.dto.MessageDTO;
import com.forealert.intf.entity.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/20/17
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupRepositoryImpl extends ForeAlterRepository implements GroupRepository{

    private Logger logger = LoggerFactory.getLogger(GroupRepositoryImpl.class);

    protected GroupRepositoryImpl(CouchbaseTemplate couchBaseTemplate) {
        super(couchBaseTemplate);
    }

    @Override
    public List<GroupAttachmentEntity> getGroupAttachment(String groupId) {
        Statement statement = Select.select(GroupAttachmentEntity.TYPE + ".*, META("+GroupAttachmentEntity.TYPE+").id as _ID, META("+GroupAttachmentEntity.TYPE+").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(GroupAttachmentEntity.TYPE)))
                .join(Expression.i(Constant.BUCKET_NAME).as(GroupEntity.TYPE))
                .onKeys(Expression.i(GroupAttachmentEntity.TYPE+"`.`groupId"))
                .where(Expression.x("typeKey").eq(Expression.s(GroupAttachmentEntity.TYPE))
                        .and(Expression.i(GroupAttachmentEntity.TYPE + "`.`groupId").eq(Expression.s(groupId))));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        return getCouchBaseTemplate().findByN1QL(query, GroupAttachmentEntity.class);
    }

    @Override
    public GroupAttachmentEntity getAttachmentById(String groupId, String attachmentId) {
        GroupAttachmentEntity groupAttachment = getCouchBaseTemplate().findById(attachmentId, GroupAttachmentEntity.class);
        if(null != groupAttachment && !groupAttachment.getGroupId().equals(groupId))
            groupAttachment = null;
        return groupAttachment;
    }

    @Override
    public GroupEntity getById(String groupId) {
        return getCouchBaseTemplate().findById(groupId, GroupEntity.class);
    }

    @Override
    public List<EmojiEntity> getEmojis(String groupId) {
        Statement statement = Select.select(EmojiEntity.TYPE + ".*, META("+EmojiEntity.TYPE+").id as _ID, META("+EmojiEntity.TYPE+").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(EmojiEntity.TYPE)))
                .join(Expression.i(Constant.BUCKET_NAME).as(GroupEntity.TYPE))
                .onKeys(Expression.i(EmojiEntity.TYPE+"`.`groupId"))
                .where(Expression.x("typeKey").eq(Expression.s(EmojiEntity.TYPE))
                        .and(Expression.i(EmojiEntity.TYPE + "`.`groupId").eq(Expression.s(groupId))));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        return getCouchBaseTemplate().findByN1QL(query, EmojiEntity.class);
    }

    @Override
    public GroupMemberEntity getGroupMember(String groupId, String userId) {
        Statement statement = Select.select(GroupMemberEntity.TYPE + ", META("+GroupMemberEntity.TYPE+").id as _ID, META("+GroupMemberEntity.TYPE+").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(GroupMemberEntity.TYPE)))
                .where(Expression.x("typeKey").eq(Expression.s(GroupMemberEntity.TYPE))
                        .and(Expression.i(GroupMemberEntity.TYPE + "`.`groupId").eq(Expression.s(groupId)))
                .and(Expression.i(GroupMemberEntity.TYPE+"`.`userId").eq(Expression.s(userId))));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        List groupMembers = getCouchBaseTemplate().findByN1QL(query, GroupMemberEntity.class);
        return null != groupMembers && groupMembers.size()>0? (GroupMemberEntity)groupMembers.get(0):null;
    }

    public List<GroupMemberEntity> getGroupMemberDetail(String groupId) {
        Statement statement = Select.select(GroupMemberEntity.TYPE + ", "+UserEntity.TYPE+",  META("+GroupMemberEntity.TYPE+").id as _ID, META("+GroupMemberEntity.TYPE+").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(GroupMemberEntity.TYPE)))
                .join(Expression.i(Constant.BUCKET_NAME).as(UserEntity.TYPE))
                .onKeys(Expression.i(UserMessageEntity.TYPE+"`.`userId"))
                .where(Expression.x("typeKey").eq(Expression.s(GroupMemberEntity.TYPE))
                        .and(Expression.i(GroupMemberEntity.TYPE + "`.`groupId").eq(Expression.s(groupId))));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        N1qlQueryResult queryResult = getCouchBaseTemplate().queryN1QL(query);
        List<GroupMemberEntity> groupMembers = new ArrayList<GroupMemberEntity>();
        if (queryResult.finalSuccess()) {
            List<N1qlQueryRow> allRows = queryResult.allRows();
            Gson gson = new Gson();
            for (N1qlQueryRow row : allRows) {
                JsonObject json = row.value();
                String id = json.getString(getCouchBaseTemplate().SELECT_ID);
                JsonObject gm = json.getObject(GroupMemberEntity.TYPE);
                GroupMemberEntity groupMember = gson.fromJson(gm.toString(), GroupMemberEntity.class);
                groupMember.setId(id);
                UserEntity user = gson.fromJson(json.getObject(UserEntity.TYPE).toString(),UserEntity.class);
                user.setId(groupMember.getUserId());
                groupMember.setUser(user);
                groupMembers.add(groupMember);
            }
        }
        return groupMembers;
    }
}
