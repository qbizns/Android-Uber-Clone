package com.tatx.userapp.dataset;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Home on 12-05-2016.
 */
public class Drivers {
    private String price_per_minute;

    private String type;

    private int type_id;

    private String time;

    private String distance;

    private String min_price;

    private String base_fare;

    private String name;

    private int capacity;

    private int driver_id;

    private double longitude;

    private double latitude;

    private String vehicle_no;

    private String price_per_km;

    private Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getPrice_per_minute ()
    {
        return price_per_minute;
    }

    public void setPrice_per_minute (String price_per_minute)
    {
        this.price_per_minute = price_per_minute;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public int getType_id ()
    {
        return type_id;
    }

    public void setType_id (int type_id)
    {
        this.type_id = type_id;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getMin_price ()
    {
        return min_price;
    }

    public void setMin_price (String min_price)
    {
        this.min_price = min_price;
    }

    public String getBase_fare ()
    {
        return base_fare;
    }

    public void setBase_fare (String base_fare)
    {
        this.base_fare = base_fare;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public int getCapacity ()
    {
        return capacity;
    }

    public void setCapacity (int capacity)
    {
        this.capacity = capacity;
    }

    public int getDriver_id ()
    {
        return driver_id;
    }

    public void setDriver_id (int driver_id)
    {
        this.driver_id = driver_id;
    }

    public double getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (double latitude)
    {
        this.latitude = latitude;
    }

    public String getVehicle_no ()
    {
        return vehicle_no;
    }

    public void setVehicle_no (String vehicle_no)
    {
        this.vehicle_no = vehicle_no;
    }

    public String getPrice_per_km ()
    {
        return price_per_km;
    }

    public void setPrice_per_km (String price_per_km)
    {
        this.price_per_km = price_per_km;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [price_per_minute = "+price_per_minute+", type = "+type+", type_id = "+type_id+", time = "+time+", distance = "+distance+", min_price = "+min_price+", base_fare = "+base_fare+", name = "+name+", capacity = "+capacity+", driver_id = "+driver_id+", longitude = "+longitude+", latitude = "+latitude+", vehicle_no = "+vehicle_no+", price_per_km = "+price_per_km+"]";
    }
}

