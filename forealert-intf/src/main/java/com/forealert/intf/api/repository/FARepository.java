package com.forealert.intf.api.repository;

import com.forealert.intf.entity.Base;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/11/17
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FARepository {

    void save(Base entity);

    void update(Base entity);

    void remove(Base entity);
}
