package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 15-07-2016.
 */
public class BroadcastLocationVo {


    @SerializedName("types")
    @Expose
    public List<Type> types = new ArrayList<Type>();
    @SerializedName("drivers")
    @Expose
    public List<Driver> drivers = new ArrayList<Driver>();
    @SerializedName("map_car_image_url")
    @Expose
    public String mapCarImageUrl;

    @SerializedName("category_car_image_url")
    @Expose
    public String categoryCarImageUrl;

    @SerializedName("destination_reach_time")
    @Expose
    public String destinationReachTime;



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}