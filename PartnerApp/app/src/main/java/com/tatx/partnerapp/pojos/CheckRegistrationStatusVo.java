package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CheckRegistrationStatusVo {

    @SerializedName("CreateDriverStep3")
    @Expose
    public boolean createDriverStep3;
    @SerializedName("CreateDriverStep2")
    @Expose
    public boolean createDriverStep2;
    @SerializedName("employee_type")
    @Expose
    public int employeeType;
    @SerializedName("cab_id")
    @Expose
    public int cabId;
    @SerializedName("owner_authority_status")
    @Expose
    public boolean ownerAuthorityStatus;
    @SerializedName("country_of_residency")
    @Expose
    public String countryOfResidency;
    @SerializedName("nationality")
    @Expose
    public String nationality;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}