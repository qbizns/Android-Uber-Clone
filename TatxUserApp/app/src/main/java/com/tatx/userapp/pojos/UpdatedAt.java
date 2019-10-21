package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdatedAt {

@SerializedName("date")
@Expose
public String date;
@SerializedName("timezone_type")
@Expose
public int timezoneType;
@SerializedName("timezone")
@Expose
public String timezone;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}