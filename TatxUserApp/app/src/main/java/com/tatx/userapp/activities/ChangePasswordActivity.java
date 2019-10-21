package com.tatx.userapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;

import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by user on 11/04/2016.
 */
public class ChangePasswordActivity extends BaseActivity implements RetrofitResponseListener{
    Button login;
    EditText emails, password;

    public static String emailId;
    private String emailOrMobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.change_password));

        initilaizedAll();
    }
    public void initilaizedAll() {

        Intent intent = getIntent();
        emailOrMobile=intent.getStringExtra("email");
        login = (Button) findViewById(R.id.submit_btn);
        emails = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        emails.setHint(Common.getStringResourceText(R.string.new_password));
        emails.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setHint(Common.getStringResourceText(R.string.confirm_new_password));
        Common.restrictSpaceInPasswordField(this,password);
        Common.restrictSpaceInPasswordField(this,emails);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                emailId = emails.getText().toString().trim();
                String passward = password.getText().toString().trim();

                if (passward.length()<6) {
                    password.requestFocus();
                    password.setError(Common.getStringResourceText(R.string.please_enter_new_password));
                }else if (emailId.length()<6) {
                    emails.requestFocus();
                    emails.setError(Common.getStringResourceText(R.string.please_confirm_new_password));

                } else if(emailId.equalsIgnoreCase(passward)){
                    if (Common.haveInternet(ChangePasswordActivity.this)) {
                        callResetPasswordApi(emailOrMobile, passward);
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





    public void callResetPasswordApi(String user_name, String password) {

        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(ServiceUrls.RequestParams.EMAIL, user_name);
        hashMap.put(ServiceUrls.RequestParams.PASSWORD, password);
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.FORGET_PASSWORD, hashMap);

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

               // CustomerOnTripVo customerOnTripVo = Common.getSpecificDataObject(apiResponseVo.data, CustomerOnTripVo.class);
                Common.customToast(this, apiResponseVo.status);
                Intent i = new Intent(getApplicationContext(), SplashScreen.class);
                startActivity(i);
                finish();

        }
        }
}