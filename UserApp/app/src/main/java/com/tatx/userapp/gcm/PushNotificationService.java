package com.tatx.userapp.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by kundan on 10/22/2015.
 */
public class PushNotificationService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        //createNotification(mTitle, push_msg);
    }
}
