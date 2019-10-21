
package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class UpdateDriverProfileDataVo {

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
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("country")
    @Expose
    public Object country;
    @SerializedName("created_at")
    @Expose
    public CreatedAt createdAt;
    @SerializedName("updated_at")
    @Expose
    public UpdatedAt updatedAt;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("earning")
    @Expose
    public int earning;
    @SerializedName("avgrating")
    @Expose
    public int avgrating;
    @SerializedName("avgacceptance")
    @Expose
    public double avgacceptance;
    @SerializedName("erningday")
    @Expose
    public double erningday;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
