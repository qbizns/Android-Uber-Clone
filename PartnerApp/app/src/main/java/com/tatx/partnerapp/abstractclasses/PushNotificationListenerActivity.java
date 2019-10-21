package com.tatx.partnerapp.abstractclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.gcm.ApplicationConstants;
import com.tatx.partnerapp.pojos.PushNotificationResponseVo;


/**
 * Created by user on 07-07-2016.
 */
public abstract class PushNotificationListenerActivity extends NetworkChangeListenerActivity
{

    //public static final String PUSH_NOTIFICATION_RECEIVED = TATX.getInstance().getPackageName() + ".PUSH_NOTIFICATION_RECEIVED";
    public static final String PUSH_NOTIFICATION_RECEIVED = "PUSH_NOTIFICATION_RECEIVED";
    public static final String UN_READ_NOTIFICATION_COUNT_CHANGE = "UN_READ_NOTIFICATION_COUNT_CHANGE";
    private OnUnReadNotificationCountChangeListener onUnReadNotificationCountChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);


        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(PUSH_NOTIFICATION_RECEIVED);

        intentFilter.addAction(UN_READ_NOTIFICATION_COUNT_CHANGE);

        registerReceiver(broadcastReceiver,intentFilter);



    }

    protected void setUnReadNotificationCountChangeListener(OnUnReadNotificationCountChangeListener onUnReadNotificationCountChangeListener)
    {
        this.onUnReadNotificationCountChangeListener = onUnReadNotificationCountChangeListener;
    }


    public interface OnUnReadNotificationCountChangeListener
    {
        void onUnReadNotificationCountChage(int count);
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {


        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction())
            {
                case PUSH_NOTIFICATION_RECEIVED:

                    handlePushNotificationReceived(intent);

                    break;

                case UN_READ_NOTIFICATION_COUNT_CHANGE:
                    handleUnReadNotificationCountChange(intent);
                    break;

            }



        }



    };

    private void handleUnReadNotificationCountChange(Intent intent)
    {
        int count = intent.getIntExtra(Constants.KEY_1, Constants.INTENT_INTEGER_DEFAULT_VALUE);

        Common.Log.i("? - count : "+count);

        onUnReadNotificationCountChangeListener.onUnReadNotificationCountChage(count);
    }


    private void handlePushNotificationReceived(Intent intent) {
        Common.Log.i("start of PushNotificationBroadCastReceiver - onReceive.");

        Common.Log.i("PUSH_NOTIFICATION_RECEIVED : " + PUSH_NOTIFICATION_RECEIVED);

        Common.Log.i("intent.getExtras().getString(ApplicationConstants.MSG_KEY) : " + intent.getBundleExtra(Constants.KEY_1).getString(ApplicationConstants.MSG_KEY));

        String gcmMessageData = intent.getBundleExtra(Constants.KEY_1).getString(ApplicationConstants.MSG_KEY);

        PushNotificationResponseVo pushNotificationResponseVo = Common.getCustomGson().fromJson(gcmMessageData, PushNotificationResponseVo.class);

        Common.Log.i("????? - pushNotificationResponseVo : " + pushNotificationResponseVo);

        onPushNotificationReceived(pushNotificationResponseVo);

        Common.Log.i("end of PushNotificationBroadCastReceiver - onReceive.");
    }


    protected abstract void onPushNotificationReceived(PushNotificationResponseVo pushNotificationResponseVo);


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);


    }







}
