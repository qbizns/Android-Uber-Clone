package com.tatx.partnerapp.dataset;

/**
 * Created by user on 20-06-2016.
 */
public class Type
{
    private int id;

    private String type;

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", type = "+type+"]";
    }
}