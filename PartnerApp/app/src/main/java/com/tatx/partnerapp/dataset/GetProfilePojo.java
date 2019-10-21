package com.tatx.partnerapp.dataset;

/**
 * Created by user on 20-06-2016.
 */
public class GetProfilePojo {
    private String model;

    private String phone_number;

    private String image;

    private Type[] type;

    private String country;

    private String erningday;

    private String first_name;

    private String earning;

    private String avgacceptance;

    private String email;

    private String avgrating;

    private String last_name;

    private String created_at;

    private String make;

    public String getModel ()
    {
        return model;
    }

    public void setModel (String model)
    {
        this.model = model;
    }

    public String getPhone_number ()
    {
        return phone_number;
    }

    public void setPhone_number (String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public Type[] getType ()
    {
        return type;
    }

    public void setType (Type[] type)
    {
        this.type = type;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getErningday ()
    {
        return erningday;
    }

    public void setErningday (String erningday)
    {
        this.erningday = erningday;
    }

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

    public String getAvgacceptance ()
    {
        return avgacceptance;
    }

    public void setAvgacceptance (String avgacceptance)
    {
        this.avgacceptance = avgacceptance;
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

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getMake ()
    {
        return make;
    }

    public void setMake (String make)
    {
        this.make = make;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [model = "+model+", phone_number = "+phone_number+", image = "+image+", type = "+type+", country = "+country+", erningday = "+erningday+", first_name = "+first_name+", earning = "+earning+", avgacceptance = "+avgacceptance+", email = "+email+", avgrating = "+avgrating+", last_name = "+last_name+", created_at = "+created_at+", make = "+make+"]";
    }


}
