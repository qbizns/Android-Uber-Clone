package com.tatx.partnerapp.dataset;

/**
 * Created by user on 18-06-2016.
 */
public class Documentspojo {

    private String doc_name;

    private String doc;

    public String getDoc_name ()
    {
        return doc_name;
    }

    public void setDoc_name (String doc_name)
    {
        this.doc_name = doc_name;
    }

    public String getDoc ()
    {
        return doc;
    }

    public void setDoc (String doc)
    {
        this.doc = doc;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [doc_name = "+doc_name+", doc = "+doc+"]";
    }

}
