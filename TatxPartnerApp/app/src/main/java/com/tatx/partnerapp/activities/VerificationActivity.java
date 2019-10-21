package com.tatx.partnerapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.dataset.CreateDriverStep1Vo;
import com.tatx.partnerapp.gcm.ApplicationConstants;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.SendRegistrationOtpVo;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.mime.TypedFile;

/**
 * Created by user on 20-05-2016.
 */
public class VerificationActivity extends BaseActivity implements View.OnClickListener,RetrofitResponseListener{


    public static VerificationActivity inst;
    private String smsotp;
    private int otp;
    @BindView(R.id.verify) Button verify;
    @BindView(R.id.resend_otp) Button resend_otp;
    @BindView(R.id.mobileno) TextView mobileno;
    @BindView(R.id.otp_editbox) EditText otp_editbox;
    private String phone_number;
    private String email;
    private int value;
    private int userid;
    private int resent;
    String regId = "";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Context applicationContext;
    GoogleCloudMessaging gcmObj;
    private CountDownTimer countDownTimer;
    long time=1000*60*2; //5Mins
    HashMap<String, String> requestParams;
    public static VerificationActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.verify));
        initilaizedAll();
    }

    public void initilaizedAll() {
        regId=Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.REG_ID,"");
        Intent intent = getIntent();
        otp = intent.getIntExtra("otp",0);
        value = intent.getIntExtra("value",0);
        userid=intent.getIntExtra("userid",0);
        if (value== Constants.CALL_API_FROM_REGISTRATION) {
            requestParams = (HashMap<String, String>) intent.getSerializableExtra(Constants.IntentKeys.REGISTRATION_VO_KEY);
            phone_number = requestParams.get(Constants.IntentKeys.MOBILE_NUMBER);
            email = requestParams.get(Constants.IntentKeys.EMAIL);
        }else {
            phone_number=intent.getStringExtra(Constants.IntentKeys.MOBILE_NUMBER);
            email=intent.getStringExtra(Constants.IntentKeys.EMAIL);
        }

        Log.d("otp verification:", String.valueOf(otp));

        mobileno.setText(getResources().getString(R.string.verification_code)+" "+ phone_number);
        verify.setOnClickListener(this);
        resend_otp.setOnClickListener(this);



    }


    @Override
    protected void onStart() {
        super.onStart();
        inst = this;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void receivedSmS(String message) {
        try {
            if (message != null) {
                smsotp = message.substring(0,4);
                Log.d("password", message);
                otp_editbox.setText(smsotp);
                if (countDownTimer!=null){
                    countDownTimer.cancel();
                }
                verify.performClick();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify:
                Common.hideSoftKeyboard(VerificationActivity.this);
                if (otp_editbox.getText().toString().trim().length()>3) {
                    if (otp == Integer.valueOf(otp_editbox.getText().toString().trim())) {
                        if (value == Constants.CALL_API_FROM_REGISTRATION) {
                            if (Common.haveInternet(this)) {
                                if (regId.equalsIgnoreCase(""))
                                {
                                    onCreateData();
                                }
                                else
                                {


//                                    new RetrofitRequester(VerificationActivity.this).sendStringRequest(ServiceUrls.RequestNames.CREATE_DRIVER, requestParams);

                                    File file = (File) getIntent().getSerializableExtra(Constants.IntentKeys.PROFILE_PIC_FILE);

                                    HashMap<String, TypedFile> fileHashMap = new HashMap<>();

                                    fileHashMap.put(ServiceUrls.MultiPartRequestParams.PROFILE_PIC,Common.getTypedFile(file));

                                    new RetrofitRequester(VerificationActivity.this).sendMultiPartRequest(ServiceUrls.RequestNames.CREATE_DRIVER_STEP1,requestParams,fileHashMap);





                                }
                            } else {
                                Common.customToast(getApplicationContext(), Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
                            }
                        } else {
                            Intent intent = new Intent(this, ChangePasswordActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("phone_number", phone_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
//                            finish();
                        }
                    } else {
                        Common.customToast(this, Common.getStringResourceText(R.string.invalid_otp), Common.TOAST_TIME);
                    }
                }else {
                    Common.customToast(this, Common.getStringResourceText(R.string.please_enter_4_digit_otp), Common.TOAST_TIME);
                }
                break;
            case R.id.resend_otp:
                resend_otp.setOnClickListener(null);
                if (value==Constants.CALL_API_FROM_REGISTRATION) {

                    HashMap<String, String> params = new HashMap();
                    params.put(ServiceUrls.ApiRequestParams.EMAIL, email);
                    params.put(ServiceUrls.ApiRequestParams.PHONE_NUMBER, phone_number);
                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.SEND_REGISTRATION_OTP, params);
                }else {
                    HashMap<String, String> params = new HashMap();
                    params.put(ServiceUrls.ApiRequestParams.EMAIL, email);
                    params.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);

                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.RESEND_OTP_CUSTOMER, params);
                }
                countDownTimer = new CountDownTimer(time, 1000)
                {

                    public void onTick(long millisUntilFinished)
                    {
                        String hms = String.format("%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        resend_otp.setText(hms);
                    }

                    public void onFinish()
                    {

                        resend_otp.setOnClickListener(VerificationActivity.this);
                        resend_otp.setText(Common.getStringResourceText(R.string.resend_code));

                    }

                }.start();

                break;

        }
    }









    public void onCreateData() {
        applicationContext = getApplicationContext();
          if (checkPlayServices()) {
            registerInBackground();
        }

    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj
                            .register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;
                    Log.d("regsAn", regId);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
/*
                    Toast.makeText(
                            VerificationActivity.this,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();*/

            }
        }.execute(null, null, null);
    }





    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        applicationContext,
                        Common.getStringResourceText(R.string.this_device_doesn),
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
//            Toast.makeText(
//                    applicationContext,
//                    "This device supports Play services, App will work normally",
//                    Toast.LENGTH_LONG).show();
        }
        return true;
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
            case ServiceUrls.RequestNames.CREATE_DRIVER_STEP1:

              /*  Common.customToast(this, apiResponseVo.status);

                RegistrationResponseVo registrationResponseVo=Common.getSpecificDataObject(apiResponseVo.data, RegistrationResponseVo.class);


                HashMap<String, String> loginParams = new HashMap();
                loginParams.put(ServiceUrls.ApiRequestParams.EMAIL, requestParams.get(Constants.IntentKeys.EMAIL));
                loginParams.put(ServiceUrls.ApiRequestParams.PASSWORD, requestParams.get(Constants.IntentKeys.PASSWORD));
                loginParams.put(ServiceUrls.ApiRequestParams.TOKEN, regId);
                loginParams.put(ServiceUrls.ApiRequestParams.DEVICE_ID, Common.getDeviceId(this));
                loginParams.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);



                Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.DRIVER_LOGIN_HASH_MAP,new Gson().toJson(loginParams)).commit();

                Common.saveUserIdIntoSP(registrationResponseVo.userid);

                Common.saveLoginStatusIntoSP(true);
*//*

                editor.putInt(Constants.SharedPreferencesKeys.USERID,registrationResponseVo.userid);
                editor.commit();

*//*
*/

                Common.customToast(this, apiResponseVo.status);

                CreateDriverStep1Vo createDriverStep1Vo =Common.getSpecificDataObject(apiResponseVo.data, CreateDriverStep1Vo.class);

                HashMap<String, String> loginParams = new HashMap();
                loginParams.put(ServiceUrls.ApiRequestParams.EMAIL, requestParams.get(Constants.IntentKeys.EMAIL));
                loginParams.put(ServiceUrls.ApiRequestParams.PASSWORD, requestParams.get(Constants.IntentKeys.PASSWORD));
                loginParams.put(ServiceUrls.ApiRequestParams.TOKEN, regId);
                loginParams.put(ServiceUrls.ApiRequestParams.DEVICE_ID, Common.getDeviceId(this));
                loginParams.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);

                Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.DRIVER_LOGIN_HASH_MAP,new Gson().toJson(loginParams)).commit();

                Common.saveUserIdIntoSP(createDriverStep1Vo.userid);

                Common.saveLoginStatusIntoSP(true);



                HashMap<String, String> params = new HashMap();
                params.put(ServiceUrls.ApiRequestParams.EMAIL, email);
                params.put(ServiceUrls.ApiRequestParams.STATUS, "1");
                params.put(ServiceUrls.ApiRequestParams.TOKEN, regId);
                params.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);
                params.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);
                params.put(ServiceUrls.ApiRequestParams.DEVICE_ID,Common.getDeviceId(this));

                new RetrofitRequester(VerificationActivity.this).sendStringRequest(ServiceUrls.RequestNames.USER_STATUS, params);




                break;

            case ServiceUrls.RequestNames.USER_STATUS:

                Common.customToast(this, apiResponseVo.status);

                if (countDownTimer!=null)
                {
                    countDownTimer.cancel();
                }


                Intent registrationActivity2Intent=new Intent(this,RegistrationActivity2.class);
                registrationActivity2Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(registrationActivity2Intent);

                finish();

                break;
            case ServiceUrls.RequestNames.RESEND_OTP_CUSTOMER:
                SendRegistrationOtpVo sendRegistrationOtpVo = Common.getSpecificDataObject(apiResponseVo.data, SendRegistrationOtpVo.class);
                otp=sendRegistrationOtpVo.otp;
                break;
            case ServiceUrls.RequestNames.SEND_REGISTRATION_OTP:
                SendRegistrationOtpVo sendRegistrationOtpVo1 = Common.getSpecificDataObject(apiResponseVo.data, SendRegistrationOtpVo.class);
                otp=sendRegistrationOtpVo1.otp;
                break;

        }



    }
}
