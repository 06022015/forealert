package com.forealert.repository.impl;

import com.forealert.intf.api.repository.FARepository;
import com.forealert.intf.entity.Base;
import com.forealert.intf.util.DateTimeConverter;
import com.forealert.intf.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/3/17
 * Time: 7:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ForeAlterRepository implements FARepository{

    private CouchbaseTemplate couchBaseTemplate;
    private Gson gson;

    protected ForeAlterRepository(CouchbaseTemplate couchBaseTemplate) {
        this.couchBaseTemplate = couchBaseTemplate;
        this.gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTimeConverter()).create();
    }

    public CouchbaseTemplate getCouchBaseTemplate() {
        return couchBaseTemplate;
    }

    public Gson getGson() {
        return gson;
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

    protected List<String> objectAsList(Object... objs){
        List<String> results = new ArrayList<String>();
        for(Object object : objs)
            results.add(object.toString());
        return results;
    }


}
