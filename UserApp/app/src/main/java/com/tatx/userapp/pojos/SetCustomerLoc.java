package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class SetCustomerLoc
{

@SerializedName("types")
@Expose
public List<Type> types = new ArrayList<Type>();
@SerializedName("map_car_image_url")
@Expose
public String mapCarImageUrl;
@SerializedName("category_car_image_url")
@Expose
public String categoryCarImageUrl;
@SerializedName("drivers")
@Expose
public List<Driver> drivers = new ArrayList<Driver>();

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}