package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class GetTopPerformersDetails implements Serializable
{

@SerializedName("week")
@Expose
public Week week;
@SerializedName("all")
@Expose
public All all;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}