package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class BonusHistory implements Serializable
{

    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("bonus_amount")
    @Expose
    public String bonusAmount;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}