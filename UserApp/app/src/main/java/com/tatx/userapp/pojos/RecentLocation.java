package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Home on 08-11-2016.
 */
public class RecentLocation implements Serializable{

    @SerializedName("pick_latitude")
    @Expose
    public double pickLatitude;
    @SerializedName("pick_longitude")
    @Expose
    public double pickLongitude;
    @SerializedName("drop_latitude")
    @Expose
    public double dropLatitude;
    @SerializedName("drop_longitude")
    @Expose
    public double dropLongitude;
    @SerializedName("pickup_location")
    @Expose
    public String pickupLocation;
    @SerializedName("drop_location")
    @Expose
    public String dropLocation;
}
