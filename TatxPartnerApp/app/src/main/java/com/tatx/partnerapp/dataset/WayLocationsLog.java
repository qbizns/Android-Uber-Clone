package com.tatx.partnerapp.dataset;

public class WayLocationsLog {
    String id;
    String latitude;
    String longitude;
    String flag;
    String time;
    String totaldistance;


    public String getTotaldistance() {
        return totaldistance;
    }

    public void setTotaldistance(String totaldistance) {
        this.totaldistance = totaldistance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "WayLocationsLog{" +
                "id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", flag='" + flag + '\'' +
                ", time='" + time + '\'' +
                ", totaldistance='" + totaldistance + '\'' +
                '}';
    }

}