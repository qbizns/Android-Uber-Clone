package com.tatx.userapp.broadcastreveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tatx.userapp.abstractclasses.NetworkChangeListenerActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;

public class NetworkChangeReceiver extends BroadcastReceiver
{

	private static boolean isNotFirstTime;
	private static NetworkInfo previousActiveNetInfo;

	@Override
	public void onReceive(final Context context, final Intent intent)
    {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

		Common.Log.i("Inside - activeNetInfo : "+activeNetInfo);

		if (isNotFirstTime && previousActiveNetInfo == activeNetInfo)
		{

			return;

		}

		previousActiveNetInfo = activeNetInfo;

		isNotFirstTime = true;

		Common.Log.i("Inside NetworkChangeReceiver - onReceive");


		int status = NetworkUtil.getConnectivityStatus(context);

		Intent broadcastIntent = new Intent(NetworkChangeListenerActivity.NETWORK_CHANGE_RECEIVED);

		broadcastIntent.putExtra(Constants.KEY_1,status);

		context.sendBroadcast(broadcastIntent);


		if(status == 0)
		{
//			Common.customToast(context, "NCR - "+Constants.NO_INTERNET_CONNECTION_MESSAGE);
			Common.Log.i("Inside NCR - "+Constants.NO_INTERNET_CONNECTION_MESSAGE);
		}
		else
		{
//			Common.customToast(context,"NCR - Internet Connected.");
			Common.Log.i("Inside NCR - Internet Connected.");
		}



    }






}
