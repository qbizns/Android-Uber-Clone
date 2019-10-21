package com.tatx.partnerapp.network;

import com.tatx.partnerapp.dataset.CommonRequestKey;
import com.tatx.partnerapp.pojos.ApiResponseVo;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.mime.TypedFile;


public interface RetrofitAPI
{
    @Headers("Cache-Control: no-cache")
    @POST("/api?api_token=tatx123")
    void callApiService(@Body CommonRequestKey login, Callback<ApiResponseVo> response);

    @Multipart
    @POST("/api?api_token=tatx123")
    void uploadImages(
                      @Part("multipart") String multipart,
                      @Part("requestparameters") CommonRequestKey requestparameters,
                      @PartMap Map<String,TypedFile> files,
                      Callback<ApiResponseVo> response);




}
