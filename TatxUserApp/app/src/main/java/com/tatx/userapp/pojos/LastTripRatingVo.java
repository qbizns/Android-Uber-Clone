package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class LastTripRatingVo
{

@SerializedName("tripDetails")
@Expose
public TripDetails tripDetails;
@SerializedName("ratingStatus")
@Expose
public boolean ratingStatus;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}