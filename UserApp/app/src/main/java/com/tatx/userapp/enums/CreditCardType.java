package com.tatx.userapp.enums;

import com.mobile.connect.payment.credit.PWCreditCardType;
import com.tatx.userapp.R;

/**
 * Created by user on 22-06-2016.
 */
public enum CreditCardType
{

    VISA("visa",1, R.drawable.visa,R.drawable.visacard, PWCreditCardType.VISA),
    MASTERCARD("mastercard",2, R.drawable.master,R.drawable.mastercard, PWCreditCardType.MASTERCARD),
    DINERS("diners",3, R.drawable.dinersbg,R.drawable.diners, PWCreditCardType.DINERS),
    JCB("jcb",4, R.drawable.jcbbg,R.drawable.maestro, PWCreditCardType.JCB),
    AMEX("amex",5, R.drawable.amexbg,R.drawable.amex, PWCreditCardType.AMEX);


    private final String cardName;
    private final int id;
    private final int cardDrawableSmall;
    private final int cardDrawableBig;
    private final PWCreditCardType pwCreditCardType;

    CreditCardType(String cardName, int id, int cardDrawableSmall, int cardDrawableBig, PWCreditCardType pwCreditCardType)
    {
        this.cardName = cardName;

        this.id = id;

        this.cardDrawableSmall = cardDrawableSmall;

        this.cardDrawableBig = cardDrawableBig;

        this.pwCreditCardType = pwCreditCardType;

    }


    public int getId() {
        return id;
    }

    public int getCardDrawableSmall() {
        return cardDrawableSmall;
    }

    public int getCardDrawableBig() { return cardDrawableBig; }

    public PWCreditCardType getPwCreditCardType() { return pwCreditCardType; }


}
