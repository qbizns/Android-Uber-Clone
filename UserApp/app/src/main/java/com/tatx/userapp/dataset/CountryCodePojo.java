package com.tatx.userapp.dataset;

/**
 * Created by Jithu on 3/10/2016.
 */
public class CountryCodePojo {

    private int id;
    private String countryCode;
    private String countryName ;
    private byte[] flag ;

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

