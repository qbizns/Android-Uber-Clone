package com.tatx.partnerapp.broadcastreveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.SocketResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.OnSocketOpenVo;
import com.tatx.partnerapp.services.MyService;

public class AlarmReceiver extends BroadcastReceiver implements SocketResponseListener
{

    @Override
    public void onReceive(Context context, Intent intent)
    {

        // For our recurring task, we'll just display a message
        Common.Log.i("?????-I'm running");


        if (Common.getLoginStatusFromSP())
        {
            MyService.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.UPDATE_SOCKET_CONNECTIVITY, null, false);

            Common.Log.i("?????-Below - sendSocketRequest - ServiceUrls.RequestNames.UPDATE_SOCKET_CONNECTIVITY");

        }


    }

    @Override
    public void onSocketMessageReceived(ApiResponseVo apiResponseVo, int requestId)
    {

        Common.Log.i("?????-Response : " + ServiceUrls.RequestNames.UPDATE_SOCKET_CONNECTIVITY);


        switch (apiResponseVo.requestname)
        {



            case ServiceUrls.RequestNames.UPDATE_SOCKET_CONNECTIVITY:

//                OnSocketOpenVo OnSocketOpenVo = Common.getSpecificDataObject(apiResponseVo.data, OnSocketOpenVo.class);

                Common.Log.i("????? - AlarmReceiver Response : " + ServiceUrls.RequestNames.UPDATE_SOCKET_CONNECTIVITY);

                break;



        }


    }

}