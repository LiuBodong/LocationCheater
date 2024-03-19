package org.codebase.locationcheater.ui.dao;

public class TelephoneDto {

    private String type;

    private int lac;

    private int cid;

    public TelephoneDto(String type, int lac, int cid) {
        this.type = type;
        this.lac = lac;
        this.cid = cid;
    }

    public TelephoneDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
