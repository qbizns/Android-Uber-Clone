package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Type {

    @SerializedName("vc_id")
    @Expose
    public String vcId;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("type_id")
    @Expose
    public String typeId;
    @SerializedName("category_car_image")
    @Expose
    public String categoryCarImage;
    @SerializedName("map_car_image")
    @Expose
    public String mapCarImage;
    @SerializedName("capacity")
    @Expose
    public String capacity;
    @SerializedName("minfare")
    @Expose
    public String minfare;
    @SerializedName("base_fare")
    @Expose
    public String baseFare;
    @SerializedName("price_per_minute")
    @Expose
    public String pricePerMinute;
    @SerializedName("price_per_km")
    @Expose
    public String pricePerKm;
    @SerializedName("cancellationCharges")
    @Expose
    public String cancellationCharges;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}