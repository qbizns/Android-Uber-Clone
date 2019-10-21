package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by user on 07-07-2016.
 */

public class OnTrip implements Serializable
{
    @SerializedName("tripid")
    @Expose
    public int tripid;
    @SerializedName("orderid")
    @Expose
    public int orderid;
    @SerializedName("vc_id")
    @Expose
    public int vcId;
    @SerializedName("cancellation_charges")
    @Expose
    public double cancellationCharges;
    @SerializedName("pick_latitude")
    @Expose
    public double pickLatitude;

    @SerializedName("pick_longitude")
    @Expose
    public double pickLongitude;
    @SerializedName("drop_latitude")
    @Expose
    public double dropLatitude;
    @SerializedName("drop_longitude")
    @Expose
    public double dropLongitude;
    @SerializedName("vehicle_no")
    @Expose
    public String vehicleNo;
    @SerializedName("make")
    @Expose
    public String make;
    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("profile")
    @Expose
    public String profile;
    @SerializedName("car_image")
    @Expose
    public String carImage;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("drivername")
    @Expose
    public String drivername;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("color")
    @Expose
    public String color;
    @SerializedName("payment_type")
    @Expose
    public String paymentType;

    public OnTrip(int tripid, int orderid, int vcId, double cancellationCharges, double pickLatitude, double pickLongitude, double dropLatitude, double dropLongitude, String vehicleNo, String make, String model, String profile, String rating, String drivername, String mobile,String carImage,String color,String paymentType) {
        this.tripid = tripid;
        this.orderid = orderid;
        this.vcId = vcId;
        this.cancellationCharges = cancellationCharges;
        this.pickLatitude = pickLatitude;
        this.pickLongitude = pickLongitude;
        this.dropLatitude = dropLatitude;
        this.dropLongitude = dropLongitude;
        this.vehicleNo = vehicleNo;
        this.make = make;
        this.model = model;
        this.profile = profile;
        this.rating = rating;
        this.drivername = drivername;
        this.mobile = mobile;
        this.carImage=carImage;
        this.color=color;
        this.paymentType=paymentType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}