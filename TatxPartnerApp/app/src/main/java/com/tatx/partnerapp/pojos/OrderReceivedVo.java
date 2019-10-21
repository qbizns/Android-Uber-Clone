package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class OrderReceivedVo implements Serializable
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
    public String pickLatitude;
    @SerializedName("pick_longitude")
    @Expose
    public String pickLongitude;
    @SerializedName("drop_latitude")
    @Expose
    public String dropLatitude;
    @SerializedName("drop_longitude")
    @Expose
    public String dropLongitude;
    @SerializedName("customername")
    @Expose
    public String customername;
    @SerializedName("profile")
    @Expose
    public String profile;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("code")
    @Expose
    public int code;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}