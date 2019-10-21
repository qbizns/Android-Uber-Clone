package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by Venkateswarlu SKP on 27-12-2016.
 */
public class AllReferral implements Serializable
{

    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("total_referral_amount")
    @Expose
    public String totalReferralAmount;
    @SerializedName("profile")
    @Expose
    public String profile;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
