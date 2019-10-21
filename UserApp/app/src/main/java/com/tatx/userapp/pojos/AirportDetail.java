package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 17-11-2016.
 */
public class AirportDetail {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("lat")
    @Expose
    public double lat;
    @SerializedName("lng")
    @Expose
    public double lng;
    @SerializedName("latNE")
    @Expose
    public double latNE;
    @SerializedName("lngNE")
    @Expose
    public double lngNE;
    @SerializedName("latSW")
    @Expose
    public double latSW;
    @SerializedName("lngSW")
    @Expose
    public double lngSW;
    @SerializedName("city")
    @Expose
    public String city;
}
