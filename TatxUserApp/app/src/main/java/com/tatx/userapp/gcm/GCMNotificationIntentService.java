package com.tatx.userapp.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.PushNotificationListenerActivity;
import com.tatx.userapp.activities.InvoiceActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.pojos.EndTripVo;
import com.tatx.userapp.pojos.OnTrip;
import com.tatx.userapp.pojos.PushNotificationResponseVo;

import org.json.JSONException;
import org.json.JSONObject;

public class GCMNotificationIntentService extends IntentService
{

	public static final int notifyID = 9001;


	String datetime;

	public static final String ID="ID";

	private SharedPreferences sp;

	public static final String ACTION_MyUpdate = "com.example.androidintentservice.UPDATE";

	private int tripid;

	private int orderid;

	private long mobile;

	private String drivername;

	private String amount;

	private String pickup_address;

	private String drop_address;


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
        String code= null;
        String readStatus= "0";
		JSONObject obj=null;


		sp = PreferenceManager.getDefaultSharedPreferences(this);

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
                    code=obj.getString(Constants.CODE);
                    description=obj.getString(Constants.DESCRIPTION);
					Log.d("pushdata",message.toString());


				} catch (Exception e) {
					e.printStackTrace();
				}
				switch (Integer.parseInt(code)){
					case 20000:
						jsonParsingFor_20000(obj);
						sendNotification(sms);
						break;
					case 20001:
						jsonP_20001_trip_started(obj);
						sendNotification(sms);
						break;
					case 20002:
						jsonParsingFor_20002(obj,sms);

						break;
					case 20003:
						jsonParsingFor_20003_orderInitialted(obj);
						break;
					case 20004:
						sendNotification(sms);
						break;

					case 20005:
						sendNotification(sms);
						break;

					case 20006:
						sendNotification(sms);
						break;

					case 20007:
						sendNotification(sms);
						break;

					case ServiceUrls.PushResponseCodes.FROM_NOTIFICATION_MANAGEMENT_20008:

						/*sendNotification(sms);

						String gcmMessageData  = intent.getExtras().getString(ApplicationConstants.MSG_KEY);

						Common.Log.i("? - gcmMessageData : "+gcmMessageData);

						PushNotificationResponseVo pushNotificationResponseVo = Common.getSpecificDataObject(gcmMessageData, PushNotificationResponseVo.class);


//						PushNotificationResponseVo pushNotificationResponseVo = Common.getCustomGson().fromJson(gcmMessageData, PushNotificationResponseVo.class);


*//*

						String jsonString = new Gson().toJson(gcmMessageData);

						Common.Log.i("? - jsonString : "+jsonString);

						PushNotificationResponseVo pushNotificationResponseVo = new Gson().fromJson(gcmMessageData, PushNotificationResponseVo.class);
*//*

						Common.sendUnReadNotificationCountBrodCast(this,pushNotificationResponseVo.unreadNotificationsCount);
*/
						break;

				}

			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}



	private void sendNotification(String msg) {
	        Intent resultIntent = getBaseContext().getPackageManager()
					.getLaunchIntentForPackage( getBaseContext().getPackageName() );
	        resultIntent.putExtra("msg", msg);
		    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	      //  PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_CANCEL_CURRENT);

	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	        mNotifyBuilder = new NotificationCompat.Builder(this)
	                .setContentTitle("Tatx")
	                .setContentText(msg)
	                .setSmallIcon(R.drawable.ic_untitled_1);
	        // Set pending intent
	      //  mNotifyBuilder.setContentIntent(resultPendingIntent);

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

	private void sendInvoiceNotification(String msg,EndTripVo endTripVo) {
		Intent resultIntent = new Intent(this, InvoiceActivity.class);
		resultIntent.putExtra(Constants.KEY_1, endTripVo);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("Tatx")
				.setContentText(msg)
				.setSmallIcon(R.drawable.ic_untitled_1);
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


	public void jsonParsingFor_20000(JSONObject jobj)
	{


		try {


			JSONObject dataJsonObject = jobj.getJSONObject("data");

			Common.Log.i("jsonParsingFor_20000(JSONObject jobj)");
			Common.Log.i("dataJsonObject.toString() : "+dataJsonObject.toString());

//			Data data = Common.getCustomGson().fromJson(dataJsonObject.toString(), Data.class);
			OnTrip onTrip = Common.getCustomGson().fromJson(dataJsonObject.toString(), OnTrip.class);

			Intent intentUpdate = new Intent();
			intentUpdate.setAction(ACTION_MyUpdate);
			intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
			intentUpdate.putExtra(Constants.KEY_1,onTrip);
			intentUpdate.putExtra("from","gcm");

			sendBroadcast(intentUpdate);


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void jsonP_20001_trip_started(JSONObject obj) {
		JSONObject jsonObject=null;

		try {
			jsonObject=obj.getJSONObject("data");

			Common.Log.i("jsonP_20001_trip_started(JSONObject jobj)");

			Common.Log.i("jsonObject.toString() : "+jsonObject.toString());

			tripid=jsonObject.getInt("tripid");
			orderid=jsonObject.getInt("orderid");
			drivername=jsonObject.getString("drivername");
			mobile=jsonObject.getLong("mobile");
			//datetime=jobj.getString(Constants.DATE_TIME);

			//send update
			Bundle bundle1=new Bundle();
			Intent intentUpdate = new Intent();
			intentUpdate.setAction(ACTION_MyUpdate);
			intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
			bundle1.putString("from","gcm");
			bundle1.putInt("code",20001);
			bundle1.putLong("mobile",mobile);
			bundle1.putString("drivername",drivername);
			bundle1.putInt("tripid",tripid);
			bundle1.putInt("orderid",orderid);


			intentUpdate.putExtras(bundle1);
			sendBroadcast(intentUpdate);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void jsonParsingFor_20002(JSONObject jobj,String message) {
		JSONObject jsonObject=null;

		try {
			jsonObject=jobj.getJSONObject("data");
			tripid=jsonObject.getInt("tripid");
			orderid=jsonObject.getInt("orderid");
			drivername=jsonObject.getString("drivername");
			mobile=jsonObject.getLong("mobile");
			amount=jsonObject.getString("amount");
			double pick_latitude = jsonObject.getDouble("pick_latitude");
			double pick_longitude = jsonObject.getDouble("pick_longitude");
			double drop_latitude = jsonObject.getDouble("drop_latitude");
			double drop_longitude = jsonObject.getDouble("drop_longitude");
			String distance = jsonObject.getString("distance");
			String duration = jsonObject.getString("duration");
			pickup_address=jsonObject.getString("pickup_location");
			drop_address=jsonObject.getString("drop_location");
			double distanceCost = jsonObject.getDouble("distance_cost");
			double durationCost = jsonObject.getDouble("duration_cost");
			String discount = jsonObject.getString("discount");
			String currency = jsonObject.getString("currency");
			double baseFare = jsonObject.getDouble("base_fare");
			double adjustment = jsonObject.getDouble("adjustment_amount");
			double tripAmount = jsonObject.getDouble("trip_amount");

			Intent dialogIntent = new Intent(getBaseContext(), InvoiceActivity.class);

			dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

			EndTripVo endTripVo = new EndTripVo();

			endTripVo.tripId = String.valueOf(tripid);

			endTripVo.orderId = String.valueOf(orderid);

			endTripVo.amount = Double.parseDouble(amount);

			endTripVo.pickLat = pick_latitude;

			endTripVo.pickLng = pick_longitude;

			endTripVo.dropLat = drop_latitude;

			endTripVo.dropLng = drop_longitude;

			endTripVo.distance = distance;

			endTripVo.duration=duration;

			endTripVo.pickupLocation=pickup_address;

			endTripVo.dropLocation=drop_address;

			endTripVo.distanceCost=distanceCost;

			endTripVo.discount=discount;

			endTripVo.durationCost=durationCost;

			endTripVo.adjustmentAmount =adjustment;

			endTripVo.baseFare=baseFare;

			endTripVo.tripAmount=tripAmount;

			endTripVo.currency=currency;

			dialogIntent.putExtra(Constants.KEY_1,endTripVo);

			getApplication().startActivity(dialogIntent);

			//sendInvoiceNotification(message,endTripVo);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void jsonParsingFor_20003_orderInitialted(JSONObject jobj) {
		JSONObject jsonObject=null;

		try {
			jsonObject=jobj.getJSONObject("data");
			tripid=jsonObject.getInt("tripid");
			orderid=jsonObject.getInt("orderid");
			int typeid = jsonObject.getInt("orderid");

//send update
			Bundle bundle1=new Bundle();
			Intent intentUpdate = new Intent();
			intentUpdate.setAction(ACTION_MyUpdate);
			intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
			bundle1.putString("from","gcm");
			bundle1.putInt("code",20003);
			bundle1.putInt("vcId",typeid);
			bundle1.putInt("tripid",tripid);
			bundle1.putInt("orderid",orderid);
			intentUpdate.putExtras(bundle1);
			sendBroadcast(intentUpdate);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
