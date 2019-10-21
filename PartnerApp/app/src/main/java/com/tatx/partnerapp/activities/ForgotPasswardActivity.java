package com.tatx.partnerapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.CountryCodeListAdapter;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.database.CountryCodeSqliteDbHelper;
import com.tatx.partnerapp.dataset.CountryCodePojo;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.ResendOtpVo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 13/04/2016.
 */

public class ForgotPasswardActivity extends BaseActivity implements View.OnFocusChangeListener,RetrofitResponseListener {
    private int otp;
    private String phone_number;
    GridLayoutManager gridLayoutManager;
    CountryCodeListAdapter countryCodeListAdapter;
    private CountryCodeSqliteDbHelper dbHelper;
    private List<CountryCodePojo> countryCodePojoList;
    private CountryCodePojo saudia_flag;
    @BindView(R.id.tv_country_code) TextView tvCountryCode;
    @BindView(R.id.flag_image)
    ImageView flag_image;
    @BindView(R.id.resetpassword) Button resetpwd;
    @BindView(R.id.username_pass) EditText username_passwd;
    @BindView(R.id.ll_cardview)
    LinearLayout cardview;
    @BindView(R.id.phone_number_ll_bg)  LinearLayout phoneNumberBackgroundLL;
    @BindView(R.id.et_phone_number) EditText etPhoneNumber;
    private String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.forgotpwd));

        initialaizedAll();
    }

    public void initialaizedAll() {
        getCountryCodeFromDB();
        etPhoneNumber.setOnFocusChangeListener(this);
        username_passwd.setOnFocusChangeListener(this);
        Common.removeZeroPrefix(this,etPhoneNumber);


    }
    @OnClick(R.id.resetpassword)
    void reSendPassword(){
        Common.hideSoftKeyboard(ForgotPasswardActivity.this);
        emailId = username_passwd.getText().toString().trim();
        if (!Common.isValidEmail(emailId)&&etPhoneNumber.getText().toString().length()<9) {
//            username_passwd.requestFocus();
//            username_passwd.setError("Please Enter Username");
            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_email_or_mobile_no));
        } else if (Common.haveInternet(this)) {
            if (etPhoneNumber.getText().toString().length()>8){
                String countrycode = tvCountryCode.getText().toString().trim();
                emailId=countrycode.substring(1, countrycode.length())+etPhoneNumber.getText().toString().trim();
            }
            callResendOtpApi(emailId);
        } else {
            Common.customToast(this, Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
        }
    }
    @OnClick (R.id.ll_cardview)
    void selectCountry(){
        termsAlert(countryCodePojoList);
    }




    public void callResendOtpApi(String user_name) {


        HashMap<String,String>  hashMap=new HashMap();
        hashMap.put(ServiceUrls.ResponseParams.EMAIL, user_name);
        hashMap.put(ServiceUrls.ResponseParams.ROLE, Constants.DRIVER);

        new RetrofitRequester(ForgotPasswardActivity.this).sendStringRequest(ServiceUrls.RequestNames.RESEND_OTP_CUSTOMER,hashMap);

    }



    public void getCountryCodeFromDB()
    {
        dbHelper = new CountryCodeSqliteDbHelper(this);
        dbHelper.openDataBase();
        countryCodePojoList = dbHelper.getCountryCodeFlagList();
        Log.d("countryCodePojoList", countryCodePojoList.toString());
        saudia_flag=dbHelper.getDetailsByCountryCode("966");
        setCountryCodeFlag(saudia_flag);

    }


    public void termsAlert(List<CountryCodePojo> countryCodePojos)
    {
        Button close;
        final RecyclerView list;
        EditText search;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.country_code_dialog);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        list = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);
        search = (EditText) dialog.findViewById(R.id.search_button);
        close = (Button) dialog.findViewById(R.id.button);
        setListOnDialog(countryCodePojos, list, dialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                List<CountryCodePojo> searchUserFNFContacts = new ArrayList<CountryCodePojo>();
                for (int idx = 0; idx < countryCodePojoList.size(); idx++) {
                    if (countryCodePojoList.get(idx).getCountryName().contains(charSequence) ||
                            countryCodePojoList.get(idx).getCountryName().toLowerCase().contains(charSequence) ||
                            countryCodePojoList.get(idx).getCountryName().toUpperCase().contains(charSequence) ||
                            Common.toTitleCase(countryCodePojoList.get(idx).getCountryCode()).contains(charSequence)) {
                        searchUserFNFContacts.add(countryCodePojoList.get(idx));
                        Log.d("name:: ", countryCodePojoList.get(idx).getCountryCode().toString()+" blob"+ countryCodePojoList.get(idx).getFlag());
                    }
                }
                setListOnDialog(searchUserFNFContacts, list, dialog);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }

    public void setListOnDialog(final List<CountryCodePojo> countryCodePojos, RecyclerView recyclerView, final Dialog dialog) {
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (countryCodePojos != null) {
            countryCodeListAdapter = new CountryCodeListAdapter(this, countryCodePojos,false);
            recyclerView.setAdapter(countryCodeListAdapter);
            countryCodeListAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
        }

        countryCodeListAdapter.setOnItemClickListener(new CountryCodeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setCountryCodeFlag(countryCodePojos.get(position));

                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });


    }

    private void setCountryCodeFlag(CountryCodePojo countryCodePojos) {
        String code = countryCodePojos.getCountryCode();
        tvCountryCode.setText("+" + code);

        byte[] byteCode = countryCodePojos.getFlag();
        if (byteCode!=null) {
            Bitmap flagBitMap = Common.getBitmapFromByteArray(byteCode);
            flag_image.setImageBitmap(flagBitMap);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.et_phone_number:
                if (hasFocus)
                {
                    phoneNumberBackgroundLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_text_background));
                    username_passwd.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_text_disabled_bg));
                    ///  cardview.setBackgroundColor(getResources().getColor(R.color.white));
                    username_passwd.setText(null);
                }else {

                }
                break;

            case R.id.username_pass:
                if (hasFocus)
                {
                    username_passwd.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_text_background));
                    phoneNumberBackgroundLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_text_disabled_bg));
                    //  cardview.setBackgroundColor(getResources().getColor(R.color.edit_text_disabled_bg_color));
                    etPhoneNumber.setText(null);
                }
                break;
        }
    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {
            case ServiceUrls.RequestNames.RESEND_OTP_CUSTOMER:


                Common.customToast(this, apiResponseVo.status);

                ResendOtpVo resendOtpVo = Common.getSpecificDataObject(apiResponseVo.data, ResendOtpVo.class);
                otp=resendOtpVo.otp;
                phone_number=resendOtpVo.phoneNumber;

                Intent i = new Intent(getApplicationContext(), VerificationActivity.class);
                i.putExtra("otp", otp);
                i.putExtra("value", Constants.CALL_API_FROM_FORGOT_PASS);
//                    i.putExtra("email", username_passwd.getText().toString());
                i.putExtra(Constants.IntentKeys.EMAIL, emailId);
                i.putExtra(Constants.IntentKeys.MOBILE_NUMBER,phone_number);
                startActivity(i);

                break;
        }
    }
}
