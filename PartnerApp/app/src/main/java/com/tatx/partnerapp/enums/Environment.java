package com.tatx.partnerapp.enums;

/**
 * Created by Venkateswarlu SKP on 04-01-2017.
 */

public enum Environment
{


    DEV("DEV ENVIRONMENT", "http://dev.tatx.com/dev/backend/public", "ws://dev.tatx.com:8887"),

    TEST("TEST ENVIRONMENT", "http://dev.tatx.com/test/backend/public", "ws://dev.tatx.com:8889"),

    PRODUCTION("PRODUCTION ENVIRONMENT", "http://web.tatx.com/backend/public", "ws://web.tatx.com:8889");


    private final String environmentType;
    private final String apiUrl;
    private final String socketUrl;


    Environment(String environmentType, String apiUrl, String socketUrl)
    {

        this.environmentType = environmentType;

        this.apiUrl = apiUrl;

        this.socketUrl = socketUrl;




    }

    public String getEnvironmentType()
    {
        return environmentType;
    }

    public String getApiUrl()
    {
        return apiUrl;
    }

    public String getSocketUrl()
    {
        return socketUrl;
    }





}
