package com.tatx.partnerapp.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.widget.Toast;

import com.codebutler.android_websockets.WebSocketClient;
import com.google.gson.Gson;
import com.tatx.partnerapp.activities.AcceptRejectRideActivity;
import com.tatx.partnerapp.broadcastreveiver.AlarmReceiver;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.dataset.CommonRequestKey;
import com.tatx.partnerapp.library.CountDownTimer;
import com.tatx.partnerapp.network.SocketResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.OnSocketOpenVo;
import com.tatx.partnerapp.pojos.OrderReceivedVo;
import com.tatx.partnerapp.pojos.SocketInitiationVo;


import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;


public class MyService extends IntentService implements WebSocketClient.Listener, SocketResponseListener
{
 private static boolean serviceAvailable;
// MediaPlayer myPlayer;

 private static final String TAG = "SKP";
 private WebSocketClient client;
 private boolean requestSent;

 private int requestId;
 private String currentResponseRequestName;

 public MyService()
 {
  super("Service SKP");
 }






 @Override
 public IBinder onBind(Intent intent) {
  return null;
 }

 @Override
 protected void onHandleIntent(Intent intent)
 {

  Common.Log.i("? - MyService - onHandleIntent().");

 }

 @Override
 public void onCreate()
 {

  Common.Log.i("? - MyService - onCreate().");


//  Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

  Common.Log.i("Service Created");


  instance = this;


/*

  myPlayer = MediaPlayer.create(this, R.raw.sun);
  myPlayer.setLooping(false); // Set looping
*/


  createAndConnectToSocket();


//  sendSocketInitiationRequest();


//  sendSocketRequestToOnSocketOpenService(1,-1);


 }



 public void setSocketResponseListener(SocketResponseListener socketResponseListener) {
  this.socketResponseListener = socketResponseListener;
 }




 private SocketResponseListener socketResponseListener;
 private WebSocketClient webSocketClient;
 private ProgressDialog progressDialog;


 private void createAndConnectToSocket()
 {



       /* ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();

        BasicNameValuePair basicNameValuePair = new BasicNameValuePair("Cache-Control", "no-cache");

        arrayList.add(basicNameValuePair);*/


  try {
   Thread.sleep(2000);
  } catch (InterruptedException e) {
   e.printStackTrace();
  }

  webSocketClient = new WebSocketClient(URI.create(ServiceUrls.CURRENT_ENVIRONMENT.getSocketUrl()), this, null);

  webSocketClient.connect();



 }




 @Override
 public int onStartCommand(Intent intent, int flags, int startId)
 {

//  Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

  Common.Log.i("Service Started");

//  myPlayer.start();

//  return super.onStartCommand(intent, flags, startId);

  return Service.START_STICKY;


 }

 @Override
 public void onDestroy() {
//  Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
  Common.Log.i("Service Stopped");

//  myPlayer.stop();
 }

 @Override
 public void onConnect()
 {

  Common.Log.i("onConnect - WebSocketClient");




 }

 @Override
 public void onMessage(String message)
 {
//        isWebSocketConnected = true;

  Common.Log.i("onMessage - String");

  Common.Log.i("?? - socketResponseListener : "+socketResponseListener);

  Common.Log.i("??? - socketResponseListener : "+((socketResponseListener == null) ? "null":socketResponseListener.getClass().getSimpleName())+" Socket Response - jsonString : "+message);

  final ApiResponseVo apiResponseVo = new Gson().fromJson(message, ApiResponseVo.class);

  currentResponseRequestName = (apiResponseVo != null) ? apiResponseVo.requestname : null;


//  Common.customToast(this,currentResponseRequestName);

  Common.Log.i("currentResponseRequestName : "+currentResponseRequestName);


  if(apiResponseVo != null)
  {


   if(socketResponseListener != null && socketResponseListener instanceof Activity)
   {
    ((Activity)socketResponseListener).runOnUiThread(new Runnable() {
     @Override
     public void run() {

      Common.Log.i("socketResponseListener.getClass().getSimpleName() : "+socketResponseListener.getClass().getSimpleName());
      socketResponseListener.onSocketMessageReceived(apiResponseVo,requestId);
      Common.dismissProgressDialog(progressDialog);

     }
    });




   }
   else
   {
    Common.Log.i("? - Not instance of Activity.");


    Common.Log.i("??? - requestId : "+requestId);

    onSocketMessageReceived(apiResponseVo,555888);
   }



  }










 }

 @Override
 public void onMessage(byte[] data)
 {

  Common.Log.i("onMessage - byte[]");

 }

 @Override
 public void onDisconnect(int code, String reason)
 {

  Common.Log.i("onDisconnect");
  Common.Log.i("code : "+code);
  Common.Log.i("reason : "+reason);



  createAndConnectToSocket();



 }

 @Override
 public void onError(Exception error)
 {

  Common.Log.i("onError");

  Common.Log.i("error.toString() :  "+error.toString());

  Common.Log.i("error.getMessage() :  "+error.getMessage());

  Common.Log.i("error.getClass() :  "+error.getClass());

  error.printStackTrace();


  if (error instanceof ConnectException)
  {
//            Common.customToast((Activity)socketResponseListener,"ConnectException");
   Common.Log.i("ConnectException");
  }
  else if(error instanceof UnknownHostException)
  {
//            Common.customToast((Activity)socketResponseListener,"UnknownHostException");
   Common.Log.i("UnknownHostException");
  }
  else
  {

   Common.dismissProgressDialog(progressDialog);

//            createAndConnectToSocket();
  }




  createAndConnectToSocket();


//        Common.customToast((Activity)socketResponseListener, Constants.UNABLE_TO_CONNECT_OUR_SERVER,Toast.LENGTH_SHORT);

  Common.Log.i("Constants.UNABLE_TO_CONNECT_OUR_SERVER (onError): "+ Constants.UNABLE_TO_CONNECT_OUR_SERVER);


 }


 public void sendSocketRequest(final SocketResponseListener socketResponseListener, String requestName, HashMap<String, String> requestParams, final boolean showProgressDialog, int requestId)
 {


  Common.Log.i("requestName : "+requestName);

  Common.Log.i("sendSocketRequest() : "+socketResponseListener.getClass().getSimpleName()+" : "+socketResponseListener.hashCode());

  CommonRequestKey commonRequestKey = new CommonRequestKey();

  commonRequestKey.setRequesterid(String.valueOf(Common.getUserIdFromSP(this)));

  commonRequestKey.setRequestname(requestName);

  commonRequestKey.setRequestparameters(new Gson().toJson(requestParams));

  Common.Log.i("sendSocketRequest - commonRequestKey.toString() : " + commonRequestKey.toString());

  final String jsonString = new Gson().toJson(commonRequestKey);

  Common.Log.i("?? - socketResponseListener : "+socketResponseListener.getClass().getSimpleName()+" Socket Request - jsonString : " + jsonString);

  this.socketResponseListener = socketResponseListener;

  this.requestId = requestId;

  if(!Common.haveInternet(this))
  {

            Common.customToast(getApplicationContext(), Constants.NO_INTERNET_CONNECTION_MESSAGE);
//   Common.customToast((Context) socketResponseListener, Constants.NO_INTERNET_CONNECTION_MESSAGE);
   Common.Log.i(Constants.NO_INTERNET_CONNECTION_MESSAGE);

   return;

  }



  if (showProgressDialog)
  {

   progressDialog = Common.showProgressDialog((Activity)socketResponseListener);

  }


  if (webSocketClient != null)
  {
   Common.Log.i("? - webSocketClient.isConnected() : "+webSocketClient.isConnected());
  }


  if(webSocketClient == null || !webSocketClient.isConnected())
  {

   new CountDownTimer(20000,1000)
   {

    @Override
    public void onTick(long millisUntilFinished)
    {

     Common.Log.i("Seconds Remaining: " + millisUntilFinished / 1000);

     Common.Log.i("Socket not Connected.");

//                    webSocketClient.connect();


     createAndConnectToSocket();


     Common.Log.i("webSocketClient.isConnected() : "+webSocketClient.isConnected());

     if(webSocketClient.isConnected())
     {
      Common.Log.i("Socket Connection done.");
      Common.dismissProgressDialog(progressDialog);
      cancel();
     }



    }

    @Override
    public void onFinish()
    {

     Common.Log.i("CountDown Completed.");

     Common.dismissProgressDialog(progressDialog);

     if (showProgressDialog)
     {

//      Common.customToast((Activity)socketResponseListener, Constants.UNABLE_TO_CONNECT_OUR_SERVER, Toast.LENGTH_SHORT);

      Common.Log.i(Constants.UNABLE_TO_CONNECT_OUR_SERVER);

     }

     Common.Log.i("Constants.UNABLE_TO_CONNECT_OUR_SERVER (if): "+Constants.UNABLE_TO_CONNECT_OUR_SERVER);


    }



   }.start();


  }
  else
  {

   Common.Log.i("Before sending data to socket.");

   webSocketClient.send(jsonString);


  }

  Common.Log.i("? - showProgressDialog : "+showProgressDialog);

  if (showProgressDialog)
  {
   checkResponseReceivedStatus(requestName);
  }


 }



 private void checkResponseReceivedStatus(final String requestName)
 {


  new CountDownTimer(20000,1000)
  {

   @Override
   public void onTick(long millisUntilFinished)
   {


    Common.Log.i("? - c.Resp.Name : "+currentResponseRequestName+" requestName : "+requestName+" R.Sec. : "+(millisUntilFinished)/1000+" Condition : "+ ((currentResponseRequestName != null) ? currentResponseRequestName.equals(requestName) : false));

    if (currentResponseRequestName != null && currentResponseRequestName.equals(requestName))
    {

     Common.Log.i("? - C.D.T.Cancel() : " + requestName);

     cancel();

    }

   }

   @Override
   public void onFinish()
   {

    String errorMessage = "We did not get any response for " + requestName + " Socket API Request.";

    Common.Log.i("? - errorMessage : "+errorMessage);

//                Common.customToast((Context) socketResponseListener,errorMessage);
//    Common.customToast((Context) socketResponseListener,"Oops! Something went wrong.");

    Common.Log.i("Oops! Something went wrong.");

    Common.dismissProgressDialog(progressDialog);


   }


  }.start();


 }



 public void sendSocketRequest(SocketResponseListener socketResponseListener, String requestName, HashMap<String, String> requestParams)
 {

  sendSocketRequest(socketResponseListener,requestName,requestParams,true,-1);

 }


 public void sendSocketRequest(SocketResponseListener socketResponseListener, String requestName, HashMap<String, String> requestParams, int requestId)
 {

  sendSocketRequest(socketResponseListener,requestName,requestParams,true,requestId);

 }

 public void sendSocketRequest(SocketResponseListener socketResponseListener, String requestName, HashMap<String, String> requestParams, boolean showProgressDialog)
 {
  sendSocketRequest(socketResponseListener,requestName,requestParams,showProgressDialog,-1);
 }



 public void disconnectSocket()
 {

  if(webSocketClient != null)
  {
   webSocketClient.disconnect();
  }

 }

 private static MyService instance;



 public static MyService getInstance()
 {


  for(;;)
  {

    if(instance != null)
    {
      break;
    }

  }



  return instance;







 }



 public void sendSocketInitiationRequest() {


  HashMap<String, String> params = new HashMap<>();
  params.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);
  params.put(ServiceUrls.ApiRequestParams.OS, Constants.ANDROID);

  sendSocketRequest(this, ServiceUrls.RequestNames.SOCKET_INITIATION, params, false);

  Common.Log.i("sendSocketInitiationRequest : " + this.getClass().getSimpleName() + " : " + this.hashCode());


  Common.Log.i("Inside sendSocketInitiationRequest().");

 }




 private void sendSocketRequestToOnSocketOpenService(int onlineOfflineStatus, int requestCode)
 {

  HashMap<String, String> params = new HashMap<String, String>();
  params.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);
  params.put(ServiceUrls.ApiRequestParams.ONLINE, String.valueOf(onlineOfflineStatus));
  params.put(ServiceUrls.ApiRequestParams.OS, Constants.ANDROID);

  Common.Log.i("? - MyService.getInstance() : "+ MyService.getInstance());

  sendSocketRequest(this, ServiceUrls.RequestNames.ON_SOCKET_OPEN, params, false, requestCode);

 }



 @Override
 public void onSocketMessageReceived(ApiResponseVo apiResponseVo, int requestId)
 {

  switch (apiResponseVo.requestname)
  {

   case ServiceUrls.RequestNames.SOCKET_INITIATION:

    SocketInitiationVo socketInitiationVo = Common.getSpecificDataObject(apiResponseVo.data, SocketInitiationVo.class);

    Common.Log.i("?????-socketInitiationVo.success : " + socketInitiationVo.success);

    if (socketInitiationVo.onlineStatus == Constants.DriverStatuses.SOCKET_DISCONNECTED)
    {
       sendSocketRequestToOnSocketOpenService(1,-1);
    }


    /**/

    /* Retrieve a PendingIntent that will perform a broadcast */
    Intent alarmIntent = new Intent(this, AlarmReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);


    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    long interval = 60*1000;

//    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);


    Common.Log.i("?????-Alarm Set");

    /**/






    break;

   case ServiceUrls.RequestNames.ON_SOCKET_OPEN:

    OnSocketOpenVo OnSocketOpenVo = Common.getSpecificDataObject(apiResponseVo.data, OnSocketOpenVo.class);

    Common.Log.i("OnSocketOpenVo : " + OnSocketOpenVo.toString());

    break;

   case ServiceUrls.RequestNames.ON_CONNECT:

    Common.Log.i("??? - MyService - onSocketMessageReceived - requestId : "+requestId);

    sendSocketInitiationRequest();


    break;

   case ServiceUrls.RequestNames.ORDER_RECEIVED:

    OrderReceivedVo orderReceivedVo = Common.getSpecificDataObject(apiResponseVo.data, OrderReceivedVo.class);

    Intent acceptRejectIntent = new Intent(this, AcceptRejectRideActivity.class);

    acceptRejectIntent.putExtra(Constants.KEY_1, orderReceivedVo);

    acceptRejectIntent.putExtra(Constants.IntentKeys.IS_FROM_SERVICE, true);

    acceptRejectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    startActivity(acceptRejectIntent);


    break;



   case ServiceUrls.RequestNames.UPDATE_SOCKET_CONNECTIVITY:

//                OnSocketOpenVo OnSocketOpenVo = Common.getSpecificDataObject(apiResponseVo.data, OnSocketOpenVo.class);

    Common.Log.i("????? - MyService Response : " + ServiceUrls.RequestNames.UPDATE_SOCKET_CONNECTIVITY);

    break;



  }


 }


}
