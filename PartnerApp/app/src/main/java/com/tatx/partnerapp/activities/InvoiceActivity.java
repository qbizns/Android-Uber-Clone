package com.tatx.partnerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.NetworkChangeListenerActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.enums.PaymentType;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.AddToUserTatxBalanceVo;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.DriverOnTripVo;
import com.tatx.partnerapp.pojos.EndTripVo;


public class InvoiceActivity extends NetworkChangeListenerActivity implements RetrofitResponseListener
{

    private int lastTripStatus;

    private EndTripVo endTripVo;


    @BindView(R.id.tv_pick_address)
    TextView tvPickAddress;

    @BindView(R.id.tv_destination_address)
    TextView tvDestinationAddress;

    @BindView(R.id.tv_trip_amount)
    TextView tvTripAmount;

    @BindView(R.id.tv_distance)
    TextView tvDistance;

    @BindView(R.id.tv_duration)
    TextView tvDuration;

    @BindView(R.id.rb_rating_to_user)
    RatingBar rbRatingToUser;


    @BindView(R.id.tv_cash_to_collect) TextView tvCashToCollect;
    @BindView(R.id.tv_collect_cash) TextView tvCollectCash;

    @BindView(R.id.tv_discount)
    TextView tvDiscount;

    @BindView(R.id.payment_type)
    TextView paymentType;

    @BindView(R.id.tv_adjestments)
    TextView tvAdjestments;

    @BindView(R.id.tv_current_amount) TextView tvCurrentAmount;

    @BindView(R.id.tv_previous_amount) TextView tvPreviousAmount;

    @BindView(R.id.tv_your_share) TextView tvYourShare;

    @BindView(R.id.btn_submit_invoice) Button submitInvoice;

    @BindView(R.id.cet_collected_cash) EditText cetCollectedCash;

    public static InvoiceActivity getInstance() {
        return instance;
    }

    private static InvoiceActivity instance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_invoice);

        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.invoice));

        instance=this;
        
        endTripVo = (EndTripVo) getIntent().getSerializableExtra(Constants.KEY_1);
        Common.Log.i("endTripVo 12"+endTripVo.toString());

        if (endTripVo.paymentType== PaymentType.CREDIT_CARD.getId() || endTripVo.collectCash==0){
            cetCollectedCash.setVisibility(View.GONE);

    }

        updateUI(" "+endTripVo.currency);



    }

    private void updateUI(String currency){
        lastTripStatus = getIntent().getIntExtra(Constants.IntentKeys.LAST_TRIP, 1);

        tvPickAddress.setText(Common.getCompleteAddressString(this,endTripVo.pickLat,endTripVo.pickLng));

        tvDestinationAddress.setText(Common.getCompleteAddressString(this,endTripVo.dropLat,endTripVo.dropLng));

        tvDistance.setText(" "+String.valueOf(endTripVo.distance));

        tvDuration.setText(" "+String.valueOf(endTripVo.duration));

        tvTripAmount.setText(String.valueOf(endTripVo.amount)+currency);

        tvCollectCash.setText(String.valueOf(endTripVo.collectCash)+currency);

        tvDiscount.setText(" "+String.valueOf(endTripVo.discount)+currency);

        paymentType.setText(" "+Common.getPaymentTypeById(endTripVo.paymentType).getString());

        tvAdjestments.setText(" "+String.valueOf(endTripVo.adjustmentAmount)+currency);

        tvCurrentAmount.setText(String.valueOf(Math.round(endTripVo.tripAmount)+currency));

        tvPreviousAmount.setText(String.valueOf(endTripVo.adjustmentAmount)+currency);

        tvYourShare.setText(endTripVo.driverShare+" "+currency);
    }


    @OnClick(R.id.btn_submit_invoice)
    void btnSubmitInvoiceClickFunctionality()
    {

        if (cetCollectedCash.getVisibility()==View.VISIBLE && TextUtils.isEmpty(cetCollectedCash.getText()) && rbRatingToUser.getRating() == 0)
        {
            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_collected_amount));
        }
        else if(cetCollectedCash.getVisibility()==View.VISIBLE && TextUtils.isEmpty(cetCollectedCash.getText()))
        {
            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_collected_amount1));

        }
        else if(rbRatingToUser.getRating() == 0)
        {
            Common.customToast(this,Common.getStringResourceText(R.string.please_give_rating));

        }

        else
        {

            submitInvoice.setClickable(false);

            HashMap<String, String> params = new HashMap<>();
            params.put(ServiceUrls.ApiRequestParams.TRIPID, endTripVo.tripId);
//            params.put(ServiceUrls.ApiRequestParams.AMOUNT, String.valueOf(amountToAdd - cashToCollect));
            params.put(ServiceUrls.ApiRequestParams.COLLECTED_AMOUNT, cetCollectedCash.getText().toString());

            new RetrofitRequester(InvoiceActivity.this).sendStringRequest(ServiceUrls.RequestNames.ADD_TO_USER_TATX_BALANCE,params);

        }




    }





    @Override
    public void onBackPressed()
    {
        Common.customToast(this,Common.getStringFromResources(R.string.please_rate_first),Common.TOAST_TIME);
        return;


    }

    @Override
    protected void onTitleBackPressed() {
        Common.customToast(this,Common.getStringFromResources(R.string.please_rate_first),Common.TOAST_TIME);
        return;
    }

    /*
    @OnClick(R.id.tv_cash_to_collect)
    void rechageTatxBalance()
    {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recharge_tatx_balance);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        final EditText cetRechargeTatxBalace = (EditText) dialog.findViewById(R.id.cet_recharge_tatx_balace);

        TextView tvCollectButton = (TextView) dialog.findViewById(R.id.tv_collect_button);

        TextView tvCancelButton = (TextView) dialog.findViewById(R.id.tv_cancel_button);


        tvCollectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                double amountToAdd = Double.parseDouble(cetRechargeTatxBalace.getText().toString());
                double cashToCollect = Double.parseDouble(tvCollectCash.getText().toString());

                if(amountToAdd < cashToCollect)
                {

                    Common.customToast(InvoiceActivity.this,"Amount Should not be less than Trip Amount.");

                    return;

                }

                HashMap<String, String> params = new HashMap<>();
                params.put(ServiceUrls.ApiRequestParams.TRIPID, endTripVo.tripId);
                params.put(ServiceUrls.ApiRequestParams.AMOUNT, String.valueOf(amountToAdd - cashToCollect));

                new RetrofitRequester(InvoiceActivity.this).sendStringRequest(ServiceUrls.RequestNames.ADD_TO_USER_TATX_BALANCE,params);

                dialog.dismiss();



            }
        });


        tvCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });



    }
*/






    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {

        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname)
        {
            case ServiceUrls.RequestNames.TRIP_RATING:






                break;



            case ServiceUrls.RequestNames.ADD_TO_USER_TATX_BALANCE:

//                tvCashToCollect.setClickable(false);


                AddToUserTatxBalanceVo addToUserTatxBalanceVo = Common.getSpecificDataObject(apiResponseVo.data, AddToUserTatxBalanceVo.class);



//                Common.customToast(this,addToUserTatxBalanceVo.message);


                HashMap<String, String> params = new HashMap<>();
                params.put(ServiceUrls.ApiRequestParams.TRIPID, endTripVo.tripId);
                params.put(ServiceUrls.ApiRequestParams.ORDERID, endTripVo.orderId);
                params.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);
                params.put(ServiceUrls.ApiRequestParams.RATING, String.valueOf(rbRatingToUser.getRating()));
                params.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);
//        params.put(ServiceUrls.ApiRequestParams.TIP, );

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.TRIP_RATING,params,false);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

               /* Intent intent = new Intent(this,SplashActivity.class);

                intent.putExtra(Constants.LAST_TRIP_VALUE, lastTripStatus);

                intent.putExtra(Constants.IntentKeys.SHOW_CONTENT_VIEW,false);

                startActivity(intent);

                finish();*/

                if (!getIntent().getBooleanExtra(Constants.IntentKeys.FROM_SPLASH_SCREEN,false)) {

                    DriverOnTripVo driverOnTripVo = new DriverOnTripVo();
                    if (GoogleMapDrawerActivity.makeLastTripValue(InvoiceActivity.this)==0 && endTripVo.breakDown==0)
                    {

                        driverOnTripVo.driverStatus = 1;
                    }else{
                        driverOnTripVo.driverStatus = 0;
                    }
                    Common.Log.i("updateDeviceTokenVo.message : " + driverOnTripVo.toString());

                    Intent intent = new Intent(this, GoogleMapDrawerActivity.class);

                    intent.putExtra(Constants.KEY_1, driverOnTripVo);
                    intent.putExtra(Constants.FROM_INVOICE, true);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                }

                finish();



                break;



        }



    }
}
