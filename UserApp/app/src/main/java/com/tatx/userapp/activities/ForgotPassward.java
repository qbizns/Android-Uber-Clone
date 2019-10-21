package com.tatx.userapp.activities;

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
import android.widget.Toast;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.adapter.CountryCodeListAdapter;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.database.CountryCodeSqliteDbHelper;
import com.tatx.userapp.dataset.CountryCodePojo;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.SendRegistrationOtpVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 13/04/2016.
 */

public class ForgotPassward extends BaseActivity implements View.OnFocusChangeListener,RetrofitResponseListener
{


    private int otp;
    private String phone_number;
    GridLayoutManager gridLayoutManager;
    CountryCodeListAdapter countryCodeListAdapter;
    private CountryCodeSqliteDbHelper dbHelper;
    private List<CountryCodePojo> countryCodePojoList;
    private CountryCodePojo saudia_flag;
    @BindView(R.id.tv_country_code) TextView tvCountryCode;
    @BindView(R.id.flag_image) ImageView flag_image;
    @BindView(R.id.resetpassword) Button resetpwd;
    @BindView(R.id.username_pass) EditText username_passwd;
    @BindView(R.id.cardview)  LinearLayout cardview;
    @BindView(R.id.phone_number_ll_bg)  LinearLayout phoneNumberBackgroundLL;
    @BindView(R.id.et_phone_number) EditText etPhoneNumber;
    private String emailId;


    // ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.forgot_password));

        initialaizedAll();
    }

    public void initialaizedAll() {
        getCountryCodeFromDB();
        etPhoneNumber.setOnFocusChangeListener(this);
        username_passwd.setOnFocusChangeListener(this);
        Common.removeZeroPrefix(this,etPhoneNumber);


    }

    @OnClick(R.id.resetpassword)
    void reSendPassword()
    {
//        String emailId = username_passwd.getText().toString().trim();
        emailId = username_passwd.getText().toString().trim();

        if (!Common.isValidEmail(emailId)&&etPhoneNumber.getText().toString().length()<9)
        {
            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_email_or_mobile_no));
        }
        else if (Common.haveInternet(this))
        {
            if (etPhoneNumber.getText().toString().length()>8)
            {
                String countrycode = tvCountryCode.getText().toString().trim();
                emailId=countrycode.substring(1, countrycode.length())+etPhoneNumber.getText().toString().trim();
            }

            callResendOtpApi(emailId);

        }
        else
        {
            Common.customToast(this, Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
        }

    }


@OnClick (R.id.cardview)
void selectCountry(){
    termsAlert(countryCodePojoList);
}




    public void callResendOtpApi(String user_name) {

        HashMap<String, String> params = new HashMap();
        params.put(ServiceUrls.RequestParams.EMAIL, user_name);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.RESEND_OTP_CUSTOMER, params);

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
            countryCodeListAdapter = new CountryCodeListAdapter(this, countryCodePojos);
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
        if (apiResponseVo.code != Constants.SUCCESS) {

            Common.customToast(this, apiResponseVo.status);
            return;

        }


        switch (apiResponseVo.requestname) {


            case ServiceUrls.RequestNames.RESEND_OTP_CUSTOMER:
                SendRegistrationOtpVo sendRegistrationOtpVo = Common.getSpecificDataObject(apiResponseVo.data, SendRegistrationOtpVo.class);


                otp = sendRegistrationOtpVo.otp;

                phone_number=sendRegistrationOtpVo.phoneNumber;


                // Toast.makeText(this, ((RestResponse)o).getData().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, apiResponseVo.status, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), VerificationActivity.class);
                i.putExtra("otp", otp);
                i.putExtra("value", Constants.CALL_API_FROM_FORGOT_PASS);
//                    i.putExtra("email", username_passwd.getText().toString());
                i.putExtra("email", emailId);
                i.putExtra("phone_number",phone_number);
                startActivity(i);
                finish();
                break;
        }

        }
}
