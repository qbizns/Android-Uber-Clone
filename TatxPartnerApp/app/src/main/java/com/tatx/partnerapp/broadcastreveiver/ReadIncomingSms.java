package com.tatx.partnerapp.broadcastreveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.tatx.partnerapp.activities.VerificationActivity;
import com.tatx.partnerapp.commonutills.Constants;


/**
 * Created by Jithu on 10/23/2015.
 */
public class ReadIncomingSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get(Constants.PDUS);
                Log.d("sms",pdusObj.toString());
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    try {
                      //  if (senderNum.equals(Constants.SMS_ALERT)) {

                        VerificationActivity loginActivity =VerificationActivity.instance();
                            loginActivity.receivedSmS(message);
                        Log.d("sms",message);
                     //   }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
