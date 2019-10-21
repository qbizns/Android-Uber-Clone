package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Week implements Serializable {

@SerializedName("driver_revenue")
@Expose
public double driverRevenue;
@SerializedName("top_performers_revenue")
@Expose
public double topPerformersRevenue;
@SerializedName("driver_avg_rating")
@Expose
public double driverAvgRating;
@SerializedName("top_performers_avg_rating")
@Expose
public double topPerformersAvgRating;
@SerializedName("driver_online_hours")
@Expose
public double driverOnlineHours;
@SerializedName("top_performers_online_hours")
@Expose
public double topPerformersOnlineHours;
@SerializedName("driver_number_of_trips")
@Expose
public double driverNumberOfTrips;
@SerializedName("top_performers_number_of_trips")
@Expose
public double topPerformersNumberOfTrips;
@SerializedName("driver_avg_acceptance_percentage")
@Expose
public double driverAvgAcceptancePercentage;
@SerializedName("top_performers_avg_acceptance_percentage")
@Expose
public double topPerformersAvgAcceptancePercentage;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}