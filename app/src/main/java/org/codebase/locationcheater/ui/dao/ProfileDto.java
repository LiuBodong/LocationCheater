package org.codebase.locationcheater.ui.dao;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "profile")
public class ProfileDto {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private @NonNull String profileName = UUID.randomUUID().toString();

    @ColumnInfo(name = "wifi_enabled")
    private boolean wifiEnabled;

    @ColumnInfo(name = "connected_wifi")
    private WifiDto connectedWifi;

    @ColumnInfo(name = "scan_results")
    private List<WifiDto> scanResults = Collections.emptyList();

    @ColumnInfo(name = "telephones")
    private List<TelephoneDto> telephones = Collections.emptyList();

    @ColumnInfo(name = "location")
    private LocationDto location;

    @ColumnInfo(name = "create_time")
    private long createTime = System.currentTimeMillis();

    @Ignore
    public ProfileDto(@NonNull String profileName, boolean wifiEnabled, WifiDto connectedWifi, List<WifiDto> scanResults, List<TelephoneDto> telephones, LocationDto location) {
        this.profileName = profileName;
        this.wifiEnabled = wifiEnabled;
        this.connectedWifi = connectedWifi;
        this.scanResults = scanResults;
        this.telephones = telephones;
        this.location = location;
    }

    public ProfileDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public WifiDto getConnectedWifi() {
        return connectedWifi;
    }

    public void setConnectedWifi(WifiDto connectedWifi) {
        this.connectedWifi = connectedWifi;
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
