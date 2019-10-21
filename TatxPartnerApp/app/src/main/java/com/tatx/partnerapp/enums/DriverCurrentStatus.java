package com.tatx.partnerapp.enums;


import java.util.HashMap;

public enum DriverCurrentStatus {

    OFFLINE(1),
    ONLINE(2),
    PICKING_UP_CUSTOMER(3),
    PICKED_UP_CUSTOMER(4),
    STARTED_TRIP(5),
    ENDED_TRIP(6);


    private final int id;

    DriverCurrentStatus(int id) {

        this.id = id;

    }


    static HashMap<Integer, DriverCurrentStatus> hashMap = new HashMap<Integer, DriverCurrentStatus>();


    static {

        for (DriverCurrentStatus driverCurrentStatus : DriverCurrentStatus.values()) {

            hashMap.put(driverCurrentStatus.id, driverCurrentStatus);

        }

    }


    public static DriverCurrentStatus getEnumField(int id)
    {

        return hashMap.get(id);

    }






}






