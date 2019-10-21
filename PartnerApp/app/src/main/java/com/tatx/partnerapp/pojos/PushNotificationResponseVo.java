package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PushNotificationResponseVo
{

    @SerializedName("code")
    @Expose
    public int code;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("data")
    @Expose
    public Object data;
    @SerializedName("datetime")
    @Expose
    public int datetime;

    @SerializedName("unread_notifications_count")
    @Expose
    public int unreadNotificationsCount;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }



}
