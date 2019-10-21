package com.tatx.partnerapp.dataset;

/**
 * Created by user on 28-05-2016.
 */
public class HelpPojo {
    private String id;

    private String updated_at;

    private String status;

    private String answer;

    private String created_at;

    private String role;

    private String question;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
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

    public String getAnswer ()
    {
        return answer;
    }

    public void setAnswer (String answer)
    {
        this.answer = answer;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getRole ()
    {
        return role;
    }

    public void setRole (String role)
    {
        this.role = role;
    }

    public String getQuestion ()
    {
        return question;
    }

    public void setQuestion (String question)
    {
        this.question = question;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", updated_at = "+updated_at+", status = "+status+", answer = "+answer+", created_at = "+created_at+", role = "+role+", question = "+question+"]";
    }
}

