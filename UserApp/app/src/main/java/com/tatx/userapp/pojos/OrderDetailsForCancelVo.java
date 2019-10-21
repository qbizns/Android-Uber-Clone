package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by user on 14-07-2016.
 */
public class OrderDetailsForCancelVo
{
    @SerializedName("tripid")
    @Expose
    public String tripid;
    @SerializedName("orderid")
    @Expose
    public String orderid;
    @SerializedName("vcId")
    @Expose
    public String typeid;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
