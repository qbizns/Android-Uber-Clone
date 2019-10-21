package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class DriverEarn {

@SerializedName("date")
@Expose
public String date;
@SerializedName("amount")
@Expose
public String amount;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}
