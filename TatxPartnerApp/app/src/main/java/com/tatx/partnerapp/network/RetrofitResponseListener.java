package com.tatx.partnerapp.network;

import com.tatx.partnerapp.pojos.ApiResponseVo;

import java.util.HashMap;

public interface RetrofitResponseListener
{


    void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId);



}
