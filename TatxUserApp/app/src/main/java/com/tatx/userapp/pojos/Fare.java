package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Fare implements Serializable
{

@SerializedName("type_id")
@Expose
public int typeId;
@SerializedName("min")
@Expose
public int min;
@SerializedName("max")
@Expose
public int max;
@SerializedName("capacity")
@Expose
public int capacity;
@SerializedName("currency")
@Expose
public String currency;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}