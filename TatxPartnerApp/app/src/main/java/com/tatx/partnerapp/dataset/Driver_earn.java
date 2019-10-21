package com.tatx.partnerapp.dataset;

/**
 * Created by tatx on 22-06-2016.
 */
public class Driver_earn {
    private String amount;

    private String date;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", date = "+date+"]";
    }

}
