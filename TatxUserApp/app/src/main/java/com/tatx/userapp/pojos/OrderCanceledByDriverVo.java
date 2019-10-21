package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderCanceledByDriverVo {

@SerializedName("message")
@Expose
public String message;
@SerializedName("code")
@Expose
public int code;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}