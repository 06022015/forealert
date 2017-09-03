package com.forealert.web.util;

import com.forealert.intf.entity.Location;
import org.springframework.data.couchbase.core.CouchbaseTemplate;


/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/24/17
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationMode {


    private CouchbaseTemplate couchbaseTemplate;


    /*@PostConstruct*/
    public void create(){
             Location location = new Location();
       // location.setId(1221L);
        location.setLatitude(3244.2344);
        location.setLongitude(3244.2344);
        location.setAltitude(34.2344);
        location.setRadius(20.4);
        couchbaseTemplate.insert(location);
        //location.setId(345L);
       // EntityDocument<Location> locDoc = EntityDocument.create("entity_doc_1",location);
        //couchbaseBucket.insert(locDoc);

/*
        JsonObject object = JsonObject.create().put("latitude",23432.343)
                .put("longitude",2342.4243)
                .put("radius", 24.4)
                .put("altitude",234234.243) ;
        JsonDocument document = JsonDocument.create("user_location",object);

            this.bucket.insert(document);
*/
    }


}
