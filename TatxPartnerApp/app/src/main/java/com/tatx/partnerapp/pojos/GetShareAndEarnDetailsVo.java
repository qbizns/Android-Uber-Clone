package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Venkateswarlu SKP on 27-12-2016.
 */
public class GetShareAndEarnDetailsVo implements Serializable
{
    @SerializedName("referral_code")
    @Expose
    public String referralCode;

    @SerializedName("refer_amount")
    @Expose
    public int referAmount;
    @SerializedName("currency_code")
    @Expose
    public String currencyCode;
    @SerializedName("all_referral")
    @Expose
    public ArrayList<AllReferral> allReferral = null;

    @SerializedName("total_all_referral_amount")
    @Expose
    public String totalAllReferralAmount;

    @SerializedName("text_share")
    @Expose
    public String textShare;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
