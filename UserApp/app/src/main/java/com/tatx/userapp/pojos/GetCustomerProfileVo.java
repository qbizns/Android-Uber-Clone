package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class GetCustomerProfileVo implements Serializable {

    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("emergency_name")
    @Expose
    public String emergencyName;
    @SerializedName("emergency_mobile")
    @Expose
    public String emergencyMobile;
    @SerializedName("emergency_country_code")
    @Expose
    public String emergencyCountryCode;
    @SerializedName("status")
    @Expose
    public String status;


    @SerializedName("unread_notifications_count")
    @Expose
    public int unreadNotificationsCount;

    @SerializedName("tatx_id")
    @Expose
    public String  tatxId;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}