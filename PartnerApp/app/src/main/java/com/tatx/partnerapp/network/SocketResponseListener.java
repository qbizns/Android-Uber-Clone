package com.tatx.partnerapp.network;


import com.tatx.partnerapp.pojos.ApiResponseVo;

public interface SocketResponseListener
{


    void onSocketMessageReceived(ApiResponseVo apiResponseVo, int requestId);



}
