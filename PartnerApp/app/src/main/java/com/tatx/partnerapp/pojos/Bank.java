package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Bank {

@SerializedName("bank_name")
@Expose
public String bankName;
@SerializedName("bank_id")
@Expose
public String bankId;

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}