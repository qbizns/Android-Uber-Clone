package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Home on 17-05-2016.
 */
public class Transactions {
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}


