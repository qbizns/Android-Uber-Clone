package com.tatx.userapp.enums;

import com.mobile.connect.PWConnect;

import java.util.HashMap;

/**
 * Created by Venkateswarlu SKP on 04-01-2017.
 */

public enum Hyperpay
{


    TEST(PWConnect.PWProviderMode.TEST, "gate2play.CreativityApp.mcommerce.test", "8a858406ae924adba3c0de76ddb2abfc"),

    LIVE(PWConnect.PWProviderMode.LIVE, "gate2play.CreativityApp.mcommerce", "34284a2c6de611e6a91b398965e57753");


    private final PWConnect.PWProviderMode pwProviderMode;

    private final String applicationIdentifier;

    private final String profileToken;



    Hyperpay(PWConnect.PWProviderMode pwProviderMode, String applicationIdentifier, String profileToken)
    {

        this.pwProviderMode = pwProviderMode;
        this.applicationIdentifier = applicationIdentifier;
        this.profileToken = profileToken;

    }


    public PWConnect.PWProviderMode getPwProviderMode()
    {
        return pwProviderMode;
    }


    public String getApplicationIdentifier()
    {
        return applicationIdentifier;
    }


    public String getProfileToken()
    {
        return profileToken;
    }




}
