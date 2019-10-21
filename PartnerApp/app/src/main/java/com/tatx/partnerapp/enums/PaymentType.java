package com.tatx.partnerapp.enums;

/**
 * Created by user on 22-06-2016.
 */
public enum PaymentType
{

    CASH("CASH", 1),
    CREDIT_CARD("CREDIT CARD", 2),
    TATX_BALANCE("TATX BALANCE", 3);




    private final String string;
    private final int id;

    PaymentType(String string, int id)
    {

        this.string = string;
        this.id = id;

    }


    public int getId()
    {
        return id;
    }

    public String getString() {
        return string;
    }







}
