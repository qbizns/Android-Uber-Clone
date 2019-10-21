package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Home on 15-09-2016.
 */
public class RegistrationResponseVo implements Serializable{
    @SerializedName("userid")
    @Expose
    public int userid;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("otp")
    @Expose
    public int otp;
    @SerializedName("promo")
    @Expose
    public String promo;
    @SerializedName("otp_expire")
    @Expose
    public String otpExpire;
    @SerializedName("created_at")
    @Expose
    public CreatedAt createdAt;
    @SerializedName("updated_at")
    @Expose
    public UpdatedAt updatedAt;
    @SerializedName("status")
    @Expose
    public int status;
}
