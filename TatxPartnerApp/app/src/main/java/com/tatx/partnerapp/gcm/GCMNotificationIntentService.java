package com.tatx.partnerapp.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.PushNotificationListenerActivity;
import com.tatx.partnerapp.activities.AcceptRejectRideActivity;
import com.tatx.partnerapp.activities.SplashActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.database.SqliteDB;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.UserLogoutVo;
import com.tatx.partnerapp.services.MyService;

import java.util.HashMap;
import java.util.Locale;

public class GCMNotificationIntentService extends IntentService implements RetrofitResponseListener {

	public static final int notifyID = 9001;

    SqliteDB sqliteDB;

	private SharedPreferences sp;

	private SharedPreferences.Editor editor;

	public static final String ACTION_MyUpdate = "com.example.androidintentservice.UPDATE";

    private int tripid;

    private int orderid;






	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}


	@Override
	protected void onHandleIntent(Intent intent)
	{



		Common.Log.i("start of IntentService.");

		Common.Log.i("intent.getExtras().getString(ApplicationConstants.MSG_KEY) : "+intent.getExtras().getString(ApplicationConstants.MSG_KEY));

		Intent broadCastIntent = new Intent();

		broadCastIntent.setAction(PushNotificationListenerActivity.PUSH_NOTIFICATION_RECEIVED);

		broadCastIntent.putExtra(Constants.KEY_1,intent.getExtras());

		sendBroadcast(broadCastIntent);

		Common.Log.i("PushNotificationBroadCastReceiver.PUSH_NOTIFICATION_RECEIVED : "+ PushNotificationListenerActivity.PUSH_NOTIFICATION_RECEIVED);

		Common.Log.i("End of IntentService.");


		String data= null;
        String description = null;
        String sms= null;
        int code= 0;
        String readStatus= "0";
		JSONObject obj=null;


		Common.Log.i("intent.toString() : "+intent.toString());

		Common.Log.i("intent.getExtras().toString() : "+intent.getExtras().toString());

		Common.Log.i("intent.getExtras().getString(ApplicationConstants.MSG_KEY) : "+intent.getExtras().getString(ApplicationConstants.MSG_KEY));


		sqliteDB=new SqliteDB(this);

		sp = PreferenceManager.getDefaultSharedPreferences(this);

		editor = sp.edit();

		Bundle extras = intent.getExtras();

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				String message=extras.getString(ApplicationConstants.MSG_KEY);
				try {
					obj=new JSONObject(message);
                    sms=obj.getString(Constants.MESSAGE);
                    data=obj.getString(Constants.DATA);
                    code=obj.getInt(Constants.CODE);
                    description=obj.getString(Constants.DESCRIPTION);
					Log.d("notificationdata",message+"hi "+data+description+sms);


				} catch (Exception e) {
					e.printStackTrace();
				}
				switch (code)
				{

					case 10001:

						Common.Log.i("????? - GCMNotificationIntentService :: sendLogoutRequest()");

						Common.sendLogoutRequest(this,1);

						break;

					case 10002:
						Bundle bundle2=new Bundle();
						Intent intentUpdate2 = new Intent();
						intentUpdate2.setAction(ACTION_MyUpdate);
						intentUpdate2.addCategory(Intent.CATEGORY_DEFAULT);
						bundle2.putString("from","gcm_canceled_trip");
						intentUpdate2.putExtras(bundle2);
						sendBroadcast(intentUpdate2);
						break;


				}

			}
		}

		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}


/*
	public void logoutApi(int logoutDueToPush)
	{

		Common.Log.i("????? - GCMNotificationIntentService :: sendLogoutRequest()");

		HashMap<String, String> requestParams = new HashMap();
		requestParams.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);
		requestParams.put(ServiceUrls.ApiRequestParams.DEVICE_ID, Common.getDeviceId(this));
		requestParams.put(ServiceUrls.ApiRequestParams.LOGOUT_DUE_TO_PUSH, String.valueOf(logoutDueToPush));

		new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.USER_LOGOUT, requestParams);


	}
*/




	private void sendNotification(String msg) {
	        Intent resultIntent = new Intent(this, AcceptRejectRideActivity.class);
			resultIntent.putExtra("tripid", tripid);
			resultIntent.putExtra("orderid",orderid);
	        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	        android.support.v4.app.NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	        mNotifyBuilder = new NotificationCompat.Builder(this)
	                .setContentTitle("Tatx")
	                .setContentText(msg)
	                .setSmallIcon( R.drawable.ic_untitled_1);
	        // Set pending intent
	        mNotifyBuilder.setContentIntent(resultPendingIntent);

	        // Set Vibrate, Sound and Light
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;
	        mNotifyBuilder.setDefaults(defaults);
	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        // Post a notification
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}


	@Override
	public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
		if (apiResponseVo.code != Constants.SUCCESS) {
			Common.customToast(this, apiResponseVo.status);
			return;
		}

		switch (apiResponseVo.requestname)
		{
			case ServiceUrls.RequestNames.USER_LOGOUT:

				Common.Log.i("????? - GCMNotificationIntentService :: case ServiceUrls.RequestNames.USER_LOGOUT:");


				/*UserLogoutVo userLogoutVo = Common.getSpecificDataObject(apiResponseVo.data, UserLogoutVo.class);

				Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, false).commit();

				Common.getDefaultSP(this).edit().putInt(Constants.SharedPreferencesKeys.USERID, 0).commit();

				Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.REG_ID, "").commit();

				Common.Log.i("Language Goo"+ Locale.getDefault().getDisplayLanguage());

				Common.customToast(this, userLogoutVo.status);

				MyService.getInstance().disconnectSocket();

				Intent intent = new Intent(this, SplashActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);

//				finish();*/

				Common.logoutResponseFunctionality(this,apiResponseVo);


				break;

		}
	}
}
