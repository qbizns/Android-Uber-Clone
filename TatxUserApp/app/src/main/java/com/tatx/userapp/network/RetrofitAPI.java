package com.tatx.userapp.network;

import com.tatx.userapp.dataset.CommonRequestKey;
import com.tatx.userapp.dataset.RestResponse;
import com.tatx.userapp.pojos.ApiResponseVo;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.mime.TypedFile;


public interface RetrofitAPI
{
    @POST("/api?api_token=tatx123")
    void callApiService(@Body CommonRequestKey login, Callback<ApiResponseVo> response);

    @Multipart
    @POST("/api?api_token=tatx123")
    void upload(@Part("image") TypedFile file,
                @Part("multipart") String multipart,
                @Part("requestparameters") CommonRequestKey requestparameters,
                Callback<RestResponse> response);


    @Multipart
    @POST("/api?api_token=tatx123")
    void uploadImages(
            @Part("multipart") String multipart,
            @Part("requestparameters") CommonRequestKey requestparameters,
            @PartMap Map<String,TypedFile> files,
            Callback<ApiResponseVo> response);




}
