package com.forealert.repository.impl;

import com.forealert.intf.api.repository.FARepository;
import com.forealert.intf.entity.Base;
import com.forealert.intf.util.StringUtil;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/3/17
 * Time: 7:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ForeAlterRepository implements FARepository{

    private CouchbaseTemplate couchBaseTemplate;

    protected ForeAlterRepository(CouchbaseTemplate couchBaseTemplate) {
        this.couchBaseTemplate = couchBaseTemplate;
    }

    public CouchbaseTemplate getCouchBaseTemplate() {
        return couchBaseTemplate;
    }
    
    protected int getNextInt(String type){
        return getCouchBaseTemplate().getCouchbaseBucket().counter(type, 1,0).content().intValue();
    }

    public void save(Base entity){
        if(null != entity && StringUtil.isBlank(entity.getId()))
            entity.setId(entity.getTypeKey()+":"+getNextInt(entity.getTypeKey()));
        getCouchBaseTemplate().save(entity);
    }

    public void update(Base entity){
        getCouchBaseTemplate().save(entity);
    }

    public void remove(Base entity){
        getCouchBaseTemplate().remove(entity);
    }
}
