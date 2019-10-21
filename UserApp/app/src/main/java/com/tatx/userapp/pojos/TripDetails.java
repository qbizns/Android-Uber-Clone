package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class TripDetails {

    @SerializedName("tripid")
    @Expose
    public String tripid;
    @SerializedName("orderid")
    @Expose
    public String orderid;
    @SerializedName("pick_latitude")
    @Expose
    public String pickLatitude;
    @SerializedName("pick_longitude")
    @Expose
    public String pickLongitude;
    @SerializedName("drop_latitude")
    @Expose
    public String dropLatitude;
    @SerializedName("drop_longitude")
    @Expose
    public String dropLongitude;
    @SerializedName("pickup_location")
    @Expose
    public String pickupLocation;
    @SerializedName("drop_location")
    @Expose
    public String dropLocation;
    @SerializedName("drivername")
    @Expose
    public String drivername;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("distance")
    @Expose
    public String distance;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("distance_cost")
    @Expose
    public double distanceCost;
    @SerializedName("duration_cost")
    @Expose
    public double durationCost;
    @SerializedName("adjustment_amount")
    @Expose
    public double adjustmentAmount;
    @SerializedName("base_fare")
    @Expose
    public String baseFare;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("trip_amount")
    @Expose
    public String tripAmount;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("payment_type")
    @Expose
    public String paymentType;

    @SerializedName("currency")
    @Expose
    public String currency;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}