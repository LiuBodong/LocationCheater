package org.codebase.locationcheater.ui.dao;

public class WifiDto {

    private String ssid;

    private String mac;

    public WifiDto() {
    }

    public WifiDto(String ssid, String mac) {
        this.ssid = ssid;
        this.mac = mac;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
