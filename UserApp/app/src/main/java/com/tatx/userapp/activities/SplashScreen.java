package com.tatx.userapp.activities;

import android.app.Dialog;
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
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.NetworkChangeListenerActivity;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.enums.Language;
import com.tatx.userapp.gcm.ApplicationConstants;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.CustomerLoginVo;
import com.tatx.userapp.pojos.CustomerOnTripVo;
import com.tatx.userapp.pojos.EndTripVo;
import com.tatx.userapp.pojos.LastTripRatingVo;
import com.tatx.userapp.pojos.OnTrip;
import com.tatx.userapp.pojos.TripDetails;
import com.tatx.userapp.pojos.UpdateDeviceTokenVo;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;

public class SplashScreen extends NetworkChangeListenerActivity implements RetrofitResponseListener
{
    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private VideoView mVV;
    private VideoView imgAnim;
    private String currentVersion;
    private String latestVersion;
    private AlertDialog dialog;
    private CustomerLoginVo customerLoginVo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        /*Fabric.with(this, new Crashlytics());
        logUser();*/

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sp.edit();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Common.getDefaultSP(this).contains("locale")) {
            Common.setLanguage(this, Common.getDefaultSP(this).getString("locale", "en"));
        }

        long delayTime = 0;

        if (getIntent().getBooleanExtra(Constants.IntentKeys.SHOW_CONTENT_VIEW,true))
        {
            setContentView(R.layout.splash);

            imgAnim=(VideoView)findViewById(R.id.myvideoview);

            String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.videoforsplash;
            Uri uri = Uri.parse(uriPath);
            imgAnim.setVideoURI(uri);
            imgAnim.requestFocus();
            imgAnim.start();


            delayTime = 5000;
        }
        else
        {
            Common.Log.i("? - No need to show Content View.");
        }

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
        Common.Log.i("? - Setting screen name: " + "TATX Customer App");
        mTracker.setScreenName("TATX Customer App");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        /*Google Analytics Ends*/


    }

    @Override
    protected void onInternetConnected()
    {

        super.onInternetConnected();

        getCurrentVersion();

    }




    public void initilaizedAll()
    {


        Common.Log.i("? - Common.getDefaultSP(this).getBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS,false) : "+Common.getDefaultSP(this).getBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS,false));

        if(Common.getDefaultSP(this).getBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS,false))
        {


            String customerLoginJson = Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.CUSTOMER_LOGIN_HASH_MAP, null);

            HashMap params = new Gson().fromJson(customerLoginJson, new TypeToken<HashMap<String, String>>() { }.getType());

            if (params != null)
            {
                params.put(ServiceUrls.RequestParams.TOKEN, regId);
            }

            params.put(ServiceUrls.RequestParams.DEVICE_ID, Common.getDeviceId(this));

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CUSTOMER_LOGIN,params);



        }
        else
        {
            Intent intent = new Intent(SplashScreen.this, SignupLoginActivity.class);
            startActivity(intent);
            finish();
        }


    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {

        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());

        if (apiResponseVo.code != Constants.SUCCESS)
        {
           // Common.customToast(this, apiResponseVo.status);

            switch (apiResponseVo.requestname)
            {

                case ServiceUrls.RequestNames.CUSTOMER_LOGIN:

                    startActivity(new Intent(this,SignupLoginActivity.class));

                    finish();

                    break;


            }

            return;
        }


        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.UPDATE_DEVICE_TOKEN:

                UpdateDeviceTokenVo updateDeviceTokenVo = Common.getSpecificDataObject(apiResponseVo.data,UpdateDeviceTokenVo.class);

                Common.Log.i("updateDeviceTokenVo.message : "+updateDeviceTokenVo.message);

            //    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CUSTOMER_ON_TRIP, null, false);


                break;




            case ServiceUrls.RequestNames.CUSTOMER_ON_TRIP:

                CustomerOnTripVo customerOnTripVo = Common.getSpecificDataObject(apiResponseVo.data,CustomerOnTripVo.class);

                int tripStatus = customerOnTripVo.tripstatus;

                Common.Log.i("tripStatus : "+tripStatus);

                Intent intent = null;

                OnTrip ontrip = customerOnTripVo.ontrip;


                switch (tripStatus)
                {

                    case Constants.TripStatuses.NOT_ON_TRIP:

                        intent = new Intent(SplashScreen.this, GoogleMapDrawerActivity.class);
                        intent.putExtra(Constants.IntentKeys.PROMO_IMG,customerLoginVo.promoImg);
                        intent.putExtra(Constants.IntentKeys.UN_READ_NOTIFICATION_COUNT,customerLoginVo.unreadNotificationsCount);

                        break;

                    case Constants.TripStatuses.ON_TRIP_BUT_NOT_STARTED:
//                        intent.putExtra("action", 2);
                        intent = new Intent(SplashScreen.this, OnTripMapActivity.class);
                        intent.putExtra(Constants.KEY_1,ontrip);
                        intent.putExtra("action", 2);
                        intent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS, Common.getCompleteAddressString(this,ontrip.pickLatitude,ontrip.pickLongitude).getCompleteAddress());
                        intent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS, Common.getCompleteAddressString(this,ontrip.dropLatitude,ontrip.dropLongitude).getCompleteAddress());

                        break;


                    case Constants.TripStatuses.ON_TRIP_AND_STARTED:

                        intent = new Intent(SplashScreen.this, OnTripMapActivity.class);


                        intent.putExtra(Constants.KEY_1,ontrip);
//                        intent.putExtra("action", 2);
                        intent.putExtra("action", 4);
                        intent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS, Common.getCompleteAddressString(this,ontrip.pickLatitude,ontrip.pickLongitude).getCompleteAddress());
                        intent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS, Common.getCompleteAddressString(this,ontrip.dropLatitude,ontrip.dropLongitude).getCompleteAddress());

                        break;


                }

                startActivity(intent);

                finish();


                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.LAST_TRIP_RATING,null);


                break;


            case ServiceUrls.RequestNames.CUSTOMER_LOGIN:

                 customerLoginVo = Common.getSpecificDataObject(apiResponseVo.data, CustomerLoginVo.class);

                Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, true).commit();

                Common.getDefaultSP(this).edit().putInt(Constants.SharedPreferencesKeys.USERID, customerLoginVo.userid).commit();

                TATX.updateLanguage(this, Language.getEnumFieldByLanguageCode(Integer.parseInt(customerLoginVo.language)).getLocaleCode());

              /*  HashMap<String, String> params = new HashMap<>();
                params.put(ServiceUrls.RequestParams.TOKEN,Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.REG_ID,null));
                params.put(ServiceUrls.RequestParams.DEVICE_ID,Common.getDeviceId(this));
                params.put(ServiceUrls.RequestParams.DEVICE,Constants.ANDROID);

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.UPDATE_DEVICE_TOKEN,params);*/
                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CUSTOMER_ON_TRIP, null, false);

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

                    invoiceIntent.putExtra(Constants.IntentKeys.FROM_SPLASH_SCREEN,true);

                    startActivity(invoiceIntent);



                }
                else
                {

                    Common.Log.i("No need to show Last Trip Rating Screen.");

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
            {
                registerInBackground();
            }
            else
            {
                regId=sp.getString(Constants.SharedPreferencesKeys.REG_ID,null);
                if (regId==null)
                {
                    registerInBackground();
                }
                else
                {

                    initilaizedAll();
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
                    Log.d("regsAn","GCM Token: "+ regId);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {
                if (regId != null)
                {

                    Common.getDefaultSP(SplashScreen.this).edit().putString(Constants.SharedPreferencesKeys.REG_ID, regId).commit();
                    initilaizedAll();

                }
                else
                {
                    Toast.makeText(applicationContext, Common.getStringResourceText(R.string.reg_id_creation_failed) + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }




    private void getCurrentVersion()
    {


        PackageManager pm = this.getPackageManager();

        PackageInfo pInfo = null;

        try
        {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        }
        catch (PackageManager.NameNotFoundException e1)
        {
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

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject>
    {


        private int executedCount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {


            getLatestVersion();

            return new JSONObject();
        }

        private void getLatestVersion()
        {


            try
            {

                org.jsoup.nodes.Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.tatx.userapp").timeout(30*1000).get();

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

                    Common.customToast(SplashScreen.this,"OOPS! Something went Wrong.\nPlease Try Again Later.");

                }



            }


        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            if (latestVersion != null)
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
        builder.setTitle(Common.getStringResourceText(R.string.a_new_update_is_available));
        builder.setPositiveButton(Common.getStringResourceText(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tatx.userapp"));


                startActivity(intent);




            }
        });



        builder.setCancelable(false);
        dialog = builder.show();
    }


}
