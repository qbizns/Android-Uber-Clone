package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Type {

    public String getType() {
        return type;
    }

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("id")
    @Expose
    public String id;

    public String getTypeId() {
        return typeId;
    }

    @SerializedName("type_id")
    @Expose
    public String typeId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}