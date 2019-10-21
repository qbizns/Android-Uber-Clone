package com.tatx.partnerapp.dataset;

/**
 * Created by Home on 17-05-2016.
 */
public class CreditBalHistory {
    private String amount;

    private String id;

    private String created_at;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", id = "+id+", created_at = "+created_at+"]";
    }
}


