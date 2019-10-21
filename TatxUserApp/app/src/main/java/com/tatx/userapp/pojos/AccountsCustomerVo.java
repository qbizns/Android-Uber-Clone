package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class AccountsCustomerVo {

    @SerializedName("cards")
    @Expose
    public List<Card> cards = new ArrayList<Card>();
    @SerializedName("credits")
    @Expose
    public double credits;
    @SerializedName("points")
    @Expose
    public int points;
    @SerializedName("loyality")
    @Expose
    public String loyality;
    @SerializedName("payment_type_id")
    @Expose
    public int paymentTypeId;

    @SerializedName("level_desc")
    @Expose
    public String levelDesc;

    @SerializedName("currency_code")
    @Expose
    public String currencyCode;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}