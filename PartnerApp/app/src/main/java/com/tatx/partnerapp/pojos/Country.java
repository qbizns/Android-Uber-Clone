package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Country {

@SerializedName("country_name")
@Expose
public String countryName;
@SerializedName("country_id")
@Expose
public String countryId;
    @SerializedName("phonecode")
    @Expose
    public String phoneCode;
    @SerializedName("image")
    @Expose
    public String image;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}