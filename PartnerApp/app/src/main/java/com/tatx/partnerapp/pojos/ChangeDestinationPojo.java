package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Home on 21-12-2016.
 */
public class ChangeDestinationPojo {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("drop_latitude")
    @Expose
    public String dropLatitude;
    @SerializedName("drop_longitude")
    @Expose
    public String dropLongitude;
    @SerializedName("drop_location")
    @Expose
    public String dropLocation;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
