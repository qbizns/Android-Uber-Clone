package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetFareVo implements Serializable
{

@SerializedName("fare")
@Expose
public List<Fare> fare = new ArrayList<Fare>();

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}