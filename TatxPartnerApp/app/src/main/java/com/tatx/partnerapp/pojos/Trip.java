package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Trip implements Serializable
{



    @SerializedName("trip_id")
    @Expose
    public int tripId;
    @SerializedName("cabs_id")
    @Expose
    public int cabsId;
    @SerializedName("driver_id")
    @Expose
    public int driverId;
    @SerializedName("base_fare")
    @Expose
    public String baseFare;
    @SerializedName("price_per_minute")
    @Expose
    public String pricePerMinute;
    @SerializedName("price_per_km")
    @Expose
    public String pricePerKm;
    @SerializedName("min_price")
    @Expose
    public String minPrice;
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
    @SerializedName("route")
    @Expose
    public String route;
    @SerializedName("profile")
    @Expose
    public String profile;
    @SerializedName("distance_value")
    @Expose
    public double distanceValue;
    @SerializedName("distance_cost")
    @Expose
    public double distanceCost;
    @SerializedName("duration_value")
    @Expose
    public double durationValue;
    @SerializedName("duration_value_round")
    @Expose
    public double durationValueRound;
    @SerializedName("duration_cost")
    @Expose
    public double durationCost;
    @SerializedName("duration_cost_round")
    @Expose
    public double durationCostRound;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("pickup_location")
    @Expose
    public String pickupLocation;
    @SerializedName("drop_location")
    @Expose
    public String dropLocation;
    @SerializedName("end_time")
    @Expose
    public String endTime;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("vehicle_type")
    @Expose
    public String vehicleType;
    @SerializedName("date_time")
    @Expose
    public String dateTime;
    @SerializedName("make")
    @Expose
    public String make;
    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("driver_rating")
    @Expose
    public int driverRating;
    @SerializedName("user_rating")
    @Expose
    public int userRating;
    @SerializedName("payment_type_id")
    @Expose
    public String paymentTypeId;
    @SerializedName("subtotal")
    @Expose
    public double subtotal;
    @SerializedName("sub_total_round")
    @Expose
    public double subTotalRound;
    @SerializedName("tip")
    @Expose
    public double tip;
    @SerializedName("trip_amount")
    @Expose
    public double tripAmount;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("level")
    @Expose
    public int level;
    @SerializedName("trip_status")
    @Expose
    public String tripStatus;
    @SerializedName("commission")
    @Expose
    public double commission;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("discount")
    @Expose
    public double discount;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }



}