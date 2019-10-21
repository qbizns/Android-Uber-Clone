package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Home on 12-12-2016.
 */
public class DayDetailsVo implements Serializable{

    @SerializedName("current_day_details")
    @Expose
    public List<DayDetail> currentDayDetails = null;
    @SerializedName("total_seconds")
    @Expose
    public String totalSeconds;

}
