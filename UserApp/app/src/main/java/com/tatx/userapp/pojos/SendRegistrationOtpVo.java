package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SendRegistrationOtpVo
{

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

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}