package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Venkateswarlu SKP on 03-09-2016.
 */
public class CustomerLoginVo {

    @SerializedName("userid")
    @Expose
    public int userid;

    @SerializedName("unread_notifications_count")
    @Expose
    public int unreadNotificationsCount;

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
    @SerializedName("promo_img")
    @Expose
    public String promoImg;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("promo")
    @Expose
    public String promo;
    @SerializedName("customer_trip")
    @Expose
    public boolean customerTrip;
    @SerializedName("payment_type_id")
    @Expose
    public String paymentTypeId;
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
    @SerializedName("tatx_id")
    @Expose
    public String tatxId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
