package com.tatx.userapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tatx.userapp.R;
import com.tatx.userapp.adapter.CountryCodeListAdapter;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.database.CountryCodeSqliteDbHelper;
import com.tatx.userapp.dataset.CountryCodePojo;
import com.tatx.userapp.enums.Language;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.CustomerLoginVo;
import com.tatx.userapp.pojos.EndTripVo;
import com.tatx.userapp.pojos.LastTripRatingVo;
import com.tatx.userapp.pojos.TripDetails;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by user on 11/04/2016.
 */
public class LoginActivity extends SocialMediaLoginBaseActivity implements View.OnClickListener, RetrofitResponseListener
{

    Button login;
    TextView signupvth;
    TextView sign;
    EditText emails, password;
    TextView forgetpwd;
    public String emailId;
    ProgressDialog progressDialog = null;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    String regId;
    private TextView contry_codeButon;
    GridLayoutManager gridLayoutManager;
    CountryCodeListAdapter countryCodeListAdapter;
    private CountryCodeSqliteDbHelper dbHelper;
    private List<CountryCodePojo> colorsList;
    private String locale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.login));
        initilaizedAll();


        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                {
                    Common.Log.i("? - Keypad Done Clicked.");

                    login.performClick();

                }
                return false;
            }
        });




    }

    @Override
    public void onSocialMediaLoginSuccess(String socialMediaType, String socialMediaToken, String profilePicUrl, String firstName, String lastName, String emailId, String id)
    {

        Common.Log.i("Inside Login Activity - onSocialMediaLoginSuccess().");

        Common.Log.i("socialMediaType : "+socialMediaType);
        Common.Log.i("socialMediaToken : "+socialMediaToken);
        Common.Log.i("profilePicUrl : "+profilePicUrl);
        Common.Log.i("firstName : "+firstName);
        Common.Log.i("lastName : "+lastName);
        Common.Log.i("emailId : "+emailId);

        socialMediaToken = socialMediaType.equals(Constants.SocialMediaTypes.FACEBOOK) ? id : socialMediaToken;

        Common.Log.i("socialMediaToken : "+socialMediaToken);

//        sendLoginRequest(emailId, null, regId, Constants.ANDROID, socialMediaType, socialMediaToken,LoginType.SOCIAL_MEDIA.getId());
        sendLoginRequest(emailId, null, regId, Constants.ANDROID, socialMediaType, socialMediaToken);



    }

    public void initilaizedAll() {

        sp= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sp.edit();
        regId = sp.getString(Constants.SharedPreferencesKeys.REG_ID,null);
         locale=Common.getDefaultSP(this).contains(Constants.SharedPreferencesKeys.LANGUAGE) ? Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.LANGUAGE,"en") : "en";
        Common.Log.i("locale" +locale);
        login = (Button) findViewById(R.id.submit_btn);
        emails = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        signupvth = (TextView) findViewById(R.id.signupwith);
        forgetpwd = (TextView)findViewById(R.id.forgetpwd);

        sign = (TextView) findViewById(R.id.sign);
        contry_codeButon = (TextView) findViewById(R.id.tv_country_code);

        contry_codeButon.setOnClickListener(this);
        Common.restrictSpaceInPasswordField(this,password);

        forgetpwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPassward.class);
                startActivity(i);
                finish();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                emailId = emails.getText().toString().trim();
                String passwordStr = password.getText().toString();

                if (!Common.isValidEmail(emailId)&&!NumberUtils.isDigits(emailId)) {
                    emails.requestFocus();
                    emails.setError(Common.getStringResourceText(R.string.please_enter_emailid));

                }
                else if (passwordStr.length()<6)
                {
                    password.requestFocus();
                    password.setError(Common.getStringResourceText(R.string.please_enter_password_minimum_6_digits));

                }
                else
                {

                   Common.hideSoftKeyboard(LoginActivity.this);

                    String username;
                    Log.d("Log1  ",Common.ignoreZeroPrefix(emails));
                    if (NumberUtils.isDigits(emailId) && !emailId.startsWith("000")){
                         username = contry_codeButon.getText().toString().substring(1)+Common.ignoreZeroPrefix(emails);
                    }else {
                        username=emailId;
                    }
                    sendLoginRequest(username, passwordStr, regId, "Android", null, null);


                }

            }
        });
        emails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("beforeTextChanged",s+" start "+ start+" before "+ before +" count "+count +"NumberUtils.isNumber "+NumberUtils.isNumber(""+s));

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





//    public void sendLoginRequest(String user_name, String password, String token, String device, String socialMediaType, String socialMediaToken, int requesetId)
    public void sendLoginRequest(String user_name, String password, String token, String device, String socialMediaType, String socialMediaToken)
    {

        HashMap<String, String> params = new HashMap();
        params.put(ServiceUrls.RequestParams.EMAIL, user_name);
        params.put(ServiceUrls.RequestParams.PASSWORD, password);
        params.put(ServiceUrls.RequestParams.TOKEN, token);
        params.put(ServiceUrls.RequestParams.DEVICE, device);
        params.put(ServiceUrls.RequestParams.DEVICE_ID, Common.getDeviceId(this));
        params.put(ServiceUrls.RequestParams.SOCIAL, socialMediaType);
        params.put(ServiceUrls.RequestParams.STOKEN, socialMediaToken);
        params.put(ServiceUrls.RequestParams.LANGUAGE, String.valueOf(Language.getEnumFieldByLocaleCode(locale).getLanguageCode()));
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CUSTOMER_LOGIN,params);

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_country_code:
                termsAlert(colorsList);
                break;


        }
    }

    public void insertColorsInDB() {
        dbHelper = new CountryCodeSqliteDbHelper(this);
        dbHelper.openDataBase();
        colorsList = dbHelper.getCountryCodeFlagList();
        Log.d("colorsList", colorsList.toString());
        /*for (int i = 0; i < colorsList.size(); i++) {
            //locationsDB.insertColors(colorsList.get(i).getColorCode(), 0);
        }*/

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
            countryCodeListAdapter = new CountryCodeListAdapter(this, countryCodePojos);
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

        if (apiResponseVo.code != Constants.SUCCESS) {

            Common.customToast(this, apiResponseVo.status);
            return;

        }


        switch (apiResponseVo.requestname)
        {


            case ServiceUrls.RequestNames.CUSTOMER_LOGIN:

                CustomerLoginVo customerLoginVo = Common.getSpecificDataObject(apiResponseVo.data, CustomerLoginVo.class);

                Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, true).commit();

                Common.getDefaultSP(this).edit().putInt(Constants.SharedPreferencesKeys.USERID, customerLoginVo.userid).commit();

                Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.CUSTOMER_LOGIN_HASH_MAP,new Gson().toJson(requestParams)).commit();

                TATX.updateLanguage(this, Language.getEnumFieldByLanguageCode(Integer.parseInt(customerLoginVo.language)).getLocaleCode());

                Intent i = new Intent(getApplicationContext(), GoogleMapDrawerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra(Constants.IntentKeys.PROMO_IMG,customerLoginVo.promoImg);
                i.putExtra(Constants.IntentKeys.UN_READ_NOTIFICATION_COUNT,customerLoginVo.unreadNotificationsCount);
                startActivity(i);

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.LAST_TRIP_RATING,null);



                break;






            case ServiceUrls.RequestNames.LAST_TRIP_RATING:

                LastTripRatingVo lastTripRatingVo = Common.getSpecificDataObject(apiResponseVo.data, LastTripRatingVo.class);

                if(!lastTripRatingVo.ratingStatus)
                {

                    TripDetails tripDetails = lastTripRatingVo.tripDetails;

                    EndTripVo endTripVo = new EndTripVo
                            (
                                    Double.parseDouble(tripDetails.amount),
                                    tripDetails.tripid,
                                    tripDetails.orderid,
                                    Double.parseDouble(tripDetails.pickLatitude),
                                    Double.parseDouble(tripDetails.pickLongitude),
                                    tripDetails.distance,
                                    tripDetails.duration,
                                    Double.parseDouble(tripDetails.dropLatitude),
                                    Double.parseDouble(tripDetails.dropLongitude),
                                    Integer.parseInt(tripDetails.paymentType),
                                    tripDetails.pickupLocation,
                                    tripDetails.dropLocation,
                                    tripDetails.distanceCost,
                                    tripDetails.durationCost,
                                    Double.parseDouble(tripDetails.baseFare),
                                    tripDetails.adjustmentAmount,
                                    Double.parseDouble(tripDetails.tripAmount),
                                    tripDetails.discount,
                                    tripDetails.currency

                            );



                    Intent invoiceIntent = new Intent(this, InvoiceActivity.class);

                    invoiceIntent.putExtra(Constants.KEY_1,endTripVo);

                    startActivity(invoiceIntent);



                }
                else
                {

                    Common.Log.i("No need to show Last Trip Rating Screen.");

                }



                break;




        }

    }


}