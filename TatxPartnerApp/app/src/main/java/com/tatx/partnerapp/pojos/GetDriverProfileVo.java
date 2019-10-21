package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class GetDriverProfileVo {

    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("make")
    @Expose
    public String make;
    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("type")
    @Expose
    public List<Type> type = new ArrayList<Type>();
    @SerializedName("country")
    @Expose
    public int country;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("earning")
    @Expose
    public String earning;
    @SerializedName("avgrating")
    @Expose
    public String avgrating;
    @SerializedName("erningday")
    @Expose
    public double erningday;
    @SerializedName("avgacceptance")
    @Expose
    public double avgacceptance;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("cab_active_status")
    @Expose
    public boolean cabActiveStatus;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("tatx_id")
    @Expose
    public String tatxId;
    @SerializedName("unread_notifications_count")
    @Expose
    public int unreadNotificationsCount;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}