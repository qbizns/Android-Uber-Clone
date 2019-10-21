package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Home on 10-09-2016.
 */
public class ShareAndEarnVo {

    @SerializedName("promo_code")
    @Expose
    public String promoCode;
    @SerializedName("share_amount")
    @Expose
    public int shareAmount;
    @SerializedName("promo_amount")
    @Expose
    public String promoAmount;
    @SerializedName("text_share")
    @Expose
    public String textShare;
    @SerializedName("currency_code")
    @Expose
    public String currencyCode;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
