package com.tatx.partnerapp.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.okhttp.OkHttpClient;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitAPI;
import com.tatx.partnerapp.services.MyService;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

/**
 * Created by user on 29-06-2016.
 */
public class TATX extends Application
{


    private static TATX instance;

    private RetrofitAPI retrofitAPI;


    @Override
    public void onCreate()
    {
        updateLanguage(this);

        super.onCreate();


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });



        Fabric.with(this, new Crashlytics());

        instance = this;

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS);
        okHttpClient.setRetryOnConnectionFailure(true);
        okHttpClient.setWriteTimeout(Constants.WRITE_TIME_OUT, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(ServiceUrls.CURRENT_ENVIRONMENT.getApiUrl())
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setLog(new AndroidLog("TATX"))
                .build();


        retrofitAPI = restAdapter.create(RetrofitAPI.class);

        startService(new Intent(this, MyService.class));


        Common.Log.i("? - below startService() method.");



    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onTrimMemory(int level)
    {



        Common.Log.i("onTrimMemory"+level);
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                  freeMemory();

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                 freeMemory();

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }


    }

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static TATX getInstance()
    {
        return instance;
    }

    public RetrofitAPI getRetrofitAPI()
    {
        return retrofitAPI;
    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public void updateLanguage(Context ctx)
    {
//        updateLanguage(ctx, Common.getLocaleFromSP(ctx));
        updateLanguage(ctx, Locale.getDefault().getLanguage());
    }

    public static void updateLanguage(final Context ctx, String localeCode)
    {


        Configuration cfg = ctx.getResources().getConfiguration();

        cfg.locale = new Locale(localeCode);

        ctx.getResources().updateConfiguration(cfg, null);

        Common.Log.i("Locale.getDefault().toString() : "+Locale.getDefault().toString());

/*
        if (ctx instanceof DisplayProfileActivity)
        {


            GoogleMapDrawerActivity.getInstatnce().recreate();


            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ((Activity)ctx).finish();

                }
            },500);


        }*/

/*

        if(ctx instanceof SplashScreen || ctx instanceof LoginActivity || ctx instanceof UpdateProfileActivity)
        {

            Common.setLocaleToSP(ctx, localeCode);

        }
*/




//        Common.setLocaleToSP(ctx, localeCode);



    }


    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }


}
