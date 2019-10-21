package com.tatx.userapp.menuactivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.activities.AddCreditBalanceActivity;
import com.tatx.userapp.activities.AddCreditCardActivity;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.activities.LoyalityDetailsActivity;
import com.tatx.userapp.adapter.CustomRecyclerViewAdapterCreditCards;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.enums.PaymentType;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.AccountsCustomerVo;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.UpdatePaymentTypeVo;

import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends BaseActivity implements RetrofitResponseListener
{


    @BindView(R.id.tv_tatx_balance) TextView tvTatxBalance;

    @BindView(R.id.tv_loyalty_points) TextView tvLoyaltyPoints;

    @BindView(R.id.rl_add_credit_card) RelativeLayout rlAddCreditCard;

    @BindView(R.id.loyaltiicon_click) RelativeLayout loyaltiiconClick;

    @BindView(R.id.ll_cards_layout) LinearLayout llCardsLayout;

    @BindView(R.id.et_payee_id) EditText etPayeeId;

    @BindView(R.id.et_amount_to_credit) EditText etAmountToCredit;

    @BindView(R.id.tv_payment_type) TextView tvPaymentType;

    @BindView(R.id.loyaltiicon) ImageView loyaltiIcon;

    @BindView(R.id.rv_credit_cards) RecyclerView rvCreditCards;

    @BindView(R.id.tv_currency) TextView tvCurrency;


    private AccountsCustomerVo accountsCustomerVo;

    private int icon=R.drawable.ic_launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accounts);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.accounts));

        Common.showContentView(this,false);


//        llCardsLayout.setVisibility(View.GONE);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER,null);


    }





    private void sendChangeCardRequest(int viewId, String cardId)
    {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put(ServiceUrls.RequestParams.CARD_ID, cardId);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CHANGE_CARD,params,viewId);


    }






    private void showAddCreditCardConfirmationDialog(String titleText, String messageText, final Class<?> activityToStart)
    {



        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);

//        builder.setTitle("No Cards were Added");
        builder.setTitle(titleText);

//        builder.setMessage("Please Add Card to process.\n\nDo You Want to Add Credit Card ?");
        builder.setMessage(messageText);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

//                startActivity(new Intent(AccountActivity.this,AddCreditCardActivity.class));
                startActivity(new Intent(AccountActivity.this,activityToStart));

            }



        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });



        builder.create().show();


    }





    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {

        if (apiResponseVo.code != Constants.SUCCESS)
        {

            Common.customToast(this, apiResponseVo.status);

            switch(apiResponseVo.requestname)
            {
                case ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER:
                    finish();
                    break;
            }




            return;


        }


        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER:

//                AccountsCustomer accountsCustomerVo = Common.getSpecificDataObject(apiResponseVo.data, AccountsCustomer.class);
                accountsCustomerVo = Common.getSpecificDataObject(apiResponseVo.data, AccountsCustomerVo.class);

            if (accountsCustomerVo.credits<0&&accountsCustomerVo.paymentTypeId!=PaymentType.CASH.getId())
            {
                callUpdatePaymetAPI(String.valueOf(PaymentType.CASH.getId()));

            }
                 icon=R.drawable.ic_launcher;

                switch (accountsCustomerVo.loyality)
                {

                    case Constants.LoyalityTypes.BRONZE:
                        icon=R.drawable.loyalty_bronz;
                        break;

                    case Constants.LoyalityTypes.SILVER:
                        icon=R.drawable.loyalty_silver;
                        break;

                    case Constants.LoyalityTypes.GOLD:
                        icon=R.drawable.loyalty_gold;
                        break;

                    case Constants.LoyalityTypes.DIAMOND:
                        icon=R.drawable.loyalty_diamond;
                        break;

                    case Constants.LoyalityTypes.LOYALTY_ZERO:
                        icon=R.drawable.ic_launcher;
                        break;


                }

                loyaltiIcon.setBackgroundResource(icon);
                tvTatxBalance.setText(String.valueOf(accountsCustomerVo.credits));
                tvLoyaltyPoints.setText(accountsCustomerVo.points+" PT");
                tvCurrency.setText(accountsCustomerVo.currencyCode);

               /* switch (accountsCustomerVo.cards.size())
                {

                    case 0:

                        rlAddCreditCard.setVisibility(View.VISIBLE);

                        break;

                    case 1:

                        ivCardImageOneSingleLayout.setBackgroundResource(Common.getCardBackgroundById(Integer.parseInt(accountsCustomerVo.cards.get(0).brand)).getCardDrawableBig());
                        tvCardOneNumberSingleLayout.setText(accountsCustomerVo.cards.get(0).number);
                        rlAddCreditCard.setVisibility(View.VISIBLE);
                        rlSingleCardLayout.setVisibility(View.VISIBLE);
                        rlSingleCardLayout.setTag(accountsCustomerVo.cards.get(0).id);

                        break;

                    case 2:

                        ivCardImageOneDoubleLayout.setBackgroundResource(Common.getCardBackgroundById(Integer.parseInt(accountsCustomerVo.cards.get(0).brand)).getCardDrawableBig());
                        tvCardOneNumberDoubleLayout.setText(accountsCustomerVo.cards.get(0).number);
                        rbCardOneDoubleLayout.setChecked(Integer.parseInt(accountsCustomerVo.cards.get(0).primary) == 1);
                        rlDoubleCardLayoutOne.setTag(accountsCustomerVo.cards.get(0).id);

                        ivCardImageTwoDoubleLayout.setBackgroundResource(Common.getCardBackgroundById(Integer.parseInt(accountsCustomerVo.cards.get(1).brand)).getCardDrawableBig());
                        tvCardOneTwoDoubleLayout.setText(accountsCustomerVo.cards.get(1).number);
                        rbCardTwoDoubleLayout.setChecked(Integer.parseInt(accountsCustomerVo.cards.get(1).primary) == 1);
                        rlDoubleCardLayoutTwo.setTag(accountsCustomerVo.cards.get(1).id);

                        llCardsLayout.setVisibility(View.VISIBLE);

                        break;



                }*/

                Common.Log.i("? - accountsCustomerVo.cards : "+accountsCustomerVo.cards);

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rvCreditCards.setLayoutManager(mLayoutManager);
//                rvCreditCards.setItemAnimator(new DefaultItemAnimator());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCreditCards.getContext(), mLayoutManager.getOrientation());
                rvCreditCards.addItemDecoration(dividerItemDecoration);
                rvCreditCards.setAdapter(new CustomRecyclerViewAdapterCreditCards(this,accountsCustomerVo.cards));






//                tvPaymentType.setText(Common.getPaymentTypeById(Integer.parseInt(accountsCustomerVo.paymentTypeId)).getString());

                tvPaymentType.setBackgroundResource(Common.getPaymentTypeById(accountsCustomerVo.paymentTypeId).getBackgroundDrawableId());


                Common.showContentView(this,true);

                break;

            case ServiceUrls.RequestNames.CHANGE_CARD:

                /*switch (requestId)
                {
                    case R.id.rl_double_card_layout_one:
                        checkRadioButton(rbCardOneDoubleLayout);

                        break;


                    case R.id.rl_double_card_layout_two:
                        checkRadioButton(rbCardTwoDoubleLayout);
                        break;

                }*/



                Common.refreshActivity(this);

                break;


            case ServiceUrls.RequestNames.DELETE_CARD:

                Common.customToast(this, apiResponseVo.status);

                Common.refreshActivity(this);

                break;




            case ServiceUrls.RequestNames.UPDATE_PAYMENT_TYPE:


                UpdatePaymentTypeVo updatePaymentTypeVo = Common.getSpecificDataObject(apiResponseVo.data, UpdatePaymentTypeVo.class);


                Common.Log.i("Common.getPaymentTypeById(Integer.parseInt(paymentType)).name() : "+Common.getPaymentTypeById(Integer.parseInt(updatePaymentTypeVo.payment)).name());

//                tvPaymentType.setText(Common.getPaymentTypeById(Integer.parseInt(updatePaymentTypeVo.payment)).getString());

                tvPaymentType.setBackgroundResource(Common.getPaymentTypeById(Integer.parseInt(updatePaymentTypeVo.payment)).getBackgroundDrawableId());





//                    Common.getDefaultSP(this).edit().putInt(ServiceUrls.RequestParams.PAYMENT_TYPE, Integer.parseInt(updatePaymentTypeVo.payment)).commit();

                break;


            case ServiceUrls.RequestNames.TRANSFER_CREDITS:
                Common.customToast(this, apiResponseVo.status);
                Common.refreshActivity(this);
                break;



        }





    }





    @OnClick(R.id.tv_transefer)
    public void transferFunctionality()
    {



        Common.hideSoftKeyboard(this);

        if (isValidData())
        {

            HashMap<String, String> hashMap = new HashMap<String, String>();

            hashMap.put(ServiceUrls.RequestParams.EMAIL, etPayeeId.getText().toString());

            hashMap.put(ServiceUrls.RequestParams.AMOUNT, etAmountToCredit.getText().toString());
            //disable balance transfer
            hashMap.put(ServiceUrls.RequestParams.DISABLE, "0");

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.TRANSFER_CREDITS,hashMap);

        }


    }




    private boolean isValidData()
    {

        if(TextUtils.isEmpty(etPayeeId.getText()))
        {
            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_sender_details));
            return false;
        }
        else if(TextUtils.isEmpty(etAmountToCredit.getText()))
        {
            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_amount_to_credit));
            return false;
        }

        return true;
    }




    @OnClick(R.id.tv_add_tatx_balance)
    void addTatxBalance()
    {
//        Common.setPreviousActivity(this);
//        startActivity(new Intent(this,AddCreditBalanceActivity.class));
    }

    @OnClick(R.id.tv_add_credit_card)
    void addCreditCard()
    {

//        Common.setPreviousActivity(this);
//        startActivity(new Intent(this,AddCreditCardActivity.class));

    }


/*
    @OnClick(R.id.rl_double_card_layout_one)
    void cradOneClickedFunctionality()
    {
        sendChangeCardRequest(R.id.rl_double_card_layout_one, String.valueOf(rlDoubleCardLayoutOne.getTag()));

    }
*/
/*

    @OnClick(R.id.rl_double_card_layout_two)
    void cradTwoClickedFunctionality()
    {
        sendChangeCardRequest(R.id.rl_double_card_layout_two, String.valueOf(rlDoubleCardLayoutTwo.getTag()));

    }*/
@OnClick(R.id.loyaltiicon_click) void setLoyaltiiconClick(){
    Intent intent=new Intent(this, LoyalityDetailsActivity.class);
    intent.putExtra(Constants.IntentKeys.LOYALITY_POINT, accountsCustomerVo.loyality);
    intent.putExtra(Constants.IntentKeys.LOYALITY_TYPE,icon);
    intent.putExtra(Constants.IntentKeys.LOYALITY_TEXT, accountsCustomerVo.levelDesc);
    startActivity(intent);
    finish();
}
/*
    private void checkRadioButton(RadioButton radioButton)
    {

//        rbCardOneDoubleLayout.setChecked(false);
        rbCardTwoDoubleLayout.setChecked(false);

        radioButton.setChecked(true);

    }
*/



    public void longPressFunctionality(final View view)
    {

        Common.Log.i("Inside longPressFunctionality() method.");


        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);

        builder.setMessage(Common.getStringResourceText(R.string.do_you_want_to_delete_this_card));

        builder.setPositiveButton(Common.getStringResourceText(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                HashMap<String, String> hashMap = new HashMap<String, String>();

                Common.Log.i("String.valueOf(view.getTag()) : "+String.valueOf(view.getTag()));

                hashMap.put(ServiceUrls.RequestParams.CARD_ID, String.valueOf(view.getTag()));


                new RetrofitRequester(AccountActivity.this).sendStringRequest(ServiceUrls.RequestNames.DELETE_CARD,hashMap);


            }



        });


        builder.setNegativeButton(Common.getStringResourceText(R.string.no), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });



        builder.create().show();






    }


    @OnClick(R.id.ll_payment_type)
    void showPaymentOptionPopup()
    {

        final TextView tvOnline, tvCash, tvCreditBalance;

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.payment_option_popup);

        tvOnline = (TextView) dialog.findViewById(R.id.online);

        tvCash = (TextView) dialog.findViewById(R.id.cash);

        tvCreditBalance = (TextView) dialog.findViewById(R.id.credits);



        tvOnline.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {



                if(accountsCustomerVo.cards.size()!= 0)
                {
                    if (accountsCustomerVo.credits>=0)
                    {
                        callUpdatePaymetAPI(String.valueOf(PaymentType.CREDIT_CARD.getId()));
                    }
                    else
                    {

                       Common.customToast(AccountActivity.this,Common.getStringResourceText(R.string.as_of_now_this_feature_is_not_available));

  //                      Common.showNegativeTatxBalanceErrorDialog(AccountActivity.this,String.valueOf(accountsCustomerVo.credits));



                    }

                }
                else
                {
                    Common.customToast(AccountActivity.this,Common.getStringResourceText(R.string.as_of_now_this_feature_is_not_available));

                  //  showAddCreditCardConfirmationDialog(Common.getStringResourceText(R.string.no_cards_were_added), Common.getStringResourceText(R.string.no_card_was_added_try_again), AddCreditCardActivity.class);



                }

                dialog.cancel();



            }




        });


        tvCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callUpdatePaymetAPI(String.valueOf(PaymentType.CASH.getId()));

                dialog.cancel();


            }
        });


        tvCreditBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if(Double.parseDouble(tvCreditBalance.getText().toString()) > 100)
                if(accountsCustomerVo.credits > 1)
                {

                    callUpdatePaymetAPI(String.valueOf(PaymentType.TATX_BALANCE.getId()));

                }
                else
                {

                  //  showAddCreditCardConfirmationDialog(Common.getStringResourceText(R.string.insufficient_tatx_balance), Common.getStringResourceText(R.string.please_add_tatx_balance_to_process_do_you_want_to_add_tatx_balance), AddCreditBalanceActivity.class);

                }


                dialog.cancel();


            }
        });


        dialog.setCancelable(true);


        dialog.show();


    }

    private void callUpdatePaymetAPI(String paymentType)
    {

        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put(ServiceUrls.RequestParams.PAYMENT_TYPE, paymentType);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.UPDATE_PAYMENT_TYPE,hashMap);

    }


}
