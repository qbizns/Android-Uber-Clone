package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerOnTripVo {

    @SerializedName("ontrip")
    @Expose
    public OnTrip ontrip;
    @SerializedName("tripstatus")
    @Expose
    public int tripstatus;

}