package com.tatx.userapp.dataset;

public class UserLocations {
    private String LocName;

    private String ZoomLevel;

    private String ID;

    private String LocLat;

    private String LocLong;

    private String LocAddr;

    public String getLocName ()
    {
        return LocName;
    }

    public void setLocName (String LocName)
    {
        this.LocName = LocName;
    }

    public String getZoomLevel ()
    {
        return ZoomLevel;
    }

    public void setZoomLevel (String ZoomLevel)
    {
        this.ZoomLevel = ZoomLevel;
    }

    public String getID ()
    {
        return ID;
    }

    public void setID (String ID)
    {
        this.ID = ID;
    }

    public String getLocLat ()
    {
        return LocLat;
    }

    public void setLocLat (String LocLat)
    {
        this.LocLat = LocLat;
    }

    public String getLocLong ()
    {
        return LocLong;
    }

    public void setLocLong (String LocLong)
    {
        this.LocLong = LocLong;
    }

    public String getLocAddr ()
    {
        return LocAddr;
    }

    public void setLocAddr (String LocAddr)
    {
        this.LocAddr = LocAddr;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [LocName = "+LocName+", ZoomLevel = "+ZoomLevel+", ID = "+ID+", LocLat = "+LocLat+", LocLong = "+LocLong+", LocAddr = "+LocAddr+"]";
    }
}

