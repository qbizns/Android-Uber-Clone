package com.tatx.userapp.network;


import com.tatx.userapp.pojos.ApiResponseVo;

public interface SocketResponseListener
{


    void onSocketMessageReceived(ApiResponseVo apiResponseVo, int requestId);



}
