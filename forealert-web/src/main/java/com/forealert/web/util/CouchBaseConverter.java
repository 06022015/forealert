package com.forealert.web.util;

import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.mapping.CouchbaseDocument;
import org.springframework.data.couchbase.core.mapping.CouchbaseMappingContext;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentEntity;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.util.TypeInformation;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/2/17
 * Time: 8:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class CouchBaseConverter extends MappingCouchbaseConverter {


    public CouchBaseConverter(final MappingContext<? extends CouchbasePersistentEntity<?>, CouchbasePersistentProperty> mappingContext) {
        super(mappingContext, "typeKey");
    }

    @Override
    protected <R> R read(final TypeInformation<R> type, final CouchbaseDocument source, final Object parent) {
        if (Object.class == typeMapper.readType(source, type).getType()) {
            return null;
        }
        return super.read(type, source, parent);
    }
}
