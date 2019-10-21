package com.tatx.userapp.menuactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.activities.FeedbackActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.Trip;

import java.util.HashMap;

import butterknife.ButterKnife;



public class TripDetailsActivity extends BaseActivity implements View.OnClickListener ,RetrofitResponseListener{
    private GoogleMap map;
    ImageView mapimg;
    RatingBar ratingBar;
    private RelativeLayout backButton;
    Trip data;
    TextView date;
    TextView time;
    TextView sar;
    TextView distance;
    TextView kms;
    TextView duration;
    TextView min;
    TextView basefare;
    TextView bsfar;
    TextView distancefare;
    TextView sarfare;
    TextView durationfare;
    TextView sarduration;
    TextView subtotal;
    TextView subtotalsar;
    TextView tip;
    TextView tipsar;
    TextView rounddwn;
    TextView totalfare;
    TextView totalsarfare;
    TextView paymentmthd;
    TextView rnddnsar;
    TextView cash;
    TextView rs;
    TextView vehiclemake;
    TextView rate;
    TextView paymentmethod;
    Button feedback;
    private ProgressDialog progressDialog;
    private int value=0;
    private String tripId;
    private TextView adjustment;
    private TextView ctvDiscount;
    private TextView tvDistanceKm;
    private TextView tvDurationMin;
    private ImageView driverProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.tripdetails);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.tripdetails));

        initializedAll();

    }

    public void initializedAll() {

        mapimg = (ImageView) findViewById(R.id.mapImages);
        ratingBar = (RatingBar) findViewById(R.id.userrating);
        date = (TextView) findViewById(R.id.date);
        rs = (TextView) findViewById(R.id.rs);
        distance = (TextView) findViewById(R.id.distance);
        paymentmethod = (TextView) findViewById(R.id.cash);
        kms = (TextView) findViewById(R.id.kms);
        vehiclemake = (TextView) findViewById(R.id.vehiclemake);
        duration = (TextView) findViewById(R.id.duration);
        min = (TextView) findViewById(R.id.min);
        rate = (TextView) findViewById(R.id.rate);
        basefare = (TextView) findViewById(R.id.basefare);
        bsfar = (TextView) findViewById(R.id.bsfar);
        distancefare = (TextView) findViewById(R.id.distancefare);
        sarfare = (TextView) findViewById(R.id.sarfare);
        durationfare = (TextView) findViewById(R.id.durationfare);
        sarduration = (TextView) findViewById(R.id.sarduration);
        subtotal = (TextView) findViewById(R.id.subtotal);
        subtotalsar = (TextView) findViewById(R.id.subtotalsar);
        tip = (TextView) findViewById(R.id.tip);
        subtotalsar = (TextView) findViewById(R.id.subtotalsar);
        tipsar = (TextView) findViewById(R.id.tipsar);
        rounddwn = (TextView) findViewById(R.id.rounddwn);
        totalfare = (TextView) findViewById(R.id.totalfare);
        totalsarfare = (TextView) findViewById(R.id.totalsarfare);
        paymentmthd = (TextView) findViewById(R.id.paymentmthd);
        rnddnsar = (TextView) findViewById(R.id.rnddnsar);
        adjustment=(TextView)findViewById(R.id.adjustment);
        ctvDiscount =(TextView)findViewById(R.id.ctv_discount);
        tvDistanceKm =(TextView)findViewById(R.id.distance_km);
        tvDurationMin =(TextView)findViewById(R.id.duration_min);
        driverProfilePic =(ImageView)findViewById(R.id.iv_user_pic);


        feedback = (Button) findViewById(R.id.feedback);
        feedback.setOnClickListener(this);
        Intent intent = getIntent();

        Bundle bundle = intent.getParcelableExtra("bundle");

        if (bundle.containsKey("value")) {
            tripId=bundle.getString("trip_id");
            Log.d("trip_id",tripId);
           getTripHistoryApi(tripId);

        } else {
            data = (Trip)bundle.getSerializable("triphistory");
            tripId=data.tripId;
            Log.d("data",data.toString());
            setData(data);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback:
                Intent i = new Intent(new Intent(this, FeedbackActivity.class));
                i.putExtra(Constants.IntentKeys.TRIP_ID,tripId);
                startActivity(i);
                break;
        }

    }

    public void setData(Trip data) {
        String currency = data.currency;
        Picasso.with(this).load(data.route).into(mapimg);
        if (data.profile != null) {
            Picasso.with(this).load(data.profile).into(driverProfilePic);
        }
        kms.setText(currency+": "+data.distanceCost);
        min.setText(currency+": "+data.durationCost);
        bsfar.setText(currency+": "+data.baseFare);
        sarfare.setText(currency+": "+data.pricePerKm);
        sarduration.setText(currency+": "+data.pricePerMinute);
        subtotalsar.setText(currency+": "+data.subtotal);
        totalsarfare.setText(currency+": "+data.tripAmount);
        tipsar.setText(currency+": "+data.tip);
        rs.setText(currency+": "+data.tripAmount);
        date.setText(data.startTime);
        paymentmethod.setText(data.paymentTypeId);
        rate.setText(Common.getTwoDecimalRoundValueString(Double.parseDouble(data.driverRating)));
        vehiclemake.setText("("+data.make+" "+data.model+")");
        adjustment.setText(currency+": "+data.adjustmentAmount);
        ctvDiscount.setText(currency+": "+data.discount);
        tvDistanceKm.setText(data.distanceValue+" KM");
        tvDurationMin.setText(data.durationValue+" MIN");
        rnddnsar.setText(String.valueOf(Math.round(Double.parseDouble(data.subtotal))));



    }

    public void getTripHistoryApi(String tripId) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(ServiceUrls.RequestParams.TRIP_ID,tripId);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.TRIP_DETAIL, hashMap);

    }

    /*public void commonResponse(Object o) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        String response = ((RestResponse) o).getCode();
        Log.d("params", "response:" + response +o.toString());
        if (response.equalsIgnoreCase("200")) {
            switch (((RestResponse) o).getRequestname()) {
                case Constants.tripDetail:
                    Object data = ((RestResponse) o).getData();
                    try {
                        Gson gson = new Gson();
                        String data1 = gson.toJson(data);
                        JSONObject jsonObject = new JSONObject(data1);
                        JSONArray jsonArray = jsonObject.getJSONArray("orders");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            jsonObject1.getString("order_id");
                            jsonObject1.getString("payment_type_id");
                            jsonObject1.getString("total_cash");
                            jsonObject1.getString("order_status_type_id");
                            JSONArray jsonArrayTrip = jsonObject1.getJSONArray("trip");
                            for (int j=0;j<jsonArrayTrip.length();j++) {
                                JSONObject jsonObject2 = jsonArrayTrip.getJSONObject(j);
                                final Trip trip = new Trip();
                                Log.d("params", "response:" + jsonObject2.getInt("trip_id")+"   "+tripId);
                                if (jsonObject2.getInt("trip_id")==(Integer.parseInt(tripId))) {
                                    Log.d("params", "response: 1 " + jsonObject2.getInt("trip_id")+"   "+tripId);
                                    trip.setTrip_id(jsonObject2.getString("trip_id"));
                                    trip.setCabs_id(jsonObject2.getString("cabs_id"));
                                    trip.setDriver_id(jsonObject2.getString("driver_id"));
                                    trip.setBase_fare(jsonObject2.getString("base_fare"));
                                    Log.d("params", "response: 2 " + jsonObject2.getInt("trip_id")+"   "+tripId);
                                    trip.setPrice_per_minute(jsonObject2.getString("price_per_minute"));
                                    trip.setPrice_per_km(jsonObject2.getString("price_per_km"));
                                    trip.setMin_price(jsonObject2.getString("min_price"));
                                    trip.setPick_latitude(jsonObject2.getString("pick_latitude"));
                                    trip.setPick_longitude(jsonObject2.getString("pick_longitude"));
                                    trip.setDrop_latitude(jsonObject2.getString("drop_latitude"));
                                    trip.setDrop_longitude(jsonObject2.getString("drop_longitude"));
                                    Log.d("params", "response: 22 " + jsonObject2.getInt("trip_id")+"   "+tripId);
                                    trip.setRoute(jsonObject2.getString("route"));
                                    trip.setDistance(jsonObject2.getString("distance_value"));
                                    trip.setDuration(jsonObject2.getString("duration_value"));
                                    trip.setDistance_cost(jsonObject2.getString("distance_cost"));
                                    trip.setDuration_cost(jsonObject2.getString("duration_cost"));
                                    Log.d("params", "response: 222 " + jsonObject2.getInt("trip_id")+"   "+tripId);
                                    trip.setStart_time(jsonObject2.getString("start_time"));
                                    trip.setVehicle_type(jsonObject2.getString("vehicle_type"));
                                    trip.setMake(jsonObject2.getString("make"));
                                    trip.setModel(jsonObject2.getString("model"));
                                    Log.d("params", "response: 3 " + jsonObject2.getInt("trip_id")+"   "+tripId);
                                    trip.setEnd_time(jsonObject2.getString("end_time"));
                                    trip.setDriver_rating(jsonObject2.getString("driver_rating"));
                                    trip.setUser_rating(jsonObject2.getString("user_rating"));
                                    trip.setTrip_amount(jsonObject2.getString("trip_amount"));
                                    trip.setTip(jsonObject2.getString("tip"));
                                    Log.d("params", "response: 4 " + jsonObject2.getInt("trip_id")+"   "+tripId);
                                    trip.setSubtotal(jsonObject2.getString("subtotal"));
                                    trip.setMobile(jsonObject2.getString("mobile"));
                                    trip.setLevel(jsonObject2.getString("level"));
                                    trip.setStatus(jsonObject2.getString("status"));
                                    trip.setPayment_type_id(jsonObject2.getString("payment_type_id"));
                                    trip.setCurrency(jsonObject2.getString("currency"));
                                    if(jsonObject2.has("profile"))
                                    {

                                        trip.setProfile(jsonObject2.getString("profile"));

                                    }
                                    Log.d("params", "response: 5 " + jsonObject2.getInt("trip_id")+"   "+tripId);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setData(trip);
                                            Log.d("params", "response:" + "   "+tripId);
                                        }
                                    });
                                    break;
                                }


                                //historyList.add(trip);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Toast.makeText(this, ((RestResponse)o).getData().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, ((RestResponse) o).getStatus().toString(), Toast.LENGTH_SHORT).show();

                    break;

            }
        } else {

            Common.customToast(this, ((RestResponse) o).getStatus(), Common.TOAST_TIME);
        }
      //  Log.d("historyList",historyList.toString());
    }*/


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS) {

            Common.customToast(this, apiResponseVo.status);

            return;


        }


        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.TRIP_DETAIL:
                Common.customToast(this, apiResponseVo.status);
                final Trip trip = Common.getSpecificDataObject(apiResponseVo.data, Trip.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData(trip);
                        Log.d("params", "response:" + "   "+tripId);
                    }
                });
                break;
        }
    }
}

