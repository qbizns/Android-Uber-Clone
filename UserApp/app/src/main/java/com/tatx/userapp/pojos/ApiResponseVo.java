package com.tatx.userapp.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ApiResponseVo
{

    @SerializedName("data")
    @Expose
    public Object data;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("requestname")
    @Expose
    public String requestname;
    @SerializedName("code")
    @Expose
    public int code;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}



