package com.tatx.partnerapp.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

import com.tatx.partnerapp.application.TATX;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.dataset.CommonRequestKey;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class RetrofitRequester {
    private final RetrofitResponseListener retrofitResponseListener;
    private final Context context;
    private ProgressDialog progressDialog;

    public RetrofitRequester(RetrofitResponseListener retrofitResponseListener) {

        this.retrofitResponseListener = retrofitResponseListener;

        this.context = (Context) retrofitResponseListener;

    }

    public void sendStringRequest(String requestName, HashMap<String, String> requestParams) {

        sendStringRequest(requestName, requestParams, -1);

    }



    public void sendStringRequest(String requestName, final HashMap<String, String> requestParams, final int requestId, boolean showProgressDialog)
    {





        if (requestId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {
            Common.showContentView((Activity) context, false);
        }

        if (!Common.haveInternet(context)) {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }



        if (showProgressDialog) {

            progressDialog = Common.showProgressDialog(context);

        }


        CommonRequestKey commonRequestKey = new CommonRequestKey();
        commonRequestKey.setRequesterid(String.valueOf(Common.getUserIdFromSP(context)));
        commonRequestKey.setRequestname(requestName);
        commonRequestKey.setRequestparameters(new Gson().toJson(requestParams));
        Common.Log.i("commonRequestKey.toString() : " + commonRequestKey.toString());

        Common.Log.i("? - API Request : "+new Gson().toJson(commonRequestKey));


/*

        if(requestName.equals(ServiceUrls.RequestNames.CREATE_DRIVER_STEP2))
        {
            Common.Log.i("? - Not Processing "+ServiceUrls.RequestNames.CREATE_DRIVER_STEP2+" Request.");

            Common.dismissProgressDialog(progressDialog);


            return;
        }

*/






        TATX.getInstance().getRetrofitAPI().callApiService(commonRequestKey, new Callback<ApiResponseVo>() {
            @Override
            public void success(ApiResponseVo apiResponseVo, Response response) {
                Common.dismissProgressDialog(progressDialog);

                Common.Log.i("Inside Retrofit Success of " + getClass().getSimpleName());

                Common.Log.i("? - API Response : " + new Gson().toJson(apiResponseVo));

                Common.Log.i("response : " + response);

                retrofitResponseListener.onResponseSuccess(apiResponseVo, requestParams, requestId);

                if (requestId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {
                    Common.showContentView((Activity) context, true);
                }



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
