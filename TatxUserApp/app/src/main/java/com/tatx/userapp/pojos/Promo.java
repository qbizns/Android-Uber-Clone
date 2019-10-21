package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Home on 24-09-2016.
 */
public class Promo implements Serializable
{
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("promo_code")
    @Expose
    public String promoCode;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end_date")
    @Expose
    public String endDate;
}
