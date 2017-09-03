package com.forealert.intf.bean;


import com.forealert.intf.entity.Location;

public class GeoPoint {

	private Double latitude;
	private Double longitude;
	private Double minLatitude;
	private Double maxLatitude;
	private Double minLongitude;
	private Double maxLongitude;
	private Double radius;
	private Double altitude;
    private String city;
	
	public GeoPoint(Location location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		this.radius = location.getRadius();
        this.altitude = location.getAltitude();
        this.city = location.getCity();
		calculateGeoPoints();
	}
	
	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
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

	public Double getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(Double minLatitude) {
		this.minLatitude = minLatitude;
	}

	public Double getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(Double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public Double getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(Double minLongitude) {
		this.minLongitude = minLongitude;
	}

	public Double getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(Double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		if(radius != null && radius > 0) {
			this.radius = radius;
		}
	}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void calculateGeoPoints() {
		if(latitude != null && longitude != null) {
			double radiusInKm = radius * 1.60934;
			double kmInLongitudeDegree = 111.320 * Math.cos( latitude / 180.0 * Math.PI);
			double deltaLat = radiusInKm / 111.1;
			double deltaLong = radiusInKm / kmInLongitudeDegree;
			minLatitude = latitude - deltaLat;  
			maxLatitude = latitude + deltaLat;
			minLongitude = longitude - deltaLong; 
			maxLongitude = longitude + deltaLong;
            swap();
		}
	}
    
    private void swap(){
        if(minLongitude> maxLongitude){
            Double tempLong = this.minLongitude;
            this.minLongitude = this.maxLongitude;
            this.maxLongitude = tempLong;
        }
        if(this.minLatitude > this.maxLatitude){
            Double tempLat = this.minLatitude;
            this.minLatitude = this.maxLatitude;
            this.maxLatitude = tempLat;
        }
    }
	
}
