package com.forealert.repository.impl;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.query.Select;
import com.couchbase.client.java.query.SimpleN1qlQuery;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.dsl.Expression;
import com.forealert.intf.Constant;
import com.forealert.intf.api.repository.UserRepository;
import com.forealert.intf.bean.GeoPoint;
import com.forealert.intf.entity.GroupEntity;
import com.forealert.intf.entity.GroupMemberEntity;
import com.forealert.intf.entity.UserEntity;
import com.forealert.intf.entity.type.Role;
import org.apache.ignite.springdata.repository.support.IgniteRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserRepositoryImpl extends ForeAlterRepository implements UserRepository {

    private Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    public UserRepositoryImpl(CouchbaseTemplate couchbaseTemplate) {
        super(couchbaseTemplate);
    }

    public UserEntity getUserById(String id) {
        return getCouchBaseTemplate().findById(id, UserEntity.class);
    }

    @Override
    public List<UserEntity> findUserByIds(List<String> userIds) {
        Statement statement = Select.select(UserEntity.TYPE+".*, META("+UserEntity.TYPE+").id as _ID, META("+UserEntity.TYPE+").cas as _CAS")
                .from(Expression.i(Constant.BUCKET_NAME)
                .as(Expression.i(UserEntity.TYPE))).where(Expression.x("id").in(JsonArray.from(userIds)));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        return getCouchBaseTemplate().findByN1QL(query, UserEntity.class);
    }

    @Override
    public UserEntity findUserByUUId(String UUId) {
        Statement statement = Select.select(UserEntity.TYPE+".*, META("+UserEntity.TYPE+").id as _ID, META("+UserEntity.TYPE+").cas as _CAS").from(Expression.i(Constant.BUCKET_NAME)
                .as(Expression.i(UserEntity.TYPE)))
                .where(Expression.x("uuId").eq(Expression.s(UUId))
                        .and(Expression.x("typeKey").eq(Expression.s(UserEntity.TYPE))));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        List<UserEntity> users = getCouchBaseTemplate().findByN1QL(query, UserEntity.class);
        return null != users && users.size()>0?users.get(0):null;
    }

    public void delete(UserEntity user) {
        getCouchBaseTemplate().remove(user);
    }



    @Override
    public List<UserEntity> findNearByUser(GeoPoint geoPoint, Role... roles) {
        Statement statement = Select.select(UserEntity.TYPE+".*, META("+UserEntity.TYPE+").id as _ID, META("+UserEntity.TYPE+").cas as _CAS").from(Expression.i(Constant.BUCKET_NAME)
                .as(Expression.i(UserEntity.TYPE)))
                .where(Expression.x("typeKey").eq(Expression.s(UserEntity.TYPE))
                        .and(Expression.i("location`.`city").eq(Expression.s(geoPoint.getCity())))
                        .and(Expression.i("location`.`latitude")
                                .between(Expression.x(geoPoint.getMinLatitude()).and(Expression.x(geoPoint.getMaxLatitude()))))
                        .and(Expression.i("location`.`longitude").between(Expression.x(geoPoint.getMinLongitude()).and(Expression.x(geoPoint.getMaxLongitude()))))
                        .and(Expression.i("role").in(JsonArray.from(objectAsList(roles))))
                        .and(Expression.i("loggedOut").eq(false)).and(Expression.i("trackingOff").eq(false)));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        return getCouchBaseTemplate().findByN1QL(query, UserEntity.class);
    }

    @Override
    public List<GroupMemberEntity> findGroupAdmin(List<String> groupIds) {
        Statement statement = Select.select(GroupMemberEntity.TYPE+".*, META("+GroupMemberEntity.TYPE+").id as _ID, META("+GroupMemberEntity.TYPE+").cas as _CAS").from(Expression.i(Constant.BUCKET_NAME)
                .as(Expression.i(GroupMemberEntity.TYPE)))
                .join(Expression.i(Constant.BUCKET_NAME).as(UserEntity.TYPE))
                .onKeys(Expression.i(GroupMemberEntity.TYPE+".userId"))
                .where(Expression.i(GroupMemberEntity.TYPE+".admin").eq(true)
                        .and(Expression.i(UserEntity.TYPE+".trackingOff").eq(false))
                        .and(Expression.i(UserEntity.TYPE+".loggedOut").eq(false))
                        .and(Expression.i(GroupMemberEntity.TYPE+".groupId").in(JsonArray.from(groupIds))));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        return getCouchBaseTemplate().findByN1QL(query, GroupMemberEntity.class);
    }

    @Override
    public List<GroupMemberEntity> findUerGroup(String userId) {
        Statement statement = Select.select(GroupMemberEntity.TYPE+".*, META("+GroupMemberEntity.TYPE+").id as _ID, META("+GroupMemberEntity.TYPE+").cas as _CAS").from(Expression.i(Constant.BUCKET_NAME)
                .as(Expression.i(GroupMemberEntity.TYPE)))
                .join(Expression.i(Constant.BUCKET_NAME).as(UserEntity.TYPE))
                .onKeys(Expression.i(GroupMemberEntity.TYPE+".userId"))
                .join(Expression.i(Constant.BUCKET_NAME).as(GroupEntity.TYPE))
                .onKeys(Expression.i(GroupMemberEntity.TYPE+".groupId"))
                .where(Expression.i(GroupMemberEntity.TYPE + ".userId").eq(Expression.s(userId)));
        SimpleN1qlQuery query =  SimpleN1qlQuery.simple(statement.toString());
        return getCouchBaseTemplate().findByN1QL(query, GroupMemberEntity.class);
    }
    
    public static void main(String args[]){
        UserRepositoryImpl userRepository = new UserRepositoryImpl(null);
        List<String> ids = new ArrayList<String>();
        ids.add("FUser:0");
        userRepository.findUserByIds(ids);
        IgniteRepositoryImpl i = null;

    }
}