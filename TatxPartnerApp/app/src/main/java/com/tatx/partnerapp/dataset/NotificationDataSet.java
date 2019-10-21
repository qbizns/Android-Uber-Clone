package com.tatx.partnerapp.dataset;


public class NotificationDataSet {
    private String id;

    private String readstatus;

    private String timedate;

    private String description;

    private String data;

    private String code;

    private String messages;

    private String wayid;

    private String currentlocation;

    private String ownerid;

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }



    public String getCurrentlocation() {
        return currentlocation;
    }

    public void setCurrentlocation(String currentlocation) {
        this.currentlocation = currentlocation;
    }

    public String getWayid() {
        return wayid;
    }

    public void setWayid(String wayid) {
        this.wayid = wayid;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getReadstatus ()
    {
        return readstatus;
    }

    public void setReadstatus (String readstatus)
    {
        this.readstatus = readstatus;
    }

    public String getTimedate ()
    {
        return timedate;
    }

    public void setTimedate (String timedate)
    {
        this.timedate = timedate;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getData ()
    {
        return data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getMessages ()
    {
        return messages;
    }

    public void setMessages (String messages)
    {
        this.messages = messages;
    }


    @Override
    public String toString() {
        return "NotificationDataSet{" +
                "id='" + id + '\'' +
                ", readstatus='" + readstatus + '\'' +
                ", timedate='" + timedate + '\'' +
                ", description='" + description + '\'' +
                ", data='" + data + '\'' +
                ", code='" + code + '\'' +
                ", messages='" + messages + '\'' +
                ", wayid='" + wayid + '\'' +
                ", currentlocation='" + currentlocation + '\'' +
                ", ownerid='" + ownerid + '\'' +
                '}';
    }

}

