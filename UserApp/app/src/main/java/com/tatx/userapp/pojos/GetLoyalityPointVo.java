package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetLoyalityPointVo
{

    @SerializedName("credits")
    @Expose
    public double credits;
    @SerializedName("points")
    @Expose
    public int points;
    @SerializedName("loyality")
    @Expose
    public String loyality;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
