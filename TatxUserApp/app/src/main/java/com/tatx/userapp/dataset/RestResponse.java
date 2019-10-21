package com.tatx.userapp.dataset;

/**
 * Created by Home on 26-04-2016.
 */
public class RestResponse {
    private String status;

    private Object data;

    private String code;

    private String requestname;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Object getData ()
    {
        return data;
    }

    public void setData (Object data)
    {
        this.data = data;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
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
        return "ClassPojo [status = "+status+", data = "+data+", code = "+code+", requestname = "+requestname+"]";
    }
}


