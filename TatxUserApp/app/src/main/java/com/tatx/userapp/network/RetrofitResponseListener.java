package com.tatx.userapp.network;


import com.tatx.userapp.pojos.ApiResponseVo;

import java.util.HashMap;

public interface RetrofitResponseListener
{


    void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId);



}
