package com.forealert.repository.impl;

import com.forealert.core.util.ForeAlertS3Client;
import com.forealert.intf.entity.UserEntity;
import org.apache.ignite.cache.store.CacheStore;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.jetbrains.annotations.Nullable;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 9/28/17
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class IgnitePersistentStore implements CacheStore<Long, UserEntity> {

    private ForeAlertS3Client s3Client = ForeAlertS3Client.create("forealert-bucket","AKIAIM7VMLWT7X6UZLFA","2HBtUqpH7jBrVY+oI5pUhzUGxGVQt/uZyxrU8J8m","us-west-1");

    @Override
    public void loadCache(IgniteBiInClosure<Long, UserEntity> clo, @Nullable Object... args) throws CacheLoaderException {
        System.out.println(">> Loading cache from Dynamo DB...");


    }

    @Override
    public void sessionEnd(boolean commit) throws CacheWriterException {

    }

    @Override
    public UserEntity load(Long aLong) throws CacheLoaderException {
        return null;
    }

    @Override
    public Map<Long, UserEntity> loadAll(Iterable<? extends Long> longs) throws CacheLoaderException {
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends UserEntity> entry) throws CacheWriterException {

    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends UserEntity>> entries) throws CacheWriterException {

    }

    @Override
    public void delete(Object o) throws CacheWriterException {

    }

    @Override
    public void deleteAll(Collection<?> objects) throws CacheWriterException {

    }
}
