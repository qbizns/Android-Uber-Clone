package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Order {

@SerializedName("order_id")
@Expose
public int orderId;
@SerializedName("payment_type_id")
@Expose
public String paymentTypeId;
@SerializedName("total_cash")
@Expose
public String totalCash;
@SerializedName("order_status_type_id")
@Expose
public int orderStatusTypeId;
@SerializedName("trip")
@Expose
public List<Trip> trip = new ArrayList<Trip>();

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}