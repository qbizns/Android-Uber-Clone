package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateReadStatusVo {

@SerializedName("message")
@Expose
public String message;

@SerializedName("unread_notifications_count")
@Expose
public int unreadNotificationsCount;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}