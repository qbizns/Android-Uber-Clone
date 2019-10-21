package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Home on 15-12-2016.
 */
public class Order implements Serializable{

    @SerializedName("order_id")
    @Expose
    public String orderId;
    @SerializedName("payment_type_id")
    @Expose
    public String paymentTypeId;
    @SerializedName("total_cash")
    @Expose
    public String totalCash;
    @SerializedName("order_status_type_id")
    @Expose
    public String orderStatusTypeId;
    @SerializedName("trip")
    @Expose
    public List<Trip> trip = null;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
