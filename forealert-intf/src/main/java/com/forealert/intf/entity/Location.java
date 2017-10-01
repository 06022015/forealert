package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/26/17
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "location")
public class Location {

    @Field
    private String city = "Bangalore";

    @Field
    private Double latitude;
    @Field
    private Double longitude;
    @Field
    private Double altitude;
    @Field
    private Double radius;
    @Field
    private Double warningRadius;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getWarningRadius() {
        return warningRadius;
    }

    public void setWarningRadius(Double warningRadius) {
        this.warningRadius = warningRadius;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", radius=" + radius +
                ", warningRadius=" + warningRadius +
                '}';
    }
}
