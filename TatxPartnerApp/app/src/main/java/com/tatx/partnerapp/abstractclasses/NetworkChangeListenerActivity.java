package com.tatx.partnerapp.abstractclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.tatx.partnerapp.application.TATX;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.services.MyService;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by user on 18-07-2016.
 */
public class NetworkChangeListenerActivity extends BaseActivity
{

    public static final String NETWORK_CHANGE_RECEIVED = TATX.getInstance().getPackageName() + ".NETWORK_CHANGED";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        registerReceiver(broadcastReceiver, new IntentFilter(NETWORK_CHANGE_RECEIVED));

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            Common.Log.i("Inside BroadcastReceiver - onReceive");

            Common.Log.i("? - intent.getIntExtra(Constants.KEY_1, 0) : "+intent.getIntExtra(Constants.KEY_1, 0));

            if (intent.getIntExtra(Constants.KEY_1, 0) == 0)
            {
                onInternetDisconnected();

            }
            else
            {
                onInternetConnected();

            }



        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    protected void onInternetConnected()
    {

        Common.Log.i("? - Inside - onInternetConnected()");

        MyService.getInstance().sendSocketInitiationRequest();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Common.dismissInternetUnavailableDialog();
            }

        },5000);






    }

    protected void onInternetDisconnected()
    {
        Common.Log.i("? - Inside - onInternetDisconnected()");

        Common.showInternetUnavailableDialog(NetworkChangeListenerActivity.this);
    }

}
