package com.tatx.partnerapp.enums;

import java.util.HashMap;

/**
 * Created by Venkateswarlu SKP on 25-08-2016.
 */
public enum DensityType
{

    LDPI(0.75,"ldpi"),
    MDPI(1.0,"mdpi"),
    HDPI(1.5,"hdpi"),
    XHDPI(2.0,"xhdpi"),
    XXDPI(3.0,"xxdpi");


    private final double densityValue;
    private final String densityString;


    DensityType(double densityValue, String densityString)
    {

        this.densityValue = densityValue;

        this.densityString = densityString;


    }


    public double getDensityValue()
    {
        return densityValue;
    }



    public String getDensityString()
    {
        return densityString;
    }


    static HashMap<Double,DensityType> hashMap = new HashMap<Double,DensityType>();

    static
    {

        for (DensityType densityType:DensityType.values())
        {

            hashMap.put(densityType.getDensityValue(),densityType);

        }

    }



    public static DensityType getEnumField(double screenDensity)
    {

        return hashMap.get(screenDensity);

    }






}
