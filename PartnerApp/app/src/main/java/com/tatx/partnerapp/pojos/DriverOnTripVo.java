package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class DriverOnTripVo implements Serializable
{

    @SerializedName("ontrip")
    @Expose
    public Ontrip ontrip;
    @SerializedName("tripstatus")
    @Expose
    public int tripstatus;
    @SerializedName("driverStatus")
    @Expose
    public int driverStatus;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
