package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CheckUserExistanceStatusVo
{
    @SerializedName("email")
    @Expose
    public boolean email;
    @SerializedName("mobile")
    @Expose
    public boolean mobile;
    @SerializedName("referral_code")
    @Expose
    public boolean referralCode;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}