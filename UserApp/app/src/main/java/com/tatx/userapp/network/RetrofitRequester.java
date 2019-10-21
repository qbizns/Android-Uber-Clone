package com.tatx.userapp.network;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.dataset.CommonRequestKey;
import com.tatx.userapp.pojos.ApiResponseVo;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class RetrofitRequester {
    private final RetrofitResponseListener retrofitResponseListener;
    private Activity activity;
    private Context context;
    private ProgressDialog progressDialog;

    public RetrofitRequester(RetrofitResponseListener retrofitResponseListener) {

        this.retrofitResponseListener = retrofitResponseListener;

        if(retrofitResponseListener instanceof Activity)
        {
        this.context = (Context) retrofitResponseListener;
            this.activity = (Activity) retrofitResponseListener;
        }
        else if(retrofitResponseListener instanceof Fragment)
        {
            this.context = ((Fragment) retrofitResponseListener).getActivity();
            this.activity = ((Fragment) retrofitResponseListener).getActivity();

        }
        else if(retrofitResponseListener instanceof android.support.v4.app.Fragment)
        {
            this.context = ((android.support.v4.app.Fragment) retrofitResponseListener).getActivity();
            this.activity = ((android.support.v4.app.Fragment) retrofitResponseListener).getActivity();

        }

    }

    public void sendStringRequest(String requestName, HashMap<String, String> requestParams) {

        sendStringRequest(requestName, requestParams, -1);

    }


    public void sendStringRequest(String requestName, final HashMap<String, String> requestParams, final int requestId, boolean showProgressDialog) {


        if (requestId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {
            Common.showContentView((Activity) context, false);
        }

        if (!Common.haveInternet(context)) {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }


        if (showProgressDialog) {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = Common.showProgressDialog(context);
                }
            });


        }


        CommonRequestKey commonRequestKey = new CommonRequestKey();
        commonRequestKey.setRequesterid(String.valueOf(Common.getUserIdFromSP(context)));
        commonRequestKey.setRequestname(requestName);
        commonRequestKey.setRequestparameters(new Gson().toJson(requestParams));
        Common.Log.i("commonRequestKey.toString() : " + commonRequestKey.toString());

        Common.Log.i("? - API Request : "+new Gson().toJson(commonRequestKey));


        TATX.getInstance().getRetrofitAPI().callApiService(commonRequestKey, new Callback<ApiResponseVo>() {
            @Override
            public void success(ApiResponseVo apiResponseVo, Response response) {

                Common.Log.i("Inside Retrofit Success of " + getClass().getSimpleName());

                Common.Log.i("? - API Response : " + new Gson().toJson(apiResponseVo));

                Common.Log.i("response : " + response);

                retrofitResponseListener.onResponseSuccess(apiResponseVo, requestParams, requestId);

                if (requestId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {
                    Common.showContentView((Activity) context, true);
                }

                Common.dismissProgressDialog(progressDialog);

            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();

                Common.Log.i("error.toString() : " + error.toString());

                Common.dismissProgressDialog(progressDialog);


            }

        });


    }


    public void sendStringRequest(String requestName, HashMap<String, String> requestParams, boolean showProgressDialog) {

        sendStringRequest(requestName, requestParams, -1, showProgressDialog);

    }


    public void sendStringRequest(String requestName, HashMap<String, String> requestParams, final int requestId) {
        sendStringRequest(requestName, requestParams, requestId, true);
    }



    public void sendMultiPartRequest(String requestName, final HashMap<String, String> requestParams, HashMap<String, TypedFile> filesHashMap)
    {

        if (!Common.haveInternet(context))
        {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }


        progressDialog = Common.showProgressDialog(context);




        CommonRequestKey commonRequestKey = new CommonRequestKey();
        commonRequestKey.setRequesterid(String.valueOf(Common.getUserIdFromSP(context)));
        commonRequestKey.setRequestname(requestName);
        commonRequestKey.setRequestparameters(new Gson().toJson(requestParams));

        Common.Log.i("? - Mupltipart Request : "+new Gson().toJson(commonRequestKey));




        TATX.getInstance().getRetrofitAPI().uploadImages( "1", commonRequestKey,filesHashMap, new Callback<ApiResponseVo>() {
            @Override
            public void success(ApiResponseVo apiResponseVo, Response response)
            {
                Common.Log.i("Inside Retrofit Success of " + getClass().getSimpleName());

                Common.Log.i("? - Mupltipart Response : " + new Gson().toJson(apiResponseVo));

                Common.Log.i("response : " + response);

                retrofitResponseListener.onResponseSuccess(apiResponseVo, requestParams, -1);

                Common.dismissProgressDialog(progressDialog);

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                Common.Log.i("error.toString() : " + error.toString());

                Common.dismissProgressDialog(progressDialog);

            }
        });





    }
}
