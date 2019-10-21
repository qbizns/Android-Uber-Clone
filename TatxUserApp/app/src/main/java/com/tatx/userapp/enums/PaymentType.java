package com.tatx.userapp.enums;

import com.tatx.userapp.R;

import java.util.HashMap;

/**
 * Created by user on 22-06-2016.
 */
public enum PaymentType
{

/*

    CASH("CASH", 1, R.drawable.pt_dollor_icon),
    CREDIT_CARD("CREDIT\nCARD", 2, R.drawable.pt_creditcard),
    TATX_BALANCE("TATX\nBALANCE", 3, R.drawable.pt_coin);
*/

    CASH("CASH", 1, R.drawable.cash,R.string.cash),
    CREDIT_CARD("CREDIT CARD", 2, R.drawable.card,R.string.credit_card),
    TATX_BALANCE("TATX BALANCE", 3, R.drawable.logo,R.string.tatx_balance);




    private final String string;
    private final int id;
    private final int backgroundDrawableId;
    private final int stringId;

    PaymentType(String string, int id, int backgroundDrawableId,int stringId)
    {

        this.string = string;
        this.id = id;
        this.backgroundDrawableId = backgroundDrawableId;
        this.stringId=stringId;

    }


    public int getId()
    {
        return id;
    }

    public String getString() {
        return string;
    }


    public int getBackgroundDrawableId() {
        return backgroundDrawableId;
    }

    public int getStringId() {
        return stringId;
    }

    static HashMap<Integer,PaymentType> paymentTypeHashMap = new HashMap<Integer,PaymentType>();


    static
    {

        for (PaymentType paymentType:values())
        {

            paymentTypeHashMap.put(paymentType.id,paymentType);


        }


    }


    public static PaymentType getEnumFieldById(int id) {
        return paymentTypeHashMap.get(id);

    }


}
