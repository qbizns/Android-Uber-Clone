package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 09-09-2016.
 */
public class CreditBalanceTransactionVo implements Serializable {

    @SerializedName("transactions")
    @Expose
    public List<Transactions> transactions = new ArrayList<>();

}
