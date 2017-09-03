package com.forealert.intf.api.repository;

import com.forealert.intf.bean.GeoPoint;
import com.forealert.intf.entity.GroupEntity;
import com.forealert.intf.entity.GroupMemberEntity;
import com.forealert.intf.entity.UserEntity;
import com.forealert.intf.entity.type.Role;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserRepository extends FARepository {


    UserEntity getUserById(String id);

    List<UserEntity> findUserByIds(List<String> userIds);

    UserEntity findUserByUUId(String UUId);

    void delete(UserEntity user);

    List<UserEntity>  findNearByUser(GeoPoint geoPoint, Role... role);
    
    List<GroupMemberEntity>  findGroupAdmin(List<String> groupIds);
    
    List<GroupMemberEntity>  findUerGroup(String userId);

}
