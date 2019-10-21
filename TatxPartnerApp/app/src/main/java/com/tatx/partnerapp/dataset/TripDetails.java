package com.tatx.partnerapp.dataset;

/**
 * Created by Home on 07-06-2016.
 */
public class TripDetails {
    private String longitude_src;

    private String latitude_src;

    private String longitude_dest;

    private String latitude_dest;

    private String orderid;

    private String tripid;

    private String mobile;

    private String customername;

    private String polyline;

    public String getLatitude_src() {
        return latitude_src;
    }

    public void setLatitude_src(String latitude_src) {
        this.latitude_src = latitude_src;
    }

    public String getLongitude_src() {
        return longitude_src;
    }

    public void setLongitude_src(String longitude_src) {
        this.longitude_src = longitude_src;
    }

    public String getLongitude_dest() {
        return longitude_dest;
    }

    public void setLongitude_dest(String longitude_dest) {
        this.longitude_dest = longitude_dest;
    }

    public String getLatitude_dest() {
        return latitude_dest;
    }

    public void setLatitude_dest(String latitude_dest) {
        this.latitude_dest = latitude_dest;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public String getOrderid ()
    {
        return orderid;
    }

    public void setOrderid (String orderid)
    {
        this.orderid = orderid;
    }

    public String getTripid ()
    {
        return tripid;
    }

    public void setTripid (String tripid)
    {
        this.tripid = tripid;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    public String getCustomername ()
    {
        return customername;
    }

    public void setCustomername (String customername)
    {
        this.customername = customername;
    }


}

