package com.tatx.userapp.dataset;

/**
 * Created by Home on 19-05-2016.
 */
public class OffersPojo {

    private String message;

    private String id;

    private String send_to_roles;

    private String updated_at;

    private String status;

    private String created_at;

    private String send_to;

    private String message_type;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getSend_to_roles ()
    {
        return send_to_roles;
    }

    public void setSend_to_roles (String send_to_roles)
    {
        this.send_to_roles = send_to_roles;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getSend_to ()
    {
        return send_to;
    }

    public void setSend_to (String send_to)
    {
        this.send_to = send_to;
    }

    public String getMessage_type ()
    {
        return message_type;
    }

    public void setMessage_type (String message_type)
    {
        this.message_type = message_type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", id = "+id+", send_to_roles = "+send_to_roles+", updated_at = "+updated_at+", status = "+status+", created_at = "+created_at+", send_to = "+send_to+", message_type = "+message_type+"]";
    }
}



