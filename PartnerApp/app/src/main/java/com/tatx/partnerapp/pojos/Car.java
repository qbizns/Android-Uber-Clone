package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Car {

@SerializedName("id")
@Expose
public String id;
@SerializedName("vehicle_no")
@Expose
public String vehicleNo;
@SerializedName("make")
@Expose
public String make;
@SerializedName("model")
@Expose
public String model;
@SerializedName("drive_name")
@Expose
public String driveName;
@SerializedName("reg_no")
@Expose
public String regNo;
@SerializedName("image")
@Expose
public String image;
@SerializedName("color")
@Expose
public String color;
@SerializedName("primary")
@Expose
public String primary;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}