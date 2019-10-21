package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Home on 12-12-2016.
 */
public class DaylyDetail implements Serializable{

    @SerializedName("seconds")
    @Expose
    public long seconds;
    @SerializedName("day")
    @Expose
    public String day;
}
