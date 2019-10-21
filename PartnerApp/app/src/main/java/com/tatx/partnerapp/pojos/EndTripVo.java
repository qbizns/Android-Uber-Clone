package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


public class EndTripVo implements Serializable
{



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
    public double distance;
    @SerializedName("duration")
    @Expose
    public double duration;
    @SerializedName("dropLat")
    @Expose
    public double dropLat;
    @SerializedName("dropLng")
    @Expose
    public double dropLng;
    @SerializedName("payment_type")
    @Expose
    public int paymentType;

    @SerializedName("collect_cash")
    @Expose
    public double collectCash;

    @SerializedName("discount")
    @Expose
    public double discount;

    @SerializedName("adjustment_amount")
    @Expose
    public double adjustmentAmount;

    @SerializedName("trip_amount")
    @Expose
    public double tripAmount;

    @SerializedName("currency")
    @Expose
    public String currency;

    @SerializedName("driver_share")
    @Expose
    public String driverShare;

    @SerializedName("break_down")
    @Expose
    public int breakDown;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
