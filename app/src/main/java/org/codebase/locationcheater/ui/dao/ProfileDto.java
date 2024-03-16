package org.codebase.locationcheater.ui.dao;

import java.util.Collections;
import java.util.List;

public class ProfileDto {

    private String profileName;

    private boolean wifiEnabled;

    private WifiDto currentWifi;

    private List<WifiDto> scanResults = Collections.emptyList();

    private List<TelephoneDto> telephones = Collections.emptyList();

    private LocationDto location;

    private long createTime = System.currentTimeMillis();

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public boolean isWifiEnabled() {
        return wifiEnabled;
    }

    public void setWifiEnabled(boolean wifiEnabled) {
        this.wifiEnabled = wifiEnabled;
    }

    public WifiDto getCurrentWifi() {
        return currentWifi;
    }

    public void setCurrentWifi(WifiDto currentWifi) {
        this.currentWifi = currentWifi;
    }

    public List<WifiDto> getScanResults() {
        return scanResults;
    }

    public void setScanResults(List<WifiDto> scanResults) {
        this.scanResults = scanResults;
    }

    public List<TelephoneDto> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<TelephoneDto> telephones) {
        this.telephones = telephones;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
