package com.forealert.repository.impl;

import com.couchbase.client.java.query.Select;
import com.couchbase.client.java.query.SimpleN1qlQuery;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.dsl.Expression;
import com.forealert.intf.Constant;
import com.forealert.intf.api.repository.GroupRepository;
import com.forealert.intf.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

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
                .onKeys(Expression.i(GroupAttachmentEntity.TYPE+".groupId"))
                .where(Expression.i(GroupAttachmentEntity.TYPE + ".groupId").eq(groupId));
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
                .onKeys(Expression.i(EmojiEntity.TYPE+".groupId"))
                .where(Expression.i(EmojiEntity.TYPE + ".groupId").eq(groupId));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        return getCouchBaseTemplate().findByN1QL(query, EmojiEntity.class);
    }

    @Override
    public GroupMemberEntity getGroupMember(String groupId, String userId) {
        Statement statement = Select.select(GroupMemberEntity.TYPE + ".*, META("+GroupMemberEntity.TYPE+").id as _ID, META("+GroupMemberEntity.TYPE+").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                        .as(Expression.i(GroupMemberEntity.TYPE)))
                .where(Expression.i(GroupMemberEntity.TYPE + ".groupId").eq(groupId)
                .and(Expression.i(GroupMemberEntity.TYPE+".userId").eq(userId)));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        List groupMembers = getCouchBaseTemplate().findByN1QL(query, GroupMemberEntity.class);
        return null != groupMembers && groupMembers.size()>0? (GroupMemberEntity)groupMembers.get(0):null;
    }
}
