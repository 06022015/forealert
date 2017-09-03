package com.forealert.web.bean;

import com.forealert.intf.api.repository.GroupRepository;
import com.forealert.intf.api.repository.MessageRepository;
import com.forealert.intf.api.repository.UserRepository;
import com.forealert.intf.bean.ForeAlertBean;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForeAlertBeanImpl implements ForeAlertBean {

    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private GroupRepository groupRepository;
    private Properties properties;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public MessageRepository getMessageRepository() {
        return this.messageRepository;
    }

    @Override
    public UserRepository getUserRepository() {
        return this.userRepository;
    }

    @Override
    public GroupRepository getGroupRepository() {
        return this.groupRepository;
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }
}
