package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Home on 12-12-2016.
 */
public class DayDetail implements Serializable{
    @SerializedName("seconds")
    @Expose
    public long seconds;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("end_time")
    @Expose
    public String endTime;
}
