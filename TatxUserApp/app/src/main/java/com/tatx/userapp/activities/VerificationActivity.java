package com.tatx.userapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.broadcastreveiver.ReadIncomingSms;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.enums.Language;
import com.tatx.userapp.interfaces.SmsListener;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.ChangeDefaultLanguageVo;
import com.tatx.userapp.pojos.CreateCustomerVo;
import com.tatx.userapp.pojos.SendRegistrationOtpVo;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.mime.TypedFile;

/**
 * Created by Home on 02-05-2016.
 */
public class VerificationActivity extends BaseActivity implements View.OnClickListener, RetrofitResponseListener {

    public static VerificationActivity inst;
    private String smsotp="0";
    private int otp;
    @BindView(R.id.verify) Button verify;
    @BindView(R.id.resend_otp) Button resend_otp;
    @BindView(R.id.mobileno) TextView mobileno;
    @BindView(R.id.otp_editbox) EditText otp_editbox;
    private String email;
    String phone_number;
    private int value;
    String regId = "";
    private CountDownTimer countDownTimer;
    long time=1000*60*2; //5Mins
    private int userid;


    public static VerificationActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.verification);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.verify));

        initilaizedAll();

    }

    public void initilaizedAll() {


        regId = Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.REG_ID, "");
        Intent intent = getIntent();
        otp = intent.getIntExtra(Constants.IntentKeys.OTP, 0);
        value = intent.getIntExtra(Constants.IntentKeys.VALUE, 0);
        userid=intent.getIntExtra("userid",0);
        Log.d("otp verification:", String.valueOf(otp));
        phone_number = intent.getStringExtra(Constants.IntentKeys.PHONE_NUMBER);
        email = intent.getStringExtra(Constants.IntentKeys.EMAIL);

        mobileno.setText(phone_number);
        verify.setOnClickListener(this);
        resend_otp.setOnClickListener(this);

        ReadIncomingSms.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                receivedSmS(messageText);
            }
        });

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
                smsotp = message.substring(0, 4);
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
                Common.hideSoftKeyboard(this);
                smsotp=otp_editbox.getText().toString().trim();
                if (smsotp.length()>3) {
                    if (otp == Integer.valueOf(smsotp)) {
                        if (value == Constants.CALL_API_FROM_REGISTRATION) {
                            if (Common.haveInternet(this)) {

                                HashMap<String, String> params = new HashMap();

                                params.put(ServiceUrls.RequestParams.FIRST_NAME, getIntent().getStringExtra(Constants.IntentKeys.FIRST_NAME));
                                params.put(ServiceUrls.RequestParams.LAST_NAME, getIntent().getStringExtra(Constants.IntentKeys.LAST_NAME));
                                params.put(ServiceUrls.RequestParams.EMAIL, getIntent().getStringExtra(Constants.IntentKeys.EMAIL));
                                params.put(ServiceUrls.RequestParams.PASSWORD, getIntent().getStringExtra(Constants.IntentKeys.PASSWORD));
                                params.put(ServiceUrls.RequestParams.COUNTRY_CODE,getIntent().getStringExtra(Constants.IntentKeys.COUNTRY_CODE));
                                params.put(ServiceUrls.RequestParams.PHONE_NUMBER, getIntent().getStringExtra(Constants.IntentKeys.PHONE_NUMBER));
                                params.put(ServiceUrls.RequestParams.REFERRAL_CODE, getIntent().getStringExtra(Constants.IntentKeys.REFERRAL_CODE));
                                params.put(ServiceUrls.RequestParams.SOCIAL, getIntent().getStringExtra(ServiceUrls.RequestParams.SOCIAL));
                                params.put(ServiceUrls.RequestParams.SOCIAL_TOKEN, getIntent().getStringExtra(ServiceUrls.RequestParams.SOCIAL_TOKEN));
                                params.put(ServiceUrls.RequestParams.GCM_TOKEN, regId);
                                params.put(ServiceUrls.RequestParams.PROFILE_PIC, getIntent().getStringExtra(ServiceUrls.RequestParams.PROFILE_PIC));
                                params.put(ServiceUrls.RequestParams.DEVICE, Constants.ANDROID);
                                params.put(ServiceUrls.RequestParams.DEVICE_ID, Common.getDeviceId(this));
                                params.put(ServiceUrls.RequestParams.LANGUAGE, getIntent().getStringExtra(Constants.IntentKeys.LANGUAGE));

//                                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CREATE_CUSTOMER, params);




                                File file = (File) getIntent().getSerializableExtra(Constants.IntentKeys.PROFILE_PIC_FILE);


                                Common.Log.i("? - file : "+file);

                                if (file != null)
                                {

                                    Common.Log.i("? - file.exists() : "+file.exists());

                                    Common.Log.i("? - file.length() : "+file.length());

                                }




                                HashMap<String, TypedFile> fileHashMap = new HashMap<>();


                                TypedFile typedFile = (file != null)?Common.getTypedFile(file):null;

                                fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMAGE,typedFile);

                                new RetrofitRequester(this).sendMultiPartRequest(ServiceUrls.RequestNames.CREATE_CUSTOMER,params,fileHashMap);





                            } else {
                                Common.customToast(getApplicationContext(), Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
                            }
                        } else {
                            Intent intent = new Intent(this, ChangePasswordActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
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
                if (value == Constants.CALL_API_FROM_REGISTRATION) {
                    HashMap<String, String> requestParams = new HashMap();
                    requestParams.put(ServiceUrls.RequestParams.EMAIL, email);
                    requestParams.put(ServiceUrls.RequestParams.PHONE_NUMBER,phone_number);
                    //requestParams.put(ServiceUrls.RequestParams.REFERRAL_CODE, etReferralCode.getText().toString());
                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.SEND_REGISTRATION_OTP, requestParams);
                }else {
                    HashMap<String, String> params = new HashMap();
                    params.put(ServiceUrls.RequestParams.EMAIL, email);
                    params.put(ServiceUrls.RequestParams.ROLE, Constants.CUSTOMER);

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






    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {



        if (apiResponseVo.code != Constants.SUCCESS) {

            Common.customToast(this, apiResponseVo.status);
            return;

        }


        switch (apiResponseVo.requestname) {


            case ServiceUrls.RequestNames.CREATE_CUSTOMER:

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                CreateCustomerVo createCustomerVo = Common.getSpecificDataObject(apiResponseVo.data, CreateCustomerVo.class);

//                AppConstants.userProfile = new UserProfile(null,createCustomerVo.firstName,createCustomerVo.lastName,createCustomerVo.email,createCustomerVo.country,Common.getOnlyPhoneNumber(createCustomerVo.country,createCustomerVo.phoneNumber),1,"Emergency Contact Name",null,"Emergency Contact Number");

                HashMap<String, String> loginParams = new HashMap();
                loginParams.put(ServiceUrls.RequestParams.EMAIL, requestParams.get(ServiceUrls.RequestParams.EMAIL));
                loginParams.put(ServiceUrls.RequestParams.PASSWORD, requestParams.get(ServiceUrls.RequestParams.PASSWORD));
                loginParams.put(ServiceUrls.RequestParams.DEVICE, Constants.ANDROID);
                loginParams.put(ServiceUrls.RequestParams.SOCIAL, requestParams.get(ServiceUrls.RequestParams.SOCIAL));
                loginParams.put(ServiceUrls.RequestParams.STOKEN, requestParams.get(ServiceUrls.RequestParams.SOCIAL_TOKEN));

                Common.Log.i("loginParams.toString() : " + loginParams.toString());

                Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.CUSTOMER_LOGIN_HASH_MAP, new Gson().toJson(loginParams)).commit();

                Common.saveUserIdIntoSP(createCustomerVo.userid);

                Common.saveLoginStatusIntoSP(true);


                HashMap<String, String> changeDefaultLanguageParams = new HashMap();

                changeDefaultLanguageParams.put(ServiceUrls.RequestParams.LANGUAGE, String.valueOf(Language.getEnumFieldByLocaleCode(Locale.getDefault().getLanguage()).getLanguageCode()));

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CHANGE_DEFAULT_LANGUAGE, changeDefaultLanguageParams);


                break;


            case ServiceUrls.RequestNames.CHANGE_DEFAULT_LANGUAGE:

                ChangeDefaultLanguageVo changeDefaultLanguageVo = Common.getSpecificDataObject(apiResponseVo.data, ChangeDefaultLanguageVo.class);


                TATX.updateLanguage(this, Language.getEnumFieldByLanguageCode(Integer.parseInt(changeDefaultLanguageVo.language)).getLocaleCode());


                HashMap<String, String> params = new HashMap();
                params.put(ServiceUrls.RequestParams.EMAIL, email);
                params.put(ServiceUrls.RequestParams.STATUS, "1");
                params.put(ServiceUrls.RequestParams.TOKEN, regId);
                params.put(ServiceUrls.RequestParams.DEVICE, Constants.ANDROID);
                params.put(ServiceUrls.RequestParams.ROLE, Constants.CUSTOMER);

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.USER_STATUS, params);


                break;


            case ServiceUrls.RequestNames.USER_STATUS:

                Intent intent = new Intent(this, GoogleMapDrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


                break;

            case ServiceUrls.RequestNames.SEND_REGISTRATION_OTP:

                SendRegistrationOtpVo sendRegistrationOtpVo = Common.getSpecificDataObject(apiResponseVo.data, SendRegistrationOtpVo.class);

               otp=sendRegistrationOtpVo.otp;

                break;
            case ServiceUrls.RequestNames.RESEND_OTP_CUSTOMER:
                SendRegistrationOtpVo sendRegistrationOtpVo1 = Common.getSpecificDataObject(apiResponseVo.data, SendRegistrationOtpVo.class);
                otp=sendRegistrationOtpVo1.otp;
                break;



        }



    }





}
