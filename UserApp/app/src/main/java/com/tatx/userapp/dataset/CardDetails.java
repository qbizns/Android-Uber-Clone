package com.tatx.userapp.dataset;

/**
 * Created by Home on 12-05-2016.
 */
public class CardDetails {
    private String account_holder_name;



    private int card_number;

    private String exp_date;


    public String getAccount_holder_name() {
        return account_holder_name;
    }

    public void setAccount_holder_name(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public int getCard_number() {
        return card_number;
    }

    public void setCard_number(int card_number) {
        this.card_number = card_number;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }


    @Override
    public String toString() {
        return "CardDetails{" +
                "account_holder_name='" + account_holder_name + '\'' +
                ", card_number=" + card_number +
                ", exp_date='" + exp_date + '\'' +
                '}';
    }

}

