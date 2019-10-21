package com.tatx.partnerapp.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.SocketResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.OrderReceivedVo;
import com.tatx.partnerapp.pojos.StatusOrderVo;
import com.tatx.partnerapp.services.MyService;


public class AcceptRejectRideActivity extends BaseActivity implements View.OnClickListener, /*RetrofitResponseListener,*/ SocketResponseListener {

    private Vibrator vib;
    private MediaPlayer mp;
    private String address;


    @BindView(R.id.btn_reject_ride)
    Button btnRejectRide;

    @BindView(R.id.btn_accept_ride)
    Button btnAcceptRide;

    @BindView(R.id.tv_time_sec)
    TextView tvTimeSec;

    @BindView(R.id.tv_driver_current_destination_address)
    TextView tvDriverCurrentDestinationAddress;

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;

    @BindView(R.id.tv_rating)
    TextView tvRating;

    @BindView(R.id.iv_profile_pic)
    ImageView ivProfilePic;

    @BindView(R.id.ll_accept_reject_parent)
    LinearLayout llAcceptRejectParent;

    private OrderReceivedVo orderReceivedVo;
    private CountDownTimer countDownTimer;

    public static AcceptRejectRideActivity getInstance() {
        return instance;
    }

    private static AcceptRejectRideActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_accept_reject_ride);

        ButterKnife.bind(this);

        instance=this;

        orderReceivedVo = (OrderReceivedVo) getIntent().getSerializableExtra(Constants.KEY_1);


        initializedAll();

        mp = MediaPlayer.create(this, R.raw.ring_new);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        vib.vibrate(1000);

        Common.Log.i("o.toString() 1 : "+"orderReceivedVo : "+orderReceivedVo);

        mp.start();



    }


    public void initializedAll()
    {

        address=Common.getCompleteAddressString(this, Double.parseDouble(orderReceivedVo.pickLatitude), Double.parseDouble(orderReceivedVo.pickLongitude));

        tvDriverCurrentDestinationAddress.setText(address);

//        Common.setRoundedCroppedBackgroundImage(this,orderReceivedVo.profile,ivProfilePic,100);


        Common.setRoundedCroppedBackgroundImage(this, orderReceivedVo.profile, ivProfilePic, 100);

        tvCustomerName.setText(orderReceivedVo.customername);

        tvRating.setText(Common.getRatingText(Double.valueOf(orderReceivedVo.rating)));

        btnRejectRide.setOnClickListener(this);
        btnAcceptRide.setOnClickListener(this);

        countDownTimer = new CountDownTimer(15000, 1000)
        {

            public void onTick(long millisUntilFinished)
            {
                tvTimeSec.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                tvTimeSec.setText("0");

                sendStatusOrderRequest(Constants.ORDER_DECLINED, Constants.RequestCodes.RIDE_IGNORED_REQUEST_CODE);



            }

        }.start();




    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("onStart","onStart");


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mp.stop();
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {

            case R.id.btn_reject_ride:

                btnRejectRide.setEnabled(false);

                mp.stop();

                countDownTimer.cancel();

//                llAcceptRejectParent.setVisibility(View.GONE);

                sendStatusOrderRequest(Constants.ORDER_DECLINED, R.id.btn_reject_ride);


//                finish();

                break;


            case R.id.btn_accept_ride:

                btnAcceptRide.setEnabled(false);

                mp.stop();

                countDownTimer.cancel();

//                llAcceptRejectParent.setVisibility(View.GONE);

                sendStatusOrderRequest(Constants.ORDER_ACCEPTED,R.id.btn_accept_ride);


                break;

        }
    }

    public void stopRing(){
        mp.stop();

        countDownTimer.cancel();
        finish();
    }

    private void sendStatusOrderRequest(String rideAcceptStatus, int requestId)
    {

        Common.Log.i("rideAcceptStatus : "+rideAcceptStatus);

        Common.Log.i("requestId : "+requestId);
        Log.d("statusOrder","statusOrder 1 ");

        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put(ServiceUrls.ApiRequestParams.TRIPID, String.valueOf(orderReceivedVo.tripid));
        requestParams.put(ServiceUrls.ApiRequestParams.ORDERID, String.valueOf(orderReceivedVo.orderid));
        requestParams.put(ServiceUrls.ApiRequestParams.STATUS, rideAcceptStatus);
        requestParams.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);
        requestParams.put(ServiceUrls.ApiRequestParams.TYPE, String.valueOf(orderReceivedVo.typeid));

//                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.STATUS_ORDER,requestParams);

        MyService.getInstance().sendSocketRequest(this,ServiceUrls.RequestNames.STATUS_ORDER,requestParams,requestId);


    }


    @Override
    public void onBackPressed()
    {
       Common.Log.i("Inside onBackPressed().");
    }



    @Override
    public void onSocketMessageReceived(ApiResponseVo apiResponseVo, int requestId)
    {
        Log.d("statusOrder","statusOrder 2 ");

        Common.Log.i("? - Inside onSocketMessageReceived()");

        Common.Log.i("? - getClass().getSimpleName() : " + getClass().getSimpleName());

        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        Common.Log.i("apiResponseVo.requestname : "+apiResponseVo.requestname);

        Common.Log.i("apiResponseVo.data : "+apiResponseVo.data);

        Common.Log.i("Switch Case Started");

        Log.d("statusOrder","statusOrder 3 ");

        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.STATUS_ORDER:

                StatusOrderVo statusOrderVo = Common.getSpecificDataObject(apiResponseVo.data, StatusOrderVo.class);

                Common.Log.i("? - o.toString() 2 : "+"StatusOrderVo : "+statusOrderVo);

                Common.Log.i("? - requestId after response : "+requestId);

                Log.d("statusOrder","statusOrder 4 ");


                switch (requestId)
                {
                    case R.id.btn_accept_ride :
                        Log.d("statusOrder","statusOrder 5 ");
//                        countDownTimer.cancel();
                        if(!statusOrderVo.cancelledByCustomer)
                        {

                            Log.d("statusOrder","statusOrder 6 ");
//
                            //  setResult(Constants.ResponseCodes.RIDE_ACCEPTED_RESPONSE_CODE);

                            if (getIntent().getBooleanExtra(Constants.IntentKeys.IS_FROM_SERVICE,false))
                            {

                                Common.restartApp(this);

                            }
                            else
                            {
                                GoogleMapDrawerActivity.getInstance().onResponseFromAcceptRejectActivity(Constants.ResponseCodes.RIDE_ACCEPTED_RESPONSE_CODE);
                            }

                            Common.customToast(this,statusOrderVo.message);

                            Log.d("statusOrder","statusOrder 7 ");

                        }
                        else
                        {
                            Common.customToast(this,statusOrderVo.message);
                        }



                        break;


                    case R.id.btn_reject_ride :
//                        countDownTimer.cancel();
                        Common.customToast(this,statusOrderVo.message);

                        Common.restartApp(this);


                        break;


                    case Constants.RequestCodes.RIDE_IGNORED_REQUEST_CODE:

                        Common.customToast(this,statusOrderVo.message);

                        Common.restartApp(this);


                        break;




                }





//                Intent intent = new Intent(this, SplashActivity.class);
//
//                intent.putExtra(Constants.IntentKeys.SHOW_CONTENT_VIEW,false);
//
//                startActivity(intent);
                finish();



                break;



        }

    }


}
