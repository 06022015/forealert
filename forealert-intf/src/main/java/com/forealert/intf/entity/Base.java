package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forealert.intf.Constant;
import org.springframework.data.annotation.Version;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/1/17
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Base {

    @Id
    private String id;

    @Field
    private Date createdAt = new Date();
    @Field
    private Date updatedAt = new Date();

    @Field
    private String typeKey = "Base";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        /*if(null == createdAt)
            createdAt = new Date();
        Constant.dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.createdAt = Constant.dateFormatter.format(createdAt);*/
        this.updatedAt = updatedAt;
    }

    public abstract String getTypeKey();

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }
}
