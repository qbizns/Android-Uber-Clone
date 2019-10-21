package com.tatx.partnerapp.dataset;

/**
 * Created by Home on 02-05-2016.
 */
public class LoginData {

    private String first_name;

    private Updated_at updated_at;

    private String phone_number;

    private String status;

    private String email;

    private String last_name;

    private String userid;



    private Created_at created_at;

    private String country;

    private String image_path;


    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public Updated_at getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (Updated_at updated_at)
    {
        this.updated_at = updated_at;
    }

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

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public String getUserid ()
    {
        return userid;
    }

    public void setUserid (String userid)
    {
        this.userid = userid;
    }

    public Created_at getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (Created_at created_at)
    {
        this.created_at = created_at;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [first_name = "+first_name+", updated_at = "+updated_at+", phone_number = "+phone_number+", status = "+status+", email = "+email+", last_name = "+last_name+", userid = "+userid+", created_at = "+created_at+", country = "+country+"]";
    }
}

