package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class City {

@SerializedName("name")
@Expose
public String name;
@SerializedName("id")
@Expose
public int id;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}