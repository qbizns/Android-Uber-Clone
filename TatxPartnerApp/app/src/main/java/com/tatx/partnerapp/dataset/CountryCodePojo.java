package com.tatx.partnerapp.dataset;

/**
 * Created by Jithu on 3/10/2016.
 */
public class CountryCodePojo {

    public int id;
    public String countryCode;
    public String countryName ;
    public byte[] flag ;

    public byte[] getFlag() {
        return flag;
    }

    public void setFlag(byte[] flag) {
        this.flag = flag;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }




}

