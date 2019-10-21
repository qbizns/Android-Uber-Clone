package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Home on 15-12-2016.
 */
public class TripHistoryVo implements Serializable{
    @SerializedName("orders")
    @Expose
    public List<Order> orders = null;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
