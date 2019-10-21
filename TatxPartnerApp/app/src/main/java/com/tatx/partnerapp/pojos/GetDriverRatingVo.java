package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 14-12-2016.
 */
public class GetDriverRatingVo {
    @SerializedName("avgrating")
    @Expose
    public String avgrating;
    @SerializedName("totoaltrips")
    @Expose
    public String totoaltrips;
    @SerializedName("ratingtrips")
    @Expose
    public String ratingtrips;
    @SerializedName("fullrating")
    @Expose
    public String fullrating;


}
