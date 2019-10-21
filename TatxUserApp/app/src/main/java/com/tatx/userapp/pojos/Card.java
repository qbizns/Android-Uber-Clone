package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Card {

@SerializedName("id")
@Expose
public int id;
@SerializedName("name")
@Expose
public String name;
@SerializedName("expiry_date")
@Expose
public String expiryDate;
@SerializedName("number")
@Expose
public String number;
@SerializedName("brand")
@Expose
public String brand;
@SerializedName("currency")
@Expose
public String currency;
@SerializedName("status")
@Expose
public String status;
@SerializedName("token")
@Expose
public String token;
@SerializedName("primary")
@Expose
public String primary;
@SerializedName("user_id")
@Expose
public String userId;
@SerializedName("created_at")
@Expose
public String createdAt;
@SerializedName("updated_at")
@Expose
public String updatedAt;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}