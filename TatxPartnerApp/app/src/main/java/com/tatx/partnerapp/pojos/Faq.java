package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 14-12-2016.
 */
public class Faq {
    @SerializedName("id")
    @Expose
    public double id;
    @SerializedName("question")
    @Expose
    public String question;
    @SerializedName("answer")
    @Expose
    public String answer;
    @SerializedName("role")
    @Expose
    public String role;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
}
