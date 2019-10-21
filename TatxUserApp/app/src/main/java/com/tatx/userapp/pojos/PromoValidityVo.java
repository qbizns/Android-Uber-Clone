package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PromoValidityVo {

@SerializedName("end_date")
@Expose
public String endDate;
@SerializedName("price")
@Expose
public String price;
@SerializedName("promo_code")
@Expose
public String promoCode;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}