package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Venkateswarlu SKP on 13-01-2017.
 */
public class LastTripDriverRatingVo {

    @SerializedName("endTripVo")
    @Expose
    public EndTripVo endTripVo;
    @SerializedName("ratingStatus")
    @Expose
    public boolean ratingStatus;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
