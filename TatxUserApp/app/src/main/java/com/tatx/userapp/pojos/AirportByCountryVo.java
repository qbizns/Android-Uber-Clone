package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 17-11-2016.
 */
public class AirportByCountryVo {
    @SerializedName("airportDetails")
    @Expose
    public List<AirportDetail> airportDetails = new ArrayList<AirportDetail>();
}
