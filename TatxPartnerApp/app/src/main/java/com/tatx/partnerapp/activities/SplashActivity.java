package com.tatx.partnerapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.NetworkChangeListenerActivity;
import com.tatx.partnerapp.application.TATX;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.enums.Language;
import com.tatx.partnerapp.gcm.ApplicationConstants;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.CheckRegistrationStatusVo;
import com.tatx.partnerapp.pojos.DriverLoginVo;
import com.tatx.partnerapp.pojos.DriverOnTripVo;
import com.tatx.partnerapp.pojos.LastTripDriverRatingVo;
import com.tatx.partnerapp.pojos.UpdateDeviceTokenVo;

import org.json.JSONObject;
import org.jsoup.Jsoup;


public class SplashActivity extends NetworkChangeListenerActivity implements RetrofitResponseListener {

    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private String currentVersion;
    private String latestVersion;
    private AlertDialog dialog;
    private String ownerAuthorityStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        long delayTime = 0;

        if (getIntent().getBooleanExtra(Constants.IntentKeys.SHOW_CONTENT_VIEW,true))
        {
            setContentView(R.layout.splash);
            delayTime = 3000;
        }
        else
        {
            Common.Log.i("? - No need to show Content View.");
        }

        sp = Common.getDefaultSP(this);

        editor = sp.edit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                getCurrentVersion();

            }
        },delayTime);




        /*Google Analytics Starts*/
        TATX application = (TATX) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        Common.Log.i("? - Setting screen name: " + "TATX Driver App");
        mTracker.setScreenName("TATX Driver App");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        /*Google Analytics Ends*/




    }

    private void getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
        System.out.println("? - My local app version====>>>>>" + currentVersion);

//        Common.customToast(this,"currentVersion : "+currentVersion);




        if (!Common.haveInternet(getApplicationContext()))
        {

            Common.showInternetUnavailableDialog(this);



        }
        else
        {

//            setContentView(R.layout.splash);

            new GetLatestVersion().execute();

        }


    }


    public void initializedAll()
    {
        if (Common.getDefaultSP(this).getBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, false))
        {


            String customerLoginJson = Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.DRIVER_LOGIN_HASH_MAP, null);

            HashMap params = new Gson().fromJson(customerLoginJson, new TypeToken<HashMap<String, String>>() { }.getType());

            params.put(ServiceUrls.ApiRequestParams.TOKEN, regId);

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.DRIVER_LOGIN,params);




        }
        else
        {
            Intent intent = new Intent(SplashActivity.this, SignupLoginActivity.class);
            startActivity(intent);
            finish();
        }


    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);

            switch (apiResponseVo.requestname)
            {

                case ServiceUrls.RequestNames.DRIVER_LOGIN:

                    startActivity(new Intent(this,SignupLoginActivity.class));

                    finish();

                    break;


            }

            return;
        }


        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.DRIVER_LOGIN:

                 DriverLoginVo driverLoginVo = Common.getSpecificDataObject(apiResponseVo.data, DriverLoginVo.class);
                TATX.updateLanguage(this, Language.getEnumFieldByLanguageCode(Integer.parseInt(driverLoginVo.language)).getLocaleCode());

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CHECK_REGISTRATION_STATUS, null);

                break;

            case ServiceUrls.RequestNames.CHECK_REGISTRATION_STATUS:

                CheckRegistrationStatusVo checkRegistrationStatusVo = Common.getSpecificDataObject(apiResponseVo.data, CheckRegistrationStatusVo.class);


                Class<?> className = null;


                if (!checkRegistrationStatusVo.createDriverStep2) {
                    className = RegistrationActivity2.class;
                } else if (!checkRegistrationStatusVo.createDriverStep3) {
                    className = RegistrationActivity3.class;
                    ownerAuthorityStatus = checkRegistrationStatusVo.ownerAuthorityStatus == true ? "1" : "0";
                }


                if (className != null) {
                    Intent i = new Intent(getApplicationContext(), className);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    if (!checkRegistrationStatusVo.createDriverStep3) {
                        i.putExtra(Constants.IntentKeys.OWNER_AUTHORITY, ownerAuthorityStatus);
                        i.putExtra(Constants.IntentKeys.EMPLOYEE_TYPE, String.valueOf(checkRegistrationStatusVo.employeeType));
                        i.putExtra(Constants.IntentKeys.COUNTRY_OF_RESIDENCY, checkRegistrationStatusVo.countryOfResidency);
                        i.putExtra(Constants.IntentKeys.NATIONALITY, checkRegistrationStatusVo.nationality);
                        i.putExtra(Constants.IntentKeys.CAB_ID, String.valueOf(checkRegistrationStatusVo.cabId));

                    }

                    startActivity(i);

                    finish();
                } else {
/*
                    HashMap<String, String> params = new HashMap<>();
                    params.put(ServiceUrls.ApiRequestParams.TOKEN, Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.REG_ID, null));
                    params.put(ServiceUrls.ApiRequestParams.DEVICE_ID, Common.getDeviceId(this));
                    params.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);

                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.UPDATE_DEVICE_TOKEN, params);*/


                    HashMap<String, String> updateDeviceTokenParams = new HashMap<>();

                    updateDeviceTokenParams.put(ServiceUrls.ApiRequestParams.DEVICE_ID, Common.getDeviceId(this));

                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.DRIVER_ON_TRIP, updateDeviceTokenParams);

                }


                break;


            case ServiceUrls.RequestNames.UPDATE_DEVICE_TOKEN:

                UpdateDeviceTokenVo updateDeviceTokenVo = Common.getSpecificDataObject(apiResponseVo.data, UpdateDeviceTokenVo.class);

                Common.Log.i("updateDeviceTokenVo.message : " + updateDeviceTokenVo.message);
/*
                HashMap<String, String> updateDeviceTokenParams = new HashMap<>();

                updateDeviceTokenParams.put(ServiceUrls.ApiRequestParams.DEVICE_ID, Common.getDeviceId(this));

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.DRIVER_ON_TRIP, updateDeviceTokenParams);*/


                break;

            case ServiceUrls.RequestNames.DRIVER_ON_TRIP:

                DriverOnTripVo driverOnTripVo = Common.getSpecificDataObject(apiResponseVo.data, DriverOnTripVo.class);
                Common.Log.i("updateDeviceTokenVo.message : " + driverOnTripVo.toString());

                Intent intent = new Intent(this, GoogleMapDrawerActivity.class);

                intent.putExtra(Constants.KEY_1, driverOnTripVo);

                intent.putExtra(Constants.FROM_INVOICE, false);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

                finish();


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



                break;

        }


    }




    public void onCreateData()
    {
        applicationContext = getApplicationContext();
        if (Common.isGooglePlayServicesAvailable(this))
        {
            if (!sp.contains(Constants.SharedPreferencesKeys.REG_ID))
            {registerInBackground();
            }
            else if (sp.contains(Constants.SharedPreferencesKeys.REG_ID)) {
                regId = sp.getString(Constants.SharedPreferencesKeys.REG_ID, "");
                Log.d("regsAn", "Token: " + regId);
                if (regId == "") {
                    registerInBackground();
                } else {
                    initializedAll();
                }
            }
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
                    Log.d("regsAn", "GCM Token: " + regId);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {

                    storeRegIdinSharedPref(regId);

                    initializedAll();

                } else {
                    Toast.makeText(applicationContext, Common.getStringResourceText(R.string.reg_id_creation_failed) + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }


    private void storeRegIdinSharedPref(String regId) {

        editor.putString(Constants.SharedPreferencesKeys.REG_ID, regId);
        editor.commit();


    }


    private class GetLatestVersion extends AsyncTask<String, String, JSONObject>
    {


        private int executedCount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params)
        {

            getLatestVersion();

            return new JSONObject();
        }




        private void getLatestVersion()
        {


            try
            {

                org.jsoup.nodes.Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.tatx.partnerapp").timeout(30*1000).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();
                System.out.println("? - Latest version of my app from playstore====>>>>"+latestVersion);
            }
            catch (Exception e)
            {

                e.printStackTrace();

                Common.Log.i("?????-Inside Exception. executedCount : "+executedCount);

                if (executedCount < 100)
                {

                    executedCount++;

                    getLatestVersion();

                }
                else
                {

                    Common.customToast(SplashActivity.this,"OOPS! Something went Wrong.\nPlease Try Again Later.");

                }



            }

        }





        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            if(latestVersion!=null)
            {

//                if (!currentVersion.equalsIgnoreCase(latestVersion))

                Common.Log.i("Common.versionCompare(currentVersion,latestVersion) : "+Common.versionCompare(currentVersion,latestVersion));

                if (Common.versionCompare(currentVersion,latestVersion) < 0)
                {
                    showUpdateDialog();
                }
                else
                {

                    onCreateData();




                }
            }

            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog()
    {

        Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS,false);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Common.getStringFromResources(R.string.a_new_update_is_available));
        builder.setPositiveButton(Common.getStringResourceText(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tatx.partnerapp"));


                startActivity(intent);




            }
        });



        builder.setCancelable(false);
        dialog = builder.show();
    }






    @Override
    protected void onInternetConnected()
    {

        super.onInternetConnected();

        getCurrentVersion();

    }






}
