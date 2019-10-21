package com.tatx.userapp.abstractclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.tatx.userapp.application.TATX;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by user on 18-07-2016.
 */
public class NetworkChangeListenerActivity extends BaseActivity
{

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public static final String NETWORK_CHANGE_RECEIVED = TATX.getInstance().getPackageName() + ".NETWORK_CHANGED";

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

        Common.dismissInternetUnavailableDialog();

    }

    protected void onInternetDisconnected()
    {
        Common.Log.i("? - Inside - onInternetDisconnected()");

        Common.showInternetUnavailableDialog(NetworkChangeListenerActivity.this);
    }


}
