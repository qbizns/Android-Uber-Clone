package com.tatx.userapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.AccountsCustomerVo;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.Card;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Home on 18-05-2016.
 */
public class AddCreditBalanceActivity extends BaseActivity implements  RetrofitResponseListener {

    @BindView(R.id.et_amount_to_add) EditText etAmountToAdd;
    @BindView(R.id.tv_buy_credit_balance) TextView tv_buy_credit_balance;
    @BindView(R.id.tv_credit_balance) TextView tv_credit_balance;
    private int credits;
    private AccountsCustomerVo accountsCustomerVo;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_credit_balance);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.add_credit_balance));

        initialaziedAll();

        Common.showContentView(this,false);


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER,null);


    }

    public void initialaziedAll()
    {

        Intent intent=getIntent();
        if (intent.hasExtra("credits")){
            credits=intent.getIntExtra("credits",0);
        }

        tv_credit_balance.setText(""+credits);


    }

    @OnClick(R.id.tv_buy_credit_balance) void buyCredits(){

        if (!TextUtils.isEmpty(etAmountToAdd.getText()))
        {
            callAddCreditBalanceAPI();
        }
        else
        {

            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_amount));


        }

    }






    private void callAddCreditBalanceAPI()
    {


        HashMap<String, String> hashMap = new HashMap<String, String>();

//        hashMap.put(Constants.RequestParams.PAYMENT_TYPE, "1");

        hashMap.put(ServiceUrls.RequestParams.AMOUNT, etAmountToAdd.getText().toString());

//        int cardId = Common.getDefaultSP(this).getInt(Constants.SharedPreferencesKeys.PRIMARY_CARD_ID, 0);


        if(accountsCustomerVo.cards.size() == 0)
        {
            Common.customToast(this,Common.getStringResourceText(R.string.no_card_was_added_please_add_card_and_try_again));
            return;
        }


        int cardId = 0;

        for (Card card: accountsCustomerVo.cards)
        {

            if(Integer.parseInt(card.primary) == 1)
            {
                cardId = card.id;
            }

        }


        if(cardId == 0)
        {
            Common.customToast(this,Common.getStringResourceText(R.string.no_primary_card_available_please));
            return;
        }

        hashMap.put(ServiceUrls.RequestParams.CARD_ID, String.valueOf(cardId));

        new RetrofitRequester(AddCreditBalanceActivity.this).sendStringRequest(ServiceUrls.RequestNames.ADD_CREDIT,hashMap);
    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {

        if (apiResponseVo.code != Constants.SUCCESS)
        {

            Common.customToast(this, apiResponseVo.status);

            switch(apiResponseVo.requestname)
            {
                case ServiceUrls.RequestNames.ADD_CREDIT:
//                    AddCreditCardActivity.finishAndStartAccountActivity(this);
                   finish();
                    break;
            }




            return;


        }



        switch (apiResponseVo.requestname)
        {


            case ServiceUrls.RequestNames.ADD_CREDIT:
                AddCreditCardActivity.finishAndStartAccountActivity(this);
                finish();

                break;

            case ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER:
                accountsCustomerVo = Common.getSpecificDataObject(apiResponseVo.data, AccountsCustomerVo.class);
                tv_credit_balance.setText(""+accountsCustomerVo.credits);
                Common.showContentView(this,true);
                break;
        }



    }

}
