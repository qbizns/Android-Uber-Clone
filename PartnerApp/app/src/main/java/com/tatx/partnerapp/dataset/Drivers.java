package com.tatx.partnerapp.dataset;

/**
 * Created by Home on 12-05-2016.
 */
public class Drivers {
    private String created_by;

    private String driver_license;

    private String national_id;

    private String status;

    private String vendor_id;

    private String id;

    private String distance;

    private String updated_at;

    private String age;

    private String dob;

    private String license_expiry;

    private String created_at;

    private String updated_by;

    private String gender;

    private String longitude;

    private String user_id;

    private String latitude;

    public String getCreated_by ()
    {
        return created_by;
    }

    public void setCreated_by (String created_by)
    {
        this.created_by = created_by;
    }

    public String getDriver_license ()
    {
        return driver_license;
    }

    public void setDriver_license (String driver_license)
    {
        this.driver_license = driver_license;
    }

    public String getNational_id ()
    {
        return national_id;
    }

    public void setNational_id (String national_id)
    {
        this.national_id = national_id;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getVendor_id ()
    {
        return vendor_id;
    }

    public void setVendor_id (String vendor_id)
    {
        this.vendor_id = vendor_id;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getAge ()
    {
        return age;
    }

    public void setAge (String age)
    {
        this.age = age;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String getLicense_expiry ()
    {
        return license_expiry;
    }

    public void setLicense_expiry (String license_expiry)
    {
        this.license_expiry = license_expiry;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getUpdated_by ()
    {
        return updated_by;
    }

    public void setUpdated_by (String updated_by)
    {
        this.updated_by = updated_by;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [created_by = "+created_by+", driver_license = "+driver_license+", national_id = "+national_id+", status = "+status+", vendor_id = "+vendor_id+", id = "+id+", distance = "+distance+", updated_at = "+updated_at+", age = "+age+", dob = "+dob+", license_expiry = "+license_expiry+", created_at = "+created_at+", updated_by = "+updated_by+", gender = "+gender+", longitude = "+longitude+", user_id = "+user_id+", latitude = "+latitude+"]";
    }
}

