package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class GetCityVo
{

@SerializedName("cities")
@Expose
public List<City> cities = new ArrayList<City>();

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}