package com.tatx.userapp.broadcastreveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.tatx.userapp.activities.VerificationActivity;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.interfaces.SmsListener;

/**
 * Created by Jithu on 10/23/2015.
 */
public class ReadIncomingSms extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        Log.d("sms",bundle.toString());

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

                       /* VerificationActivity loginActivity =VerificationActivity.instance();
                            loginActivity.receivedSmS(message);*/
                        Log.d("sms",message);
                     //   }


                        mListener.messageReceived(message);

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
