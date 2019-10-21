package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class ChangeDestinationVo {


    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("drop_latitude")
    @Expose
    public double dropLatitude;
    @SerializedName("drop_longitude")
    @Expose
    public double dropLongitude;
    @SerializedName("drop_location")
    @Expose
    public String dropLocation;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}