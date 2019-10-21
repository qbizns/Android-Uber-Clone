package com.tatx.partnerapp.dataset;

/**
 * Created by Home on 26-04-2016.
 */
public class CommonRequestKey {
    private String requestparameters;

    private String requesterid;

    private String requestname;

    public String getRequestparameters ()
    {
        return requestparameters;
    }

    public void setRequestparameters (String requestparameters)
    {
        this.requestparameters = requestparameters;
    }

    public String getRequesterid ()
    {
        return requesterid;
    }

    public void setRequesterid (String requesterid)
    {
        this.requesterid = requesterid;
    }

    public String getRequestname ()
    {
        return requestname;
    }

    public void setRequestname (String requestname)
    {
        this.requestname = requestname;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [requestparameters = "+requestparameters+", requesterid = "+requesterid+", requestname = "+requestname+"]";
    }
}


