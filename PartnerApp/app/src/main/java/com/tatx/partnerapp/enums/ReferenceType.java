package com.tatx.partnerapp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venkateswarlu SKP on 25-08-2016.
 */
public enum ReferenceType
{

    SOCIALMEDIA(1,"Social Media"),
    GOOGLESEARCH(2,"Google Search"),
    TATXEMPLOYEE(3,"Tatx Employee"),
    OTHERS(4,"Others");


    private final int referenceId;

    private final String referenceName;


    ReferenceType(int referenceId, String referenceName)
    {

        this.referenceId = referenceId;

        this.referenceName = referenceName;


    }



    public String getReferenceName() {
        return referenceName;
    }

    public int getReferenceId() {
        return referenceId;
    }



    static List< ReferenceType> list = new ArrayList<>();


    static {

        for (ReferenceType referenceType : ReferenceType.values()) {

            list.add(referenceType);

        }

    }


    public static List<ReferenceType> getList() {
        return list;
    }
}
