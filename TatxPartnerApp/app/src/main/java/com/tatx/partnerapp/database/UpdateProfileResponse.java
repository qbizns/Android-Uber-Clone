package com.tatx.partnerapp.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 19-10-2016.
 */
public class UpdateProfileResponse {
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
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("earning")
    @Expose
    public String earning;
    @SerializedName("avgrating")
    @Expose
    public float avgrating;
    @SerializedName("avgacceptance")
    @Expose
    public double avgacceptance;
    @SerializedName("erningday")
    @Expose
    public double erningday;
    @SerializedName("online_status")
    @Expose
    public int onlineStatus;

}
