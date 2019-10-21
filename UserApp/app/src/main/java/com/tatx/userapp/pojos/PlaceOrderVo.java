package com.tatx.userapp.pojos;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class PlaceOrderVo {

    @SerializedName("tripid")
    @Expose
    public String tripid;
    @SerializedName("orderid")
    @Expose
    public String orderid;

    @SerializedName("typeid")
    @Expose
    public String typeid;

    @SerializedName("vc_id")
    @Expose
    public String vcId;

    @SerializedName("cancellation_charges")
    @Expose
    public double cancellationCharges;
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
    @SerializedName("vehicle_no")
    @Expose
    public String vehicleNo;
    @SerializedName("make")
    @Expose
    public String make;
    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("drivername")
    @Expose
    public String drivername;
    @SerializedName("profile")
    @Expose
    public String profile;
    @SerializedName("car_image")
    @Expose
    public String carImage;
    @SerializedName("rating")
    @Expose
    public double rating;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("order_status")
    @Expose
    public boolean orderStatus;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("color")
    @Expose
    public String color;
    @SerializedName("code")
    @Expose
    public int code;
    @SerializedName("payment_type")
    @Expose
    public String paymentType;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}