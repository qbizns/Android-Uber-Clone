package com.tatx.partnerapp.dataset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 04-10-2016.
 */
public class CreateDriverStep1Vo {

    @SerializedName("userid")
    @Expose
    public int userid;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("status")
    @Expose
    public float status;
}
