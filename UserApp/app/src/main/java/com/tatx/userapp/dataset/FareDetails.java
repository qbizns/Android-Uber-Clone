package com.tatx.userapp.dataset;

/**
 * Created by Home on 13-06-2016.
 */
public class FareDetails {
    private String min;

    private String max;

    private String capacity;

    private String type_id;

    public String getMin ()
    {
        return min;
    }

    public void setMin (String min)
    {
        this.min = min;
    }

    public String getMax ()
    {
        return max;
    }

    public void setMax (String max)
    {
        this.max = max;
    }

    public String getCapacity ()
    {
        return capacity;
    }

    public void setCapacity (String capacity)
    {
        this.capacity = capacity;
    }

    public String getType_id ()
    {
        return type_id;
    }

    public void setType_id (String type_id)
    {
        this.type_id = type_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [min = "+min+", max = "+max+", capacity = "+capacity+", type_id = "+type_id+"]";
    }
}


