package com.forealert.intf.bean;

import com.forealert.intf.api.core.MessageBL;
import com.forealert.intf.api.core.UserBL;
import com.forealert.intf.api.repository.GroupRepository;
import com.forealert.intf.api.repository.MessageRepository;
import com.forealert.intf.api.repository.UserRepository;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 8:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ForeAlertBean {

    MessageRepository getMessageRepository();

    UserRepository getUserRepository();

    GroupRepository getGroupRepository();

    Properties getProperties();

}
