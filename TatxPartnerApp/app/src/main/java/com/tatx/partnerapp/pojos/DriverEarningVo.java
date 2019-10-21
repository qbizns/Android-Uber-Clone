package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class DriverEarningVo
{

    @SerializedName("driver_earn")
    @Expose
    public List<DriverEarn> driverEarn = new ArrayList<DriverEarn>();


    @SerializedName("last_paid")
    @Expose
    public double lastPaid;



    @SerializedName("due_amount")
    @Expose
    public double dueAmount;


    @SerializedName("bonus")
    @Expose
    public String bonus;


    @SerializedName("currency")
    @Expose
    public String currency;



    @SerializedName("last_paid_date")
    @Expose
    public String lastPaidDate;






    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}