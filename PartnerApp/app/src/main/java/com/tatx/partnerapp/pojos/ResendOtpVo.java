package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 14-12-2016.
 */
public class ResendOtpVo {

    @SerializedName("userid")
    @Expose
    public int userid;
    @SerializedName("otp")
    @Expose
    public int otp;
    @SerializedName("otp_expire")
    @Expose
    public String otpExpire;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
}
