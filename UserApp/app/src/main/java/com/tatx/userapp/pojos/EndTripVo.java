package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


public class EndTripVo implements Serializable {


    @SerializedName("amount")
    @Expose
    public double amount;
    @SerializedName("tripId")
    @Expose
    public String tripId;
    @SerializedName("orderid")
    @Expose
    public String orderId;
    @SerializedName("pickLat")
    @Expose
    public double pickLat;
    @SerializedName("pickLng")
    @Expose
    public double pickLng;
    @SerializedName("distance")
    @Expose
    public String distance;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("dropLat")
    @Expose
    public double dropLat;
    @SerializedName("dropLng")
    @Expose
    public double dropLng;
    @SerializedName("payment_type")
    @Expose
    public int paymentType;
    @SerializedName("pickup_location")
    @Expose
    public String pickupLocation;
    @SerializedName("drop_location")
    @Expose
    public String dropLocation;
    @SerializedName("distance_cost")
    @Expose
    public double distanceCost;
    @SerializedName("duration_cost")
    @Expose
    public double durationCost;

    @SerializedName("base_fare")
    @Expose
    public double baseFare;

    @SerializedName("adjustment_amount")
    @Expose
    public double adjustmentAmount;

    @SerializedName("trip_amount")
    @Expose
    public double tripAmount;

    @SerializedName("discount")
    @Expose
    public String discount;

    @SerializedName("currency")
    @Expose
    public String currency;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public EndTripVo(double amount, String tripId, String orderId, double pickLat, double pickLng, String distance, String duration, double dropLat, double dropLng, int paymentType, String pickupLocation, String dropLocation, double distanceCost, double durationCost, double baseFare, double adjustmentAmount, double tripAmount, String discount, String currency)
    {
        this.amount = amount;
        this.tripId = tripId;
        this.orderId = orderId;
        this.pickLat = pickLat;
        this.pickLng = pickLng;
        this.distance = distance;
        this.duration = duration;
        this.dropLat = dropLat;
        this.dropLng = dropLng;
        this.paymentType = paymentType;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.distanceCost = distanceCost;
        this.durationCost = durationCost;
        this.baseFare = baseFare;
        this.adjustmentAmount = adjustmentAmount;
        this.tripAmount = tripAmount;
        this.discount = discount;
        this.currency = currency;

    }

    public EndTripVo() {
    }
}
