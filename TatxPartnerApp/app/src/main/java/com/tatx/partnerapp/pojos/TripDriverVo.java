package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class TripDriverVo
{

@SerializedName("orders")
@Expose
public List<Order> orders = new ArrayList<Order>();

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}