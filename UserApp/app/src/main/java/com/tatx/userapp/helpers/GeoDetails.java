package com.tatx.userapp.helpers;

/**
 * Created by Home on 17-11-2016.
 */
public class GeoDetails {

    private final String address;
    private final String completeAddress;
    private final String country;
    private final String cityName;

    public String getCityName() {
        return cityName;
    }

    public String getAddress() {
        return address;
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public String getCountry() {
        return country;
    }

    public GeoDetails(String address, String completeAddress, String country,String cityName)
    {

        this.address = address;
        this.completeAddress = completeAddress;
        this.country = country;
        this.cityName=cityName;

    }


    @Override
    public String toString() {
        return "GeoDetails{" +
                "address='" + address + '\'' +
                ", completeAddress='" + completeAddress + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
