package com.forealert.intf.api.core;

import com.forealert.intf.dto.UserPermissionDTO;
import com.forealert.intf.entity.GroupEntity;
import com.forealert.intf.entity.Location;
import com.forealert.intf.entity.UserEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserBL {

    void save(UserEntity user);

    

    void updatePosition(Location location, String userId);

    UserEntity getUserById(String id);

    void deleteUser(String id);

    void update(UserEntity user, String userId);

    void logout(String uuid, String googleId);

    void adminApprove(String userId, UserPermissionDTO userPermissionDTO);

    void adminUnApprove(String userId);

    List<UserEntity>  getNearByUser(Location location);

}
