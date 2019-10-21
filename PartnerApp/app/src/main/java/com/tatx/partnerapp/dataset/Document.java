package com.tatx.partnerapp.dataset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Venkateswarlu SKP on 30-01-2017.
 */
public class Document {

    @SerializedName("doc_name")
    @Expose
    public String docName;
    @SerializedName("doc_id")
    @Expose
    public String docId;
    @SerializedName("cab_id")
    @Expose
    public int cabId;
    @SerializedName("isUnderReview")
    @Expose
    public boolean isUnderReview;
    @SerializedName("doc")
    @Expose
    public String doc;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}