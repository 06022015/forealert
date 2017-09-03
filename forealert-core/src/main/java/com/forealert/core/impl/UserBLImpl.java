package com.forealert.core.impl;

import com.forealert.intf.api.core.UserBL;
import com.forealert.intf.api.repository.UserRepository;
import com.forealert.intf.bean.ForeAlertBean;
import com.forealert.intf.bean.GeoPoint;
import com.forealert.intf.dto.UserPermissionDTO;
import com.forealert.intf.entity.GroupEntity;
import com.forealert.intf.entity.GroupMemberEntity;
import com.forealert.intf.entity.Location;
import com.forealert.intf.entity.UserEntity;
import com.forealert.intf.entity.type.PrivilegeType;
import com.forealert.intf.entity.type.Role;
import com.forealert.intf.exception.NoRecordFoundException;
import com.forealert.intf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserBLImpl implements UserBL {

    private static Logger logger = LoggerFactory.getLogger(UserBLImpl.class);

    private UserRepository userRepository;

    public UserBLImpl(ForeAlertBean bean) {
        this.userRepository = bean.getUserRepository();
    }

    public void save(UserEntity user) {
        //user.setId(UserEntity.TYPE+":"+user.getUuId());
         userRepository.save(user);
    }

    public void updatePosition(Location location, String userId) {
        UserEntity user = userRepository.getUserById(userId);
        if(null == user)
            throw new NoRecordFoundException("No user found for id:- "+ userId);
        user.setLocation(location);
        userRepository.save(user);
    }

    public UserEntity getUserById(String id) {
        UserEntity user = userRepository.getUserById(id);
        if (null == user)
            throw new NoRecordFoundException("No record found for message id:- " + id);
        return user;
    }

    public void deleteUser(String id) {

    }

    public void update(UserEntity user, String userId) {

    }

    @Override
    public void logout(String uuid, String googleId) {
        UserEntity user = userRepository.getUserById(uuid);
        if(null == user)
            throw new NoRecordFoundException("No user found for id:- "+ uuid);
        if(StringUtil.isNotBlank(user.getGoogleUserId()) && googleId.equalsIgnoreCase(user.getGoogleUserId())){
            user.setToken("");
            user.setGoogleUserId("");
            user.setLoggedOut(true);
            user.setUpdatedAt(new Date());
            userRepository.save(user);
        }
    }

    @Override
    public void adminApprove(String userId, UserPermissionDTO userPermissionDTO) {
        UserEntity user = userRepository.getUserById(userId);
        if(null == user)
            throw new NoRecordFoundException("No user found for user id:- "+ userId);
        user.setRole(userPermissionDTO.getRole());
        if(null != userPermissionDTO.getPrivileges() && userPermissionDTO.getPrivileges().size()>0){
            for(PrivilegeType privilegeType : userPermissionDTO.getPrivileges())
                user.addPrivilege(privilegeType);
        }
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    public void adminUnApprove(String userId) {
        UserEntity user = userRepository.getUserById(userId);
        if(null == user)
            throw new NoRecordFoundException("No user found for user id:- "+ userId);
        user.setRole(Role.USER);
        user.setPrivileges(null);
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    public List<UserEntity> getNearByUser(Location location) {
        Double radius = 5.0;
        List<UserEntity> nearByUsers;
        do {
            //location.setRadius(radius);
            GeoPoint geoPoint = new GeoPoint(location);
            nearByUsers = userRepository.findNearByUser(geoPoint, Role.USER);
            radius += 5;
        } while (nearByUsers.size() <= 5 && radius <= 25);
        return nearByUsers;
    }
}
