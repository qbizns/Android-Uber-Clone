package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by user on 15-07-2016.
 */
public class Driver implements Comparable
{

    @SerializedName("capacity")
    @Expose
    public int capacity;
    @SerializedName("driver_id")
    @Expose
    public int driverId;
    @SerializedName("vehicle_no")
    @Expose
    public String vehicleNo;
    @SerializedName("min_price")
    @Expose
    public String minPrice;
    @SerializedName("price_per_km")
    @Expose
    public String pricePerKm;
    @SerializedName("price_per_minute")
    @Expose
    public String pricePerMinute;
    @SerializedName("base_fare")
    @Expose
    public String baseFare;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("type_id")
    @Expose
    public int typeId;
    @SerializedName("category_car_image")
    @Expose
    public String categoryCarImage;
    @SerializedName("map_car_image")
    @Expose
    public String mapCarImage;
    @SerializedName("vcId")
    @Expose
    public int vcId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("latitude")
    @Expose
    public double latitude;
    @SerializedName("longitude")
    @Expose
    public double longitude;
    @SerializedName("distance")
    @Expose
    public String distance;
    @SerializedName("time")
    @Expose
    public int time;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Driver driver = (Driver) o;

        return driverId == driver.driverId && typeId==driver.typeId;

    }

    @Override
    public int hashCode()
    {
        return driverId;
    }


    @Override
    public int compareTo(Object another)
    {
        return time - ((Driver)another).time;
    }


}
