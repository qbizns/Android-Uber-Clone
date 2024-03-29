package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateCustomerVo {

    @SerializedName("userid")
    @Expose
    public int userid;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("promo")
    @Expose
    public String promo;
    @SerializedName("created_at")
    @Expose
    public CreatedAt createdAt;
    @SerializedName("updated_at")
    @Expose
    public UpdatedAt updatedAt;
    @SerializedName("status")
    @Expose
    public int status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}