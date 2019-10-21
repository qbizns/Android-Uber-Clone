package com.tatx.partnerapp.dataset;

/**
 * Created by user on 01-06-2016.
 */
public class DisplayProfilePojo {

    private String phone_number;

    private String status;

    private String userid;

    private String password;

    private String erningday;

    private String country;

    private String first_name;

    private String earning;

    private Updated_at updated_at;

    private String email;

    private String avgrating;

    private String last_name;

    private Created_at created_at;

    public String getPhone_number ()
    {
        return phone_number;
    }

    public void setPhone_number (String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getUserid ()
    {
        return userid;
    }

    public void setUserid (String userid)
    {
        this.userid = userid;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getErningday ()
    {
        return erningday;
    }

    public void setErningday (String erningday)
    {
        this.erningday = erningday;
    }

  /*  public null getCountry ()
    {
        return country;
    }*/

  /*  public void setCountry (null country)
    {
        this.country = country;
    }
*/
    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getEarning ()
    {
        return earning;
    }

    public void setEarning (String earning)
    {
        this.earning = earning;
    }

    public Updated_at getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (Updated_at updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getAvgrating ()
    {
        return avgrating;
    }

    public void setAvgrating (String avgrating)
    {
        this.avgrating = avgrating;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public Created_at getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (Created_at created_at)
    {
        this.created_at = created_at;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [phone_number = "+phone_number+", status = "+status+", userid = "+userid+", password = "+password+", erningday = "+erningday+", country = "+country+", first_name = "+first_name+", earning = "+earning+", updated_at = "+updated_at+", email = "+email+", avgrating = "+avgrating+", last_name = "+last_name+", created_at = "+created_at+"]";
    }
}


