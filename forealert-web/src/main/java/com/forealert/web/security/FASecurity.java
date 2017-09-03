package com.forealert.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class FASecurity {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.expire.hours}")
    private Integer expiryHour;

    private String encodedKey;

    @PostConstruct
    protected void init(){
        encodeKey();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEncodedKey() {
        return encodedKey;
    }

    public Integer getExpiryHour() {
        return this.expiryHour;
    }

    private void encodeKey(){
        this.encodedKey =  Base64.decodeAsString(getSecretKey());
    }

    public Date getExpiry(){
        Date now = new Date();
        Long expireInMilis = TimeUnit.HOURS.toMillis(getExpiryHour());
        return new Date(expireInMilis + now.getTime());
    }

    public String getToken(String mobile, String role){
        Date now = new Date();
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(mobile)
                .claim("role",role)
                .setIssuedAt(now)
                .setExpiration(getExpiry())
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();
    }
}
