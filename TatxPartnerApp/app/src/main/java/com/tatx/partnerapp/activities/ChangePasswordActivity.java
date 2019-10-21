package com.tatx.partnerapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by user on 20-05-2016.
 */
public class ChangePasswordActivity extends BaseActivity implements RetrofitResponseListener{
    @BindView(R.id.btn_sign_in) Button login;
    @BindView(R.id.et_email) EditText emails;
    @BindView(R.id.et_password) EditText password;
    public static String emailId;
    private String emailOrMobile;
    private String phone_number;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.forgotpwd));
        initilaizedAll();






    }
    public void initilaizedAll() {

        Intent intent = getIntent();
        emailOrMobile=intent.getStringExtra("email");
        phone_number=intent.getStringExtra("phone_number");

        Common.restrictSpaceInPasswordField(this,emails);
        Common.restrictSpaceInPasswordField(this,password);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                emailId = emails.getText().toString().trim();
                String passward = password.getText().toString().trim();
                    if (emailOrMobile.equalsIgnoreCase("")){
                        emailOrMobile=phone_number;
                    }
                if (passward.length()<6) {
                    password.requestFocus();
                    password.setError(Common.getStringFromResources(R.string.please_enter_new_password));
                }else if (emailId.length()<6) {
                    emails.requestFocus();
                    emails.setError(Common.getStringFromResources(R.string.please_confirm_new_password));

                }  else if(emailId.equalsIgnoreCase(passward)){
                    if (Common.haveInternet(ChangePasswordActivity.this)) {


                        HashMap<String,String>  hashMap=new HashMap();
                        hashMap.put(ServiceUrls.ResponseParams.EMAIL, emailOrMobile);
                        hashMap.put(ServiceUrls.ResponseParams.PASSWORD, passward);

                        new RetrofitRequester(ChangePasswordActivity.this).sendStringRequest(ServiceUrls.RequestNames.FORGET_PASSWORD,hashMap);
                    }
                    else {
                        Common.customToast(ChangePasswordActivity.this, Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
                    }

                }else {
                    Common.customToast(ChangePasswordActivity.this, Common.getStringResourceText(R.string.mismatch_password), Common.TOAST_TIME);
                }
            }
        });
    }




    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.FORGET_PASSWORD:

                //GetDriverRatingVo getDriverRatingVo = Common.getSpecificDataObject(apiResponseVo.data, GetDriverRatingVo.class);

                Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}