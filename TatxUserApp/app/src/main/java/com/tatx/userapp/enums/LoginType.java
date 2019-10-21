package com.tatx.userapp.enums;

import java.util.HashMap;

/**
 * Created by Venkateswarlu SKP on 03-09-2016.
 */
public enum LoginType
{



    NORMAL(1),
    SOCIAL_MEDIA(2);

    private final int id;



    LoginType(int id)
    {
        this.id = id;
    }

    public int getId() {
        return id;
    }



    static HashMap<Integer,LoginType> hashMap = new HashMap<Integer,LoginType>();

    static
    {
        for (LoginType loginType:values())
        {
            hashMap.put(loginType.getId(),loginType);
        }

    }


    public static LoginType getEnumField(int id)
    {
        return hashMap.get(id);
    }


}

