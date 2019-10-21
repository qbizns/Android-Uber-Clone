package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class GetReasonsForCancelVo {

@SerializedName("reasons")
@Expose
public List<Reason> reasons = new ArrayList<Reason>();

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

}