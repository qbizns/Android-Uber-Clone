package com.tatx.userapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by Home on 19-05-2016.
 */
public class Trip implements Serializable {
    @SerializedName("trip_id")
    @Expose
    public String tripId;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("cabs_id")
    @Expose
    public String cabsId;
    @SerializedName("driver_id")
    @Expose
    public String driverId;
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
    @SerializedName("route")
    @Expose
    public String route;
    @SerializedName("profile")
    @Expose
    public String profile;
    @SerializedName("distance_value")
    @Expose
    public String distanceValue;
    @SerializedName("distance_cost")
    @Expose
    public double distanceCost;
    @SerializedName("duration_value")
    @Expose
    public String durationValue;
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
    @SerializedName("end_time")
    @Expose
    public String endTime;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("date_time")
    @Expose
    public String dateTime;
    @SerializedName("vehicle_type")
    @Expose
    public String vehicleType;
    @SerializedName("make")
    @Expose
    public String make;
    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("driver_rating")
    @Expose
    public String driverRating;
    @SerializedName("user_rating")
    @Expose
    public long userRating;
    @SerializedName("airport_charges")
    @Expose
    public String airportCharges;
    @SerializedName("payment_type_id")
    @Expose
    public String paymentTypeId;
    @SerializedName("subtotal")
    @Expose
    public String subtotal;
    @SerializedName("actsubtotal")
    @Expose
    public double actsubtotal;
    @SerializedName("tip")
    @Expose
    public String tip;
    @SerializedName("trip_amount")
    @Expose
    public double tripAmount;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("level")
    @Expose
    public String level;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("trip_status")
    @Expose
    public String tripStatus;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("cancellation_charges")
    @Expose
    public String cancellationCharges;
    @SerializedName("adjustment_amount")
    @Expose
    public double adjustmentAmount;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}