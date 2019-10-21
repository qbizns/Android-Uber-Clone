package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by Venkateswarlu SKP on 22-07-2016.
 */
public class OrderInitiatedVo implements Serializable
{


    @SerializedName("code")
    @Expose
    public int code;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("tripid")
    @Expose
    public int tripid;
    @SerializedName("orderid")
    @Expose
    public int orderid;
    @SerializedName("vcId")
    @Expose
    public String typeid;
    @SerializedName("datetime")
    @Expose
    public int datetime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }



}

