package com.forealert.repository.impl;

import com.couchbase.client.java.repository.annotation.Field;
import com.forealert.intf.api.repository.UserRepository;
import com.forealert.intf.bean.GeoPoint;
import com.forealert.intf.entity.*;
import com.forealert.intf.entity.type.AppEnum;
import com.forealert.intf.entity.type.DeviceType;
import com.forealert.intf.entity.type.PrivilegeType;
import com.forealert.intf.entity.type.Role;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.lang.IgniteClosure;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.cache.Cache;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 9/23/17
 * Time: 9:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class IgniteUserRepositoryImpl /*implements UserRepository */ {

    private Ignite ignite;
    private IgniteCache<String, UserEntity> userCache;

    protected IgniteUserRepositoryImpl() {
        this.ignite = Ignition.start("examples/config/example-ignite.xml");
        userCache = ignite.cache("DB_FOREALERT_USER");
    }

    /*@Override
    public UserEntity getUserById(String id) {
        return null;
    }

    @Override
    public List<UserEntity> findUserByIds(List<String> userIds) {
        return null;
    }

    @Override
    public UserEntity findUserByUUId(String UUId) {
        return null;
    }

    @Override
    public void delete(UserEntity user) {
    }

    @Override
    public List<UserEntity> findNearByUser(GeoPoint geoPoint, Role... role) {
        return null;
    }

    @Override
    public List<GroupMemberEntity> findGroupAdmin(List<String> groupIds) {
        return null;
    }

    @Override
    public List<GroupMemberEntity> findUerGroup(String userId) {
        return null;
    }

    @Override
    public void save(Base entity) {
        userCache.put(entity.getId(), (UserEntity) entity);
    }

    @Override
    public void update(Base entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void remove(Base entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }*/


    public static void createLocationCache(Ignite ignite, int count) {
        CacheConfiguration cfg = new CacheConfiguration();
        cfg.setName("DB_FOREALERT_LOCATION");
        cfg.setCacheMode(CacheMode.PARTITIONED);
        cfg.setIndexedTypes(String.class, Location.class, Double.class, Double.class);
        IgniteCache<String, Location> locationCache = ignite.getOrCreateCache(cfg);
        for(int i=1;i< count; i++){
            locationCache.put(i+"", getLocation(i));
        }

    }

    public static void createUserCache(Ignite ignite, int count) {
        CacheConfiguration cfg = new CacheConfiguration();
        cfg.setName("DB_FOREALERT_USER");
        cfg.setCacheMode(CacheMode.PARTITIONED);
        cfg.setIndexedTypes(String.class, UserEntity.class);
        IgniteCache<String, UserEntity> userCache = ignite.getOrCreateCache(cfg);
        for(int i=0;i< count; i++){
            userCache.put(i+"", getUser(i));
        }
        /*
       userCache.put("2", getUser("2"));
       userCache.put("3", getUser("3"));
       userCache.put("4", getUser("4"));
       userCache.put("5", getUser("5"));
       userCache.put("6", getUser("6"));
       userCache.put("7", getUser("7"));
       userCache.put("8", getUser("8"));
       userCache.put("9", getUser("9"));
       userCache.put("10", getUser("10"));*/
    }

    public static void createMessageCache(Ignite ignite) {
        CacheConfiguration cfg = new CacheConfiguration();
        cfg.setName("DB_FOREALERT_MESSAGE");
        //cfg.setIndexedTypes(String.class,UserEntity.class);
        IgniteCache<String, MessageEntity> messageCache = ignite.getOrCreateCache("DB_FOREALERT_MESSAGE");
        messageCache.put("1", getMessage("1", null));
        messageCache.put("2", getMessage("2", "1"));
        messageCache.put("3", getMessage("3", null));
        messageCache.put("4", getMessage("4", null));
        messageCache.put("5", getMessage("5", "3"));
        messageCache.put("6", getMessage("6", "3"));
        messageCache.put("7", getMessage("7", "3"));
        messageCache.put("8", getMessage("8", null));
        messageCache.put("9", getMessage("9", "8"));
        messageCache.put("10", getMessage("10", null));
    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Ignite ignite = Ignition.start("/Users/ashqures/config/apacheignite/apache-ignite-fabric-2.2.0-bin/examples/config/example-ignite.xml");
        //createUserCache(ignite, 1000);
        //createMessageCache(ignite);
        createLocationCache(ignite, 100000);
        //IgniteCache<String, UserEntity> userCache = ignite.getOrCreateCache("DB_FOREALERT_USER");
        IgniteCache<String, UserEntity> userCache = ignite.getOrCreateCache("DB_FOREALERT_LOCATION");
        /*for (int i = 1; i <= 10; i++)
            System.out.println("Got [key=" + i + ", val=" + userCache.get(i + "") + ']');*/

        /*IgniteBiPredicate<String, UserEntity> filter = new IgniteBiPredicate<String, UserEntity>() {
            @Override
            public boolean apply(String key, UserEntity p) {
                System.out.print("Key:- "+ key + " Entity:- "+ p.toString());
                //return p.getId().equals("100");
                //return p.getName().equals("Ashif Qureshi12");
                return p.getLocation().getLatitude()>27 && p.getLocation().getLatitude()< 28 && p.getLocation().getLongitude()>38 && p.getLocation().getLongitude()<39;
            }
        };*/
        System.out.println(Calendar.getInstance().getTimeInMillis());
        //SqlQuery sql = new SqlQuery(Location.class, "latitude > 27");
        //TextQuery textQuery = new TextQuery(UserEntity.class, "Ashif Qureshi");
        List<Cache.Entry<String, Location>> cursor = userCache.query(new ScanQuery<String, Location>(new IgniteBiPredicate<String, Location>() {
            @Override
            public boolean apply(String key, Location p) {
                //System.out.print("Key:- "+ key + " Entity:- "+ p.toString());
               // return p.getId().equals("100");
               // return p.getName().equals("Ashif Qureshi12");
                return p.getLatitude()>27D && p.getLatitude()< 29D && p.getLongitude()>37D && p.getLongitude()<40D;
            }
        })).getAll();
        //List<Cache.Entry<String, Location>> cursor = userCache.query(sql).getAll();
        System.out.println(Calendar.getInstance().getTimeInMillis()+"Result size:- "+cursor.size());
        //for (Cache.Entry<String, Location> e : cursor)
           // System.out.println(e.getValue().toString());

        /*Class.forName("org.apache.ignite.IgniteJdbcDriver");
        Connection conn = DriverManager.getConnection("jdbc:ignite://localhost:11211/");
        ResultSet rs = conn.createStatement().executeQuery("select * from DB_FOREALERT_USER");
        while (rs.next()) {
            UserEntity user = (UserEntity)rs;
            System.out.print(user.toString());
        }*/
        /*PreparedStatement stmt = conn.prepareStatement("select name, age from Person where age = ?");
        stmt.setInt(1, 30);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            int age = rs.getInt("age");
        }*/
    }

    public static UserEntity getUser(Integer id) {
        UserEntity user = new UserEntity();
        user.setId(id+"");
        user.setApp(AppEnum.EMG_APP);
        user.setEmail("ashifqureshi15@gmail.com");
        user.setMobile("9886387129");
        user.setUuId("GFCM0:eVnkgjkTKQg:APA91bGBQW0gDSbHTgrtt-j_NbREi7PMpxBh2memqdCRYLovbCGSIZ0z-j9L_ru81TPPSVAf0BeFq-WWG4b5Q4H0agPaJAqnTHyAvmhlOdqKpt8h7zdrnMZ4NLmp_H0ZqfjHfaogb8vP");
        user.setName("Ashif Qureshi"+ id);
        Location location = new Location();
        location.setLatitude(27.8D+id/1000);
        location.setLongitude(38.4D+ id/1000);
        location.setAltitude(4D);
        user.setLocation(location);
        return user;
    }

    public static Location getLocation(Integer id) {
        Location location = new Location();
        location.setLatitude(27.8D*id/1000);
        location.setLongitude(38.4D*id/1000);
        location.setAltitude(4D);
        return location;
    }

    public static MessageEntity getMessage(String id, String parentId) {
        MessageEntity message = new MessageEntity();
        message.setId(id);
        message.setParentId(parentId);
        message.setSenderId("FUser:1");
        message.setSenderUUId("GFCM0:eVnkgjkTKQg:APA91bGBQW0gDSbHTgrtt-j_NbREi7PMpxBh2memqdCRYLovbCGSIZ0z-j9L_ru81TPPSVAf0BeFq-WWG4b5Q4H0agPaJAqnTHyAvmhlOdqKpt8h7zdrnMZ4NLmp_H0ZqfjHfaogb8vP");
        message.setMessage("Fore Alert Test");
        Location location = new Location();
        location.setLatitude(27.8D);
        location.setLongitude(38.4D);
        location.setAltitude(4D);
        message.setSenderLocation(location);
        return message;
    }
}
