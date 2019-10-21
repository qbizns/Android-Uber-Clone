package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateDriverStep2 {

@SerializedName("message")
@Expose
public String message;
@SerializedName("cab_id")
@Expose
public String cabId;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}