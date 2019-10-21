package com.tatx.userapp.application;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.codebutler.android_websockets.WebSocketClient;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.okhttp.OkHttpClient;
import com.tatx.userapp.R;
import com.tatx.userapp.activities.GoogleMapDrawerActivity;
import com.tatx.userapp.activities.LoginActivity;
import com.tatx.userapp.activities.SplashScreen;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.dataset.CommonRequestKey;
import com.tatx.userapp.menuactivity.UpdateProfileActivity;
import com.tatx.userapp.network.RetrofitAPI;
import com.tatx.userapp.network.SocketResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;


import org.apache.http.message.BasicNameValuePair;

import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

/**
 * Created by user on 29-06-2016.
 */
public class TATX extends Application implements WebSocketClient.Listener
{


    private static TATX instance;
    private RetrofitAPI retrofitAPI;
    private int requestId;
    public static final boolean DEVELOPER_MODE = false;

    public void setSocketResponseListener(SocketResponseListener socketResponseListener) {
        this.socketResponseListener = socketResponseListener;
    }

    private SocketResponseListener socketResponseListener;
    private WebSocketClient webSocketClient;
    private ProgressDialog progressDialog;
    private static final String TWITTER_KEY = "L12ecpXldANR8TTycz0QcOIec";
    private static final String TWITTER_SECRET = "2TtD87oWERfyjL0ykZDtSvEvslLAoCjncX62b9y3Z0Rk9tIb5r";

    @Override
    public void onCreate()
    {
        updateLanguage(this);
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        instance = this;

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS);
        okHttpClient.setRetryOnConnectionFailure(true);
        okHttpClient.setWriteTimeout(Constants.WRITE_TIME_OUT, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(ServiceUrls.CURRENT_ENVIRONMENT.getApiUrl())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("TATX"))
                .build();


        retrofitAPI = restAdapter.create(RetrofitAPI.class);

       /* ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();

        BasicNameValuePair basicNameValuePair = new BasicNameValuePair("Cache-Control", "no-cache");

        arrayList.add(basicNameValuePair);

        webSocketClient = new WebSocketClient(URI.create(ServiceUrls.URL_WEBSOCKET), this, arrayList);

        webSocketClient.connect();*/


        createAndConnectToSocket();


        if (DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {


            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());


        }

        initImageLoader(getApplicationContext());

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

        Common.Log.i("? - Locale.getDefault().toString() : "+Locale.getDefault().toString());


        if (ctx instanceof UpdateProfileActivity)
        {


//            GoogleMapDrawerActivity.getInstatnce().recreate();

            Common.refreshActivity(GoogleMapDrawerActivity.getInstatnce());


            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ((Activity)ctx).finish();

                }
            },500);


        }

/*

        if(ctx instanceof SplashScreen || ctx instanceof LoginActivity || ctx instanceof UpdateProfileActivity)
        {

            Common.setLocaleToSP(ctx, localeCode);

        }
*/




//        Common.setLocaleToSP(ctx, localeCode);



    }


    public static TATX getInstance()
    {
        return instance;
    }

    public RetrofitAPI getRetrofitAPI()
    {
        return retrofitAPI;
    }


    @Override
    public void onConnect()
    {
        Common.Log.i("onConnect");
    }

    @Override
    public void onMessage(String message)
    {
//        isWebSocketConnected = true;

        Common.Log.i("onMessage - String");

        Common.Log.i("? - Socket Response - jsonString : "+message);

        final ApiResponseVo apiResponseVo = new Gson().fromJson(message, ApiResponseVo.class);

        if(socketResponseListener != null && apiResponseVo != null)
        {
            ((Activity)socketResponseListener).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Common.Log.i("socketResponseListener.getClass().getSimpleName() : "+socketResponseListener.getClass().getSimpleName());
                    socketResponseListener.onSocketMessageReceived(apiResponseVo,requestId);
                    Common.dismissProgressDialog(progressDialog);

                }
            });

        }


    }

    @Override
    public void onMessage(byte[] data)
    {

        Common.Log.i("onMessage - byte[]");

    }

    @Override
    public void onDisconnect(int code, String reason)
    {

        Common.Log.i("onDisconnect");
        Common.Log.i("code : "+code);
        Common.Log.i("reason : "+reason);

    }

    @Override
    public void onError(Exception error)
    {

        Common.Log.i("? - onError");

        Common.Log.i("? - error.toString() :  "+error.toString());

        Common.Log.i("? - error.getMessage() :  "+error.getMessage());

        Common.Log.i("? - error.getClass() :  "+error.getClass());

        error.printStackTrace();


        if (error instanceof ConnectException)
        {
//            Common.customToast((Activity)socketResponseListener,"ConnectException");
            Common.Log.i("ConnectException");
        }
        else if(error instanceof UnknownHostException)
        {
//            Common.customToast((Activity)socketResponseListener,"UnknownHostException");
            Common.Log.i("UnknownHostException");
        }
        else
        {

            Common.dismissProgressDialog(progressDialog);

//            createAndConnectToSocket();
        }


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        createAndConnectToSocket();


//        Common.customToast((Activity)socketResponseListener, Constants.UNABLE_TO_CONNECT_OUR_SERVER,Toast.LENGTH_SHORT);

        Common.Log.i("? - Constants.UNABLE_TO_CONNECT_OUR_SERVER (onError): "+Constants.UNABLE_TO_CONNECT_OUR_SERVER);


    }



    public void sendSocketRequest(final SocketResponseListener socketResponseListener, String requestName, HashMap<String, String> requestParams, final boolean showProgressDialog, int requestId)
    {


        Common.Log.i("? - requestName : "+requestName);

        Common.Log.i("? - sendSocketRequest() : "+socketResponseListener.getClass().getSimpleName()+" : "+socketResponseListener.hashCode());

        CommonRequestKey commonRequestKey = new CommonRequestKey();

        commonRequestKey.setRequesterid(String.valueOf(Common.getUserIdFromSP(this)));

        commonRequestKey.setRequestname(requestName);

        commonRequestKey.setRequestparameters(new Gson().toJson(requestParams));

        Common.Log.i("sendSocketRequest - commonRequestKey.toString() : " + commonRequestKey.toString());

        final String jsonString = new Gson().toJson(commonRequestKey);

        Common.Log.i("? - SocketRequest - jsonString : " + jsonString);

        this.socketResponseListener = socketResponseListener;

        this.requestId = requestId;

        if(!Common.haveInternet(this))
        {

            Common.customToast(getApplicationContext(), Constants.NO_INTERNET_CONNECTION_MESSAGE);
            Common.Log.i(Constants.NO_INTERNET_CONNECTION_MESSAGE);

            return;

        }



        if (showProgressDialog)
        {

            progressDialog = Common.showProgressDialog((Activity)socketResponseListener);

        }




        if(!webSocketClient.isConnected())
        {

            new CountDownTimer(20000,1000)
            {

                @Override
                public void onTick(long millisUntilFinished)
                {

                    Common.Log.i("? - Seconds Remaining: " + millisUntilFinished / 1000);

                    Common.Log.i("? - Socket not Connected.");

//                    webSocketClient.connect();


                    createAndConnectToSocket();



                    Common.Log.i("? - webSocketClient.isConnected() : "+webSocketClient.isConnected());

                    if(webSocketClient.isConnected())
                    {
                        Common.Log.i("? - Socket Connection done.");
                        Common.dismissProgressDialog(progressDialog);
                        this.cancel();
                    }



                }

                @Override
                public void onFinish()
                {

                    Common.Log.i("CountDown Completed.");

                    Common.dismissProgressDialog(progressDialog);

                    if (showProgressDialog)
                    {
                        Common.customToast((Activity)socketResponseListener, Constants.UNABLE_TO_CONNECT_OUR_SERVER, Toast.LENGTH_SHORT);
                    }

                    Common.Log.i("? - Constants.UNABLE_TO_CONNECT_OUR_SERVER (if): "+Constants.UNABLE_TO_CONNECT_OUR_SERVER);


                }



            }.start();


        }
        else
        {

            Common.Log.i("? - Before sending data to socket.");

            webSocketClient.send(jsonString);


        }






    }

    private void createAndConnectToSocket()
    {

       /* ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();

        BasicNameValuePair basicNameValuePair = new BasicNameValuePair("Cache-Control", "no-cache");

        arrayList.add(basicNameValuePair);*/

        webSocketClient = new WebSocketClient(URI.create(ServiceUrls.CURRENT_ENVIRONMENT.getSocketUrl()), this, null);

        webSocketClient.connect();



    }


    public void sendSocketRequest(SocketResponseListener socketResponseListener, String requestName, HashMap<String, String> requestParams)
    {

        sendSocketRequest(socketResponseListener,requestName,requestParams,true,-1);

    }


    public void sendSocketRequest(SocketResponseListener socketResponseListener, String requestName, HashMap<String, String> requestParams, int requestId)
    {

        sendSocketRequest(socketResponseListener,requestName,requestParams,true,requestId);

    }

    public void sendSocketRequest(SocketResponseListener socketResponseListener, String requestName, HashMap<String, String> requestParams, boolean showProgressDialog)
    {
        sendSocketRequest(socketResponseListener,requestName,requestParams,showProgressDialog,-1);
    }
/*

    public void setDefaultLanguage()
    {
        String local = Locale.getDefault().getLanguage();
        Locale locale = new Locale(local);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
*/



    public static void initImageLoader(Context context)
    {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(options)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
