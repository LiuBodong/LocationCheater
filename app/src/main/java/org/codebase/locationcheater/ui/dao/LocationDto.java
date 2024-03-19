package org.codebase.locationcheater.ui.dao;

public class LocationDto {

    private float latitude;

    private float longitude;

    public LocationDto(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationDto() {
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
