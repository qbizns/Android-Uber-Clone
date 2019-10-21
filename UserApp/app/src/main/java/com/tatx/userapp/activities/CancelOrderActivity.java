package com.tatx.userapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.PushNotificationListenerActivity;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.network.SocketResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.BroadcastLocationVo;
import com.tatx.userapp.pojos.CustomerOnTripVo;
import com.tatx.userapp.pojos.OnTrip;
import com.tatx.userapp.pojos.OrderInitiatedVo;
import com.tatx.userapp.pojos.PushNotificationResponseVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Home on 29-05-2016.
 */
public class CancelOrderActivity extends PushNotificationListenerActivity implements SocketResponseListener, RetrofitResponseListener {



    private OrderInitiatedVo orderInitiatedVo;

    @BindView(R.id.tv_cancel_order) TextView tvCancelOrder;

    @BindView(R.id.tv_source_address_to_show) TextView tvSourceAddressToShow;

    @BindView(R.id.tv_destination_address_to_show) TextView tvDestinationAddressToShow;

    @BindView(R.id.tv_title_text) TextView registration;
    public static Handler handlerCheckCustomerOnTripStatus;
    private CountDownTimer countDownTimer;
    @BindView(R.id.barTimer) ProgressBar barTimer;
    @BindView(R.id.tv_timer) TextView tvTimer;

   private static CancelOrderActivity inst;

    public static CancelOrderActivity instance() {
        return inst;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cancel_order);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.requesting));
        //startTimer(1);
        startRequestingTimer(1);
        initializedAll();

        orderInitiatedVo = (OrderInitiatedVo) getIntent().getSerializableExtra(Constants.KEY_1);

        Common.Log.i("Inside CancelOrderActivity onCreate().");


        handlerCheckCustomerOnTripStatus = new Handler();

        handlerCheckCustomerOnTripStatus.postDelayed(new Runnable() {
            @Override
            public void run()
            {

                Common.Log.i("? - Handle Push not came scenario by checking trip status from server after 45 sec.");

                new RetrofitRequester(CancelOrderActivity.this).sendStringRequest(ServiceUrls.RequestNames.CUSTOMER_ON_TRIP, null);


            }

        },45000);



    }

    @Override
    protected void onStart() {
        super.onStart();
        inst=this;
    }

    public void initializedAll()
    {


        tvSourceAddressToShow.setText(getIntent().getStringExtra(Constants.IntentKeys.SOURCE_ADDRESS));

        tvDestinationAddressToShow.setText(getIntent().getStringExtra(Constants.IntentKeys.DESTINATION_ADDRESS));

    }
    public void cancelTimer(){
        if (countDownTimer!=null) {
            countDownTimer.cancel();
        }
    }

    @OnClick(R.id.tv_cancel_order)
    public void submit()
    {

        tvCancelOrder.setClickable(false);



        HashMap<String, String> params = new HashMap();
        params.put(ServiceUrls.RequestParams.TRIPID, String.valueOf(orderInitiatedVo.tripid));
        params.put(ServiceUrls.RequestParams.ORDERID, String.valueOf(orderInitiatedVo.orderid));
        params.put(ServiceUrls.RequestParams.DEVICE, "ANDROID");
        params.put(ServiceUrls.RequestParams.REQUESTING, "1");
        params.put(ServiceUrls.RequestParams.TIME_EXCEEDED, "0");
        params.put(ServiceUrls.RequestParams.REASON_ID, "0");

        TATX.getInstance().sendSocketRequest(this,ServiceUrls.RequestNames.CANCEL_TRIP,params);




    }





    @Override
    public void onBackPressed()
    {
//        Common.customToast(this, "You request is under process", Common.TOAST_TIME);

        showRequestUnderProcessToast();



    }



    @Override
    public void onPushNotificationReceived(PushNotificationResponseVo pushNotificationResponseVo)
    {

        Common.Log.i("? - Inside CancelOrderActivity - onPushNotificationReceived().");

        Common.Log.i("pushNotificationResponseVo.toString() : " + pushNotificationResponseVo.toString());

        switch (pushNotificationResponseVo.code)
        {


            case 20000:


                handlerCheckCustomerOnTripStatus.removeCallbacksAndMessages(null);
                cancelTimer();

                OnTrip onTrip = Common.getSpecificDataObject(pushNotificationResponseVo.data, OnTrip.class);

                Common.Log.i("? - onTrip : " + onTrip);

                Intent intent = new Intent(this, OnTripMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("action", 2);
                /*
                intent.putExtra("tvSourceAddressToShow", sourceAdd);
                intent.putExtra("tvDestinationAddressToShow", destinationAdd);
                */

                intent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS,getIntent().getStringExtra(Constants.IntentKeys.SOURCE_ADDRESS));

                intent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS,getIntent().getStringExtra(Constants.IntentKeys.DESTINATION_ADDRESS));

                intent.putExtra(Constants.KEY_1, onTrip);

                finish();

                startActivity(intent);



                break;


        }

    }






    @Override
    public void onInternetConnected()
    {
//        Common.customToast(this,"COA - Internet Connected.");

    }

    @Override
    public void onInternetDisconnected()
    {
//        Common.customToast(this, "COA - "+Constants.NO_INTERNET_CONNECTION_MESSAGE);

    }

    @Override
    public void onSocketMessageReceived(ApiResponseVo apiResponseVo, int requestId)
    {


        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }




        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.CANCEL_TRIP:

                handlerCheckCustomerOnTripStatus.removeCallbacksAndMessages(null);
                cancelTimer();

                Common.customToast(this, apiResponseVo.status, Common.TOAST_TIME);
                Intent intent = new Intent(this, GoogleMapDrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;

            case Constants.BROADCAST_LOCATION:

                BroadcastLocationVo broadcastLocationVo = Common.getSpecificDataObject(apiResponseVo.data, BroadcastLocationVo.class);

                Common.Log.i("? - broadcastLocationVo.drivers.size() : " + broadcastLocationVo.drivers.size());

                break;

        }


    }


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

            case ServiceUrls.RequestNames.CUSTOMER_ON_TRIP:
                countDownTimer.cancel();

                CustomerOnTripVo customerOnTripVo = Common.getSpecificDataObject(apiResponseVo.data, CustomerOnTripVo.class);

                int tripStatus = customerOnTripVo.tripstatus;

                Common.Log.i("tripStatus : " + tripStatus);

                Intent intent = null;

                OnTrip ontrip = customerOnTripVo.ontrip;


                switch (tripStatus) {

                    case Constants.TripStatuses.NOT_ON_TRIP:

                        intent = new Intent(CancelOrderActivity.this, GoogleMapDrawerActivity.class);

                        break;

                    case Constants.TripStatuses.ON_TRIP_BUT_NOT_STARTED:
//                        intent.putExtra("action", 2);
                        intent = new Intent(CancelOrderActivity.this, OnTripMapActivity.class);
                        intent.putExtra(Constants.KEY_1, ontrip);
                        intent.putExtra("action", 2);
                        intent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS, Common.getCompleteAddressString(this, ontrip.pickLatitude, ontrip.pickLongitude).getCompleteAddress());
                        intent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS, Common.getCompleteAddressString(this, ontrip.dropLatitude, ontrip.dropLongitude).getCompleteAddress());

                        break;


                    case Constants.TripStatuses.ON_TRIP_AND_STARTED:

                        intent = new Intent(CancelOrderActivity.this, OnTripMapActivity.class);


                        intent.putExtra(Constants.KEY_1, ontrip);
//                        intent.putExtra("action", 2);
                        intent.putExtra("action", 4);
                        intent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS, Common.getCompleteAddressString(this, ontrip.pickLatitude, ontrip.pickLongitude).getCompleteAddress());
                        intent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS, Common.getCompleteAddressString(this, ontrip.dropLatitude, ontrip.dropLongitude).getCompleteAddress());

                        break;


                }

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

//                finish();


                break;
        }


        }


    @Override
    protected void onTitleBackPressed()
    {
        Common.Log.i("? - onTitleBackPressed Disabled.");
        showRequestUnderProcessToast();

    }

    private void showRequestUnderProcessToast() {
        Common.customToast(this, Common.getStringResourceText(R.string.you_request_is_under_process), Common.TOAST_TIME);
    }



    private void startRequestingTimer(final int minuti) {
        countDownTimer = new CountDownTimer(60000, 1000) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = 60-(leftTimeInMilliseconds / 1000);
                barTimer.setProgress((int)seconds);
                tvTimer.setText( String.valueOf(leftTimeInMilliseconds / 1000) );
                // format the textview to show the easily readable format

            }
            @Override
            public void onFinish() {

                Intent intent = new Intent(CancelOrderActivity.this, GoogleMapDrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        }.start();

    }
}




