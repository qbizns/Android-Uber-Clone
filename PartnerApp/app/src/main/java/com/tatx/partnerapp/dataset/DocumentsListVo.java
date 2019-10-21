package com.tatx.partnerapp.dataset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by Home on 06-10-2016.
 */
public class DocumentsListVo
{


    @SerializedName("documents")
    @Expose
    public List<Document> documents = null;

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }



}
