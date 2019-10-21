package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetBonusDetailsVo implements Serializable
{
    @SerializedName("bonus_history")
    @Expose
    public List<BonusHistory> bonusHistory = new ArrayList<BonusHistory>();
    @SerializedName("total_paid")
    @Expose
    public String totalPaid;
    @SerializedName("total_unapaid")
    @Expose
    public double totalUnapaid;
    @SerializedName("avg_rating")
    @Expose
    public String avgRating;
    @SerializedName("acceptance_rate")
    @Expose
    public String acceptanceRate;
    @SerializedName("online_hours_week")
    @Expose
    public String onlineHoursWeek;
    @SerializedName("online_hours_peak")
    @Expose
    public String onlineHoursPeak;
    @SerializedName("mintrips_week")
    @Expose
    public String mintripsWeek;
    @SerializedName("avg_rating_desc")
    @Expose
    public String avgRatingDesc;
    @SerializedName("acceptance_rate_desc")
    @Expose
    public String acceptanceRateDesc;
    @SerializedName("online_hours_week_desc")
    @Expose
    public String onlineHoursWeekDesc;
    @SerializedName("online_hours_peak_desc")
    @Expose
    public String onlineHoursPeakDesc;
    @SerializedName("mintrips_week_desc")
    @Expose
    public String mintripsWeekDesc;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("minrating")
    @Expose
    public String minrating;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}