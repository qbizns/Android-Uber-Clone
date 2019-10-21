package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class OnSocketOpenVo
{

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("role_id")
    @Expose
    public String roleId;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("os")
    @Expose
    public String os;
    @SerializedName("online_status")
    @Expose
    public int onlineStatus;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}