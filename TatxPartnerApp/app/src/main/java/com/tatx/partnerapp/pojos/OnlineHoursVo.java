package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Home on 03-12-2016.
 */
public class OnlineHoursVo implements Serializable{

    @SerializedName("all_time")
    @Expose
    public long allTime;
    @SerializedName("daily_avg")
    @Expose
    public long dailyAvg;
    @SerializedName("total_seconds")
    @Expose
    public long totalSeconds;
    @SerializedName("daily_details")
    @Expose
    public List<DaylyDetail> dailyDetails = null;
    @SerializedName("current_day_details")
    @Expose
    public List<CurrentDayDetail> currentDayDetails = null;


}
