package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Ontrip implements Serializable
{

    @SerializedName("tripid")
    @Expose
    public int tripid;
    @SerializedName("orderid")
    @Expose
    public int orderid;
    @SerializedName("typeid")
    @Expose
    public int typeid;
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
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("profile")
    @Expose
    public String profile;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("mobile")
    @Expose
    public String mobile;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}