package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateDeviceTokenVo
{

@SerializedName("message")
@Expose
public String message;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}
