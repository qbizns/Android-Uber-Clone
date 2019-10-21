package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SocketInitiationVo {

    @SerializedName("success")
    @Expose
    public String success;

    @SerializedName("online_status")
    @Expose
    public int onlineStatus;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}