package com.tatx.userapp.enums;

import java.util.HashMap;

/**
 * Created by Venkateswarlu SKP on 04-01-2017.
 */

public enum Environment
{


    DEV("DEV ENVIRONMENT", "http://dev.tatx.com/dev/backend/public", "ws://dev.tatx.com:8887", Hyperpay.TEST),

    TEST("TEST ENVIRONMENT", "http://dev.tatx.com/test/backend/public", "ws://dev.tatx.com:8889", Hyperpay.TEST),

    PRODUCTION("PRODUCTION ENVIRONMENT", "http://web.tatx.com/backend/public", "ws://web.tatx.com:8889", Hyperpay.LIVE);


    private final String environmentType;
    private final String apiUrl;
    private final String socketUrl;
    private final Hyperpay hyperpay;


    Environment(String environmentType, String apiUrl, String socketUrl, Hyperpay hyperpay)
    {

        this.environmentType = environmentType;

        this.apiUrl = apiUrl;

        this.socketUrl = socketUrl;

        this.hyperpay = hyperpay;


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

    public Hyperpay getHyperpay()
    {
        return hyperpay;
    }



}
