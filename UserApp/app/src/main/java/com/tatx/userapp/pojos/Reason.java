package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Reason {

@SerializedName("reason")
@Expose
public String reason;
@SerializedName("reason_id")
@Expose
public String reasonId;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}