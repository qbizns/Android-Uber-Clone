package com.tatx.userapp.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.tatx.userapp.commonutills.Common;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent)
	{

		Common.Log.i("Inside GcmBroadcastReceiver.");

		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMNotificationIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);


	}
}
