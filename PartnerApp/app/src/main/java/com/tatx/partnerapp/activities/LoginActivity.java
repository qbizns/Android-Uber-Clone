package com.tatx.partnerapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.CountryCodeListAdapter;
import com.tatx.partnerapp.application.TATX;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.database.CountryCodeSqliteDbHelper;
import com.tatx.partnerapp.database.SqliteDB;
import com.tatx.partnerapp.dataset.CountryCodePojo;
import com.tatx.partnerapp.enums.Language;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.CheckRegistrationStatusVo;
import com.tatx.partnerapp.pojos.DriverLoginVo;
import com.tatx.partnerapp.pojos.LastTripDriverRatingVo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 20-05-2016.
 */
public class LoginActivity extends BaseActivity implements RetrofitResponseListener {


    SqliteDB sqliteDB;
    String regId = "";
    private String ownerAuthorityStatus;
    GridLayoutManager gridLayoutManager;
    CountryCodeListAdapter countryCodeListAdapter;
    private CountryCodeSqliteDbHelper dbHelper;
    private List<CountryCodePojo> colorsList;
    @BindView(R.id.btn_sign_in) Button btnSignIn;

    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.forgetpwd) TextView forgetpwd;
    @BindView(R.id.contry_code) TextView contry_codeButon;
    private String locale;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.login));
        initilaizedAll();
    }
    public void initilaizedAll() {

        String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("unique_id",unique_id);
        regId = Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.REG_ID, "");
        sqliteDB=new SqliteDB(this);
        locale=Common.getDefaultSP(this).contains(Constants.SharedPreferencesKeys.LANGUAGE) ? Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.LANGUAGE,"en") : "en";
        Common.Log.i("locale" +locale);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // Log.d("beforeTextChanged",s+" start "+ start+" before "+ before +" count "+count +"NumberUtils.isNumber "+ NumberUtils.isNumber(""+s));
                if(NumberUtils.isDigits(""+s) && !s.toString().startsWith("000") && s.length()>5){
                    contry_codeButon.setVisibility(View.VISIBLE);
                }else {
                    contry_codeButon.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        insertColorsInDB();
    }

    @OnClick(R.id.btn_sign_in)
    void signIn()
    {
        Common.hideSoftKeyboard(LoginActivity.this);
        String emailId = etEmail.getText().toString().trim();

        String password = etPassword.getText().toString();

        if (!Common.isValidEmail(emailId)&&!NumberUtils.isDigits(emailId))
        {
            etEmail.requestFocus();
            etEmail.setError(Common.getStringFromResources(R.string.please_enter_valid_emailid_or_phone_number));

        }
        else if (password.length()<6)
        {
            etPassword.requestFocus();
            etPassword.setError(Common.getStringFromResources(R.string.please_enter_password_minimum_6_digits));

        }
        else
        {
            String username;
            Log.d("Log1  ",Common.ignoreZeroPrefix(etEmail) ) ;
            if (NumberUtils.isDigits(emailId) && !emailId.startsWith("000")){
                username = contry_codeButon.getText().toString().substring(1)+Common.ignoreZeroPrefix(etEmail);
            }else {
                username=emailId;
            }
            HashMap<String, String> requestParams = new HashMap();
            requestParams.put(ServiceUrls.ApiRequestParams.EMAIL, username);
            requestParams.put(ServiceUrls.ApiRequestParams.PASSWORD, password);
            requestParams.put(ServiceUrls.ApiRequestParams.TOKEN, regId);
            requestParams.put(ServiceUrls.ApiRequestParams.DEVICE_ID, Common.getDeviceId(this));
            requestParams.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);
            requestParams.put(ServiceUrls.ApiRequestParams.LANGUAGE, String.valueOf(Language.getEnumFieldByLocaleCode(locale).getLanguageCode()));

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.DRIVER_LOGIN,requestParams);



        }



    }

    @OnClick(R.id.contry_code) void setCountryCodeList(){
        termsAlert(colorsList);

    }

    @OnClick(R.id.forgetpwd) void setForgotPassword(){
        Intent i = new Intent(getApplicationContext(), ForgotPasswardActivity.class);
        startActivity(i);

    }



    public void insertColorsInDB() {
        dbHelper = new CountryCodeSqliteDbHelper(this);
        dbHelper.openDataBase();
        colorsList = dbHelper.getCountryCodeFlagList();
        Log.d("colorsList", colorsList.toString());


    }
    public void termsAlert(List<CountryCodePojo> countryCodePojos) {
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
                for (int idx = 0; idx < colorsList.size(); idx++) {
                    if (colorsList.get(idx).getCountryName().contains(charSequence) ||
                            colorsList.get(idx).getCountryName().toLowerCase().contains(charSequence) ||
                            colorsList.get(idx).getCountryName().toUpperCase().contains(charSequence) ||
                            Common.toTitleCase(colorsList.get(idx).getCountryCode()).contains(charSequence)) {
                        searchUserFNFContacts.add(colorsList.get(idx));
                        Log.d("name:: ", colorsList.get(idx).getCountryCode().toString());
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
            countryCodeListAdapter = new CountryCodeListAdapter(this, countryCodePojos, false);
            recyclerView.setAdapter(countryCodeListAdapter);
            countryCodeListAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
        }

        countryCodeListAdapter.setOnItemClickListener(new CountryCodeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String code = countryCodePojos.get(position).getCountryCode();
                contry_codeButon.setText("+" + code);
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });


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
            case ServiceUrls.RequestNames.DRIVER_LOGIN:

                DriverLoginVo driverLoginVo = Common.getSpecificDataObject(apiResponseVo.data, DriverLoginVo.class);


                Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, true).commit();
                Common.getDefaultSP(this).edit().putInt(Constants.SharedPreferencesKeys.USERID, driverLoginVo.userid).commit();
                Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.DRIVER_LOGIN_HASH_MAP,new Gson().toJson(requestParams)).commit();

                TATX.updateLanguage(this, Language.getEnumFieldByLanguageCode(Integer.parseInt(driverLoginVo.language)).getLocaleCode());

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CHECK_REGISTRATION_STATUS,null);

                break;

            case ServiceUrls.RequestNames.CHECK_REGISTRATION_STATUS:

                CheckRegistrationStatusVo checkRegistrationStatusVo = Common.getSpecificDataObject(apiResponseVo.data, CheckRegistrationStatusVo.class);


                Class<?> className;

                if(!checkRegistrationStatusVo.createDriverStep2)
                {
                    className = RegistrationActivity2.class;
                }
                else if(!checkRegistrationStatusVo.createDriverStep3)
                {
                    className = RegistrationActivity3.class;
                    ownerAuthorityStatus = checkRegistrationStatusVo.ownerAuthorityStatus==true ? "1" : "0";
                }
                else
                {
                    Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, true).commit();
                    className = GoogleMapDrawerActivity.class;

                }

                Intent i = new Intent(getApplicationContext(), className);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                if (!checkRegistrationStatusVo.createDriverStep3)
                {
                    i.putExtra(Constants.IntentKeys.OWNER_AUTHORITY,ownerAuthorityStatus);
                    i.putExtra(Constants.IntentKeys.EMPLOYEE_TYPE,String.valueOf(checkRegistrationStatusVo.employeeType));
                    i.putExtra(Constants.IntentKeys.COUNTRY_OF_RESIDENCY,checkRegistrationStatusVo.countryOfResidency);
                    i.putExtra(Constants.IntentKeys.CAB_ID, String.valueOf(checkRegistrationStatusVo.cabId));

                }

                startActivity(i);

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.LAST_TRIP_DRIVER_RATING, null);

                break;

            case ServiceUrls.RequestNames.LAST_TRIP_DRIVER_RATING:

                LastTripDriverRatingVo lastTripDriverRatingVo = Common.getSpecificDataObject(apiResponseVo.data, LastTripDriverRatingVo.class);

                if (!lastTripDriverRatingVo.ratingStatus)
                {

                    Intent invoiceIntent = new Intent(this, InvoiceActivity.class);

                    invoiceIntent.putExtra(Constants.KEY_1, lastTripDriverRatingVo.endTripVo);

                    invoiceIntent.putExtra(Constants.IntentKeys.FROM_SPLASH_SCREEN, true);

                    startActivity(invoiceIntent);

                }


        }




    }


}
