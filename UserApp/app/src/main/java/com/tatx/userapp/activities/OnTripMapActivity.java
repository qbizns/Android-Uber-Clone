package com.tatx.userapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.Bounds;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.abstractclasses.NetworkChangeListenerActivity;
import com.tatx.userapp.adapter.CustomArrayAdapterReasons;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.customviews.ChangeDestinationAlertDialog;
import com.tatx.userapp.customviews.CustomerAlertDialog;
import com.tatx.userapp.customviews.RequestingCustomerAlertDialog;
import com.tatx.userapp.enums.PaymentType;
import com.tatx.userapp.gcm.GCMNotificationIntentService;
import com.tatx.userapp.googlemapadapters.Util;
import com.tatx.userapp.helpers.GeoDetails;
import com.tatx.userapp.interfaces.DialogClickListener;
import com.tatx.userapp.interfaces.GoogleMapOnTouchListener;
import com.tatx.userapp.library.AbstractRouting;
import com.tatx.userapp.library.Route;
import com.tatx.userapp.library.RouteException;
import com.tatx.userapp.library.Routing;
import com.tatx.userapp.library.RoutingListener;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.network.SocketResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.BroadcastLocationVo;
import com.tatx.userapp.pojos.ChangeDestinationVo;
import com.tatx.userapp.pojos.Driver;
import com.tatx.userapp.pojos.FavLocation;
import com.tatx.userapp.pojos.GetReasonsForCancelVo;
import com.tatx.userapp.pojos.GetSavedLocationVo;
import com.tatx.userapp.pojos.OnTrip;
import com.tatx.userapp.pojos.OrderCanceledByDriverVo;
import com.tatx.userapp.pojos.Reason;
import com.tatx.userapp.pojos.RecentLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OnTripMapActivity extends NetworkChangeListenerActivity implements View.OnClickListener, com.google.android.gms.location.LocationListener, RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, SocketResponseListener, RetrofitResponseListener, GoogleMapOnTouchListener
{


    private static final String LOG = OnTripMapActivity.class.getSimpleName();

    private GoogleMap googleMap = null;
    protected LatLng startLntLng;
    protected LatLng end;
    //    ImageView pickerimg;
    private static final String LOG_LOG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
    private ArrayList<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark, R.color.primary, R.color.primary_light, R.color.accent, R.color.primary_dark_material_light};
    private static final LatLngBounds BOUNDS_JAMAICA = new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531),
            new LatLng(72.77492067739843, -9.998857788741589));
    LocationRequest mLocationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    double latitude = 17.4483000;
    double longitude = 78.3915000;
    private Marker currentLocationMarker;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    ImageView view;
    private Location lastknownLocation = null;
    private boolean isMarkerRotating = false;
    private String address = "";
    private int action;
    private String mobile;
    private String drivername;
    private int tripid;
    private int orderid;
    private String sourceAdd;
    private String destinationAdd;
    private MyBroadcastReceiver_Update myBroadcastReceiver_Update;
    private Handler mHandler;
    private static OnTripMapActivity inst;
    //    private Button farestimte;
    private double source_lat = 0;
    private double source_long = 0;
    private double destination_lat = 0;
    private double destination_long = 0;

    @BindView(R.id.tv_make_and_model)
    TextView tvMakeAndModel;
    @BindView(R.id.tv_vehicle_number)
    TextView tvVehicleNumber;

    @BindView(R.id.tv_source_address_to_show)
    TextView tvSourceAddressToShow;
    @BindView(R.id.tv_destination_address_to_show)
    EditText tvDestinationAddressToShow;
    @BindView(R.id.rl_edit_destination)
    RelativeLayout rlEditDestination;
    @BindView(R.id.iv_edit_location)
    ImageView ivEditLocation;

    @BindView(R.id.rate)
    TextView rating;

    @BindView(R.id.driver_profile_pic)
    ImageView ivDriverProfilePic;
    @BindView(R.id.car_pic)
    ImageView ivCarPic;

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;

//    @BindView(R.id.startloc)
//    TextView startloc;


    @BindView(R.id.my_current_location)
    ImageButton myCurrentLocation;
    @BindView(R.id.save_button)
    Button saveButton;
    @BindView(R.id.iv_picker_icon)
    ImageView pickerimg;
    @BindView(R.id.source_dest_layout)
    RelativeLayout source_dest_layout;
    @BindView(R.id.ll_drag_view_main)
    LinearLayout dragView;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayoutSearch;
    @BindView(R.id.order_layout)
    RelativeLayout order_layout;
    @BindView(R.id.tv_driver_name)
    TextView tvDriverName;
    @BindView(R.id.contact)
    Button contact;
    @BindView(R.id.cancel_order)
    Button cancelOrder;
    @BindView(R.id.farestimte)
    Button farestimte;
    @BindView(R.id.slide_up_to_view)
    TextView slideUpToViewText;
    @BindView(R.id.linearl)
    LinearLayout linearl;
    @BindView(R.id.btn_cash)
    Button paymentTypeIcon;
    @BindView(R.id.rv_google_search)
    RecyclerView mRecyclerView;
    @BindView(R.id.recyclerview_saved_locatons)  RecyclerView recyclerviewSavedLocatons;
    @BindView(R.id.ll_suggtion_view)  LinearLayout llSuggtionView;

    private String model;
    private String make;
    private String vehicleNo;
    private Marker markerCustomerPickUp = null;
    private int count = 1;
    private String rate;
    private long activityStartTime;
    private CountDownTimer countDownTimer;
    private CustomerAlertDialog customerAlertDialog;
    private String titleTxt;
    private double cancellationCharges;
    private int timeExceeded;
    private Marker destinationMarker;
    private String profile;
    private String carImage;
    private String carColor;
    private String makeModeColorText;
    private String paymentType;
    private boolean onConnecedCall = false;
    private View[] editableViewsDisable;
    private boolean isClickedEditDestination=false;
    private GetSavedLocationVo getSavedLocationVo;
    private ArrayList<FavLocation> favLocationArrayList=new ArrayList<>();
    private LatLng changedLatLng;
    private RequestingCustomerAlertDialog requestingCustomerAlertDialog;
    private ChangeDestinationAlertDialog changeDestinationAlertDialog;
    private CountDownTimer countDownTimerForChangeDestination;
    private CountDownTimer countDownTimerForCancelOrder;


    public static OnTripMapActivity instance() {
        return inst;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ontrip_map);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.addlocation));

        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());

        Common.Log.i("Inside OnTripMapActivity onCreate().");


        activityStartTime = Common.getDefaultSP(this).getLong(Constants.ORDER_CANCEL_TIME,0);

        if (!Util.isGooglePlayServicesAvailable(this)) {
            finish();
        }

        Log.d(LOG, String.valueOf(Util.isGooglePlayServicesAvailable(this)));

        createLocationRequest();


        polylines = new ArrayList<>();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        displayGoogleLocationSettingPage(this);

        initiLazedAll();

        sildingUpPanel();

        myBroadcastReceiver_Update = new MyBroadcastReceiver_Update();
        IntentFilter intentFilter_update = new IntentFilter(GCMNotificationIntentService.ACTION_MyUpdate);
        intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver_Update, intentFilter_update);
    }


    @Override
    protected void onStart() {
        super.onStart();
        inst = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver_Update);

    }



    /*************************************
     * SlidingUpPanel
     ************************************/
    public void sildingUpPanel() {
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(OnTripMapActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An"
        );

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter);


        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(LOG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(LOG, "onPanelStateChanged " + newState);

                switch (newState.toString()) {
                    case Constants.PanelState.COLLAPSED:
                        Log.i(LOG, "onPanelStateChanged " + "COLLAPSED..");
                        setSlideUpToViewText(ContextCompat.getColor(OnTripMapActivity.this, R.color.customer_text_color), ContextCompat.getColor(OnTripMapActivity.this, R.color.white), View.VISIBLE);
                        break;
                    case Constants.PanelState.EXPANDED:

                        Log.i(LOG, "onPanelStateChanged " + "EXPANDED..");
                        setSlideUpToViewText(ContextCompat.getColor(OnTripMapActivity.this, R.color.white), ContextCompat.getColor(OnTripMapActivity.this, R.color.button_color), View.GONE);
                        break;
                }

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


    }


@OnClick(R.id.rl_edit_destination) void editDestination(){
    Log.d("clicked et","clicked et..1");
    if (isClickedEditDestination){
        relativeLayoutSearch.setVisibility(View.GONE);
        isClickedEditDestination=false;
        ivEditLocation.setBackgroundResource(R.drawable.edit_icon);
        llSuggtionView.setVisibility(View.GONE);
        Common.setViewsEnableStatus(isClickedEditDestination,editableViewsDisable);
        if (changedLatLng!=null){


            Common.Log.i("sourceLatLngSelected 2: " + lastknownLocation);
            Bounds sourceBounds = Common.getCityBoundaries(OnTripMapActivity.this, new LatLng(lastknownLocation.getLatitude(), lastknownLocation.getLongitude()));

            Common.Log.i("destinationLatLngSelected : " + changedLatLng);
            Bounds destinationBounds = Common.getCityBoundaries(OnTripMapActivity.this, changedLatLng);

            Common.Log.i("? - sourceBounds : " + sourceBounds);
            Common.Log.i("? - destinationBounds : " + destinationBounds);

            Common.Log.i("sourceBounds.northeast : " + sourceBounds.northeast);
            if (destinationBounds == null) {
                Common.customToast(this, Common.getStringResourceText(R.string.invalid_destination));
                return;
            }
            Location sourceNorthEastLocation = new Location("");
            sourceNorthEastLocation.setLatitude(sourceBounds.northeast.lat);
            sourceNorthEastLocation.setLongitude(sourceBounds.northeast.lng);

            Location sourceSouthWestLocation = new Location("");
            sourceSouthWestLocation.setLatitude(sourceBounds.southwest.lat);
            sourceSouthWestLocation.setLongitude(sourceBounds.southwest.lng);

            Location destinationNorthEastLocation = new Location("");
            destinationNorthEastLocation.setLatitude(destinationBounds.northeast.lat);
            destinationNorthEastLocation.setLongitude(destinationBounds.northeast.lng);


            Location destinationSouthWestLocation = new Location("");
            destinationSouthWestLocation.setLatitude(destinationBounds.southwest.lat);
            destinationSouthWestLocation.setLongitude(destinationBounds.southwest.lng);


            float northEastDistance = sourceNorthEastLocation.distanceTo(destinationNorthEastLocation);
            Common.Log.i("northEarstDistance : " + northEastDistance);

            float southWestDistance = sourceSouthWestLocation.distanceTo(destinationSouthWestLocation);
            Common.Log.i("southWestDistance : " + southWestDistance);

            LatLngBounds sourceLatLngBounds = new LatLngBounds(new LatLng(sourceSouthWestLocation.getLatitude(), sourceSouthWestLocation.getLongitude()), new LatLng(sourceNorthEastLocation.getLatitude(), sourceNorthEastLocation.getLongitude()));

            boolean inSideBoundary = sourceLatLngBounds.contains(changedLatLng);

            Common.Log.i("sourceLatLngBounds.contains(destinationLatLngSelected) : " + sourceLatLngBounds.contains(changedLatLng));
            Common.Log.i("sourceNorthEastLocation.toString() : " + sourceNorthEastLocation.toString());
            Common.Log.i("sourceSouthWestLocation.toString() : " + sourceSouthWestLocation.toString());
            Common.Log.i("destinationNorthEastLocation.toString() : " + destinationNorthEastLocation.toString());
            Common.Log.i("destinationSouthWestLocation.toString() :  " + destinationSouthWestLocation.toString());


            if (southWestDistance <= 20000 || northEastDistance <= 20000 || inSideBoundary) {



                HashMap<String, String> params = new HashMap();
                params.put(ServiceUrls.RequestParams.TRIPID, String.valueOf(tripid));
                params.put(ServiceUrls.RequestParams.DROP_LATITUDE, String.valueOf(changedLatLng.latitude));
                params.put(ServiceUrls.RequestParams.DROP_LONGITUDE, String.valueOf(changedLatLng.longitude));
                params.put(ServiceUrls.RequestParams.DROP_LOCATION,tvDestinationAddressToShow.getText().toString() );

                // new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CHANGE_DESTINATION,params,true);
                TATX.getInstance().sendSocketRequest(OnTripMapActivity.this, ServiceUrls.RequestNames.CHANGE_DESTINATION, params,false);

                requestingCustomerAlertDialog=new RequestingCustomerAlertDialog(this,"Requesting");
                requestingCustomerAlertDialog.setCancelable(false);
                requestingCustomerAlertDialog.show();
                startTimerToChangeDestination();


            }else {
                relativeLayoutSearch.setVisibility(View.VISIBLE);
                isClickedEditDestination=true;
                ivEditLocation.setBackgroundResource(R.drawable.checked_red);
                Common.setViewsEnableStatus(isClickedEditDestination,editableViewsDisable);
                Common.customToast(this, Common.getStringResourceText(R.string.the_destination_entered_is_outside_our_coverage_area), Common.TOAST_TIME);
            }


        }
        // Common.setSuggetions(this,mGoogleApiClient,recyclerviewSavedLocatons,mRecyclerView,favLocationArrayList,tvDestinationAddressToShow,true);
    }else{
        relativeLayoutSearch.setVisibility(View.VISIBLE);
        isClickedEditDestination=true;
        ivEditLocation.setBackgroundResource(R.drawable.checked_red);
        Common.setViewsEnableStatus(isClickedEditDestination,editableViewsDisable);
    }


    startTimer();

}


    @OnClick (R.id.tv_destination_address_to_show) void editTextDestinationClicked(){

        Log.d("clicked et","clicked et..2");
        llSuggtionView.setVisibility(View.VISIBLE);
        Common.setSuggetions(this,mGoogleApiClient,recyclerviewSavedLocatons,mRecyclerView,favLocationArrayList,tvDestinationAddressToShow);

    }


    @Override
    public void onBackPressed() {
        if (llSuggtionView.getVisibility()==View.VISIBLE){
            llSuggtionView.setVisibility(View.INVISIBLE);
            return;
        }

        super.onBackPressed();
    }



    public void mRecyclerViewClickListner(LatLng latLng) {
        if (latLng!=null && googleMap!=null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            llSuggtionView.setVisibility(View.GONE);

            changedLatLng=latLng;


        }
    }


    @Override
    public void onGoogleMapTouch(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d("countDownTimer","ACTION_DOWN...1");
                llSuggtionView.setVisibility(View.GONE);
                if (countDownTimer!=null) {
                    countDownTimer.cancel();
                }
                break;

            case MotionEvent.ACTION_UP:
                Log.d("countDownTimer","ACTION_UP...1");
                if (countDownTimer!=null) {
                    countDownTimer.start();
                }
                break;

        }

    }


    public void initiLazedAll() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        editor = sp.edit();

        editableViewsDisable = new View[]{tvDestinationAddressToShow};

        HashMap<String, String> params = new HashMap<String, String>();
        TATX.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.SPECIFIC_DRIVER, params);

        llSuggtionView.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();

        Common.Log.i("bundle.toString() : " + bundle.toString());

        action = getIntent().getIntExtra("action", 0);

        Common.Log.i("action : " + action);
        if (action == 2 || action == 4) {

            OnTrip onTrip = (OnTrip) getIntent().getSerializableExtra(Constants.KEY_1);

            Common.Log.i("onTripVo.toString() : " + onTrip.toString());

            mobile = onTrip.mobile;
            sourceAdd = getIntent().getStringExtra(Constants.IntentKeys.SOURCE_ADDRESS);
            destinationAdd = getIntent().getStringExtra(Constants.IntentKeys.DESTINATION_ADDRESS);
            drivername = onTrip.drivername;
            tripid = onTrip.tripid;
            cancellationCharges = onTrip.cancellationCharges;
            orderid = onTrip.orderid;
            source_lat = onTrip.pickLatitude;
            source_long = onTrip.pickLongitude;
            destination_lat = onTrip.dropLatitude;
            destination_long = onTrip.dropLongitude;
            make = onTrip.make;
            model = onTrip.model;
            vehicleNo = onTrip.vehicleNo;
            rate = onTrip.rating;
            profile = onTrip.profile;
            carImage = onTrip.carImage;
            paymentType = onTrip.paymentType;


            if (action == 4) {

                rlEditDestination.setVisibility(View.VISIBLE);

                if (source_lat != 0 && destination_lat != 0) {

                    if (Common.haveInternet(OnTripMapActivity.this)) {
                        route(new LatLng(source_lat, source_long), new LatLng(destination_lat, destination_long));


                    } else {
                        Common.customToast(getApplicationContext(), Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
                    }

                }
            }

            tvSourceAddressToShow.setText(sourceAdd);
            tvDestinationAddressToShow.setText(destinationAdd);
            tvDriverName.setText(drivername);
            if (carColor != null) {
                makeModeColorText = make + "\n" + model + "\nCar Color: " + carColor;

            } else {
                makeModeColorText = make + "\n" + model;
            }
            tvMakeAndModel.setText(makeModeColorText);
            tvVehicleNumber.setText(vehicleNo);
            rating.setText(Common.getTwoDecimalRoundValueString(Double.parseDouble(rate)));
            if (profile != null) {
                Common.setCircleImageBackgroundFromUrl(this, ivDriverProfilePic, profile);
            }
            if (carImage != null) {
                Common.setCircleImageBackgroundFromUrl(this, ivCarPic, carImage);
                // Picasso.with(this).load(carImage).into(ivCarPic);
            }

        }
        if (paymentType != null) {
            int stringResourceId = PaymentType.getEnumFieldById(Integer.parseInt(paymentType)).getStringId();
            int drawableResourceId = PaymentType.getEnumFieldById(Integer.parseInt(paymentType)).getBackgroundDrawableId();
            Drawable drawablImg = getResources().getDrawable(drawableResourceId);
            drawablImg.setBounds(0, 0, 90, 0);
            paymentTypeIcon.setCompoundDrawables(drawablImg, null, null, null);
            paymentTypeIcon.setText(Common.getStringResourceText(stringResourceId));
        }

        Log.d("actionMap", ":" + action);

        setUIAction(action);

        contact.setOnClickListener(this);
        cancelOrder.setOnClickListener(this);
        farestimte.setOnClickListener(this);
        myCurrentLocation.setOnClickListener(this);
        saveButton.setOnClickListener(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maploc);


        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.maploc, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);



    }




    public void route(LatLng start, LatLng end) {

        Common.Log.i("start.toString() : " + start.toString());
        Common.Log.i("end.toString() : " + end.toString());

        if (start == null || end == null) {
            if (start == null) {
                Toast.makeText(this, Common.getStringResourceText(R.string.please_choose_starting_point), Toast.LENGTH_SHORT).show();

            }
            if (end == null) {
                Toast.makeText(this, Common.getStringResourceText(R.string.please_choose_destination), Toast.LENGTH_SHORT).show();
            }
        } else {
//            progressDialog = ProgressDialog.show(this, Common.getStringResourceText(R.string.please_wait),
//                    Common.getStringResourceText(R.string.fetching_route_information), true);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .build();
            routing.execute();
        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed

        Common.Log.i("onRoutingFailure");

        Common.Log.i("e.toString()" + e.toString());

        e.printStackTrace();

        progressDialog.dismiss();
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, Common.getStringResourceText(R.string.something_went_wrong_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }


        LatLng sourceLatLng = new LatLng(source_lat, source_long);
        final LatLng destinationLatLng = new LatLng(destination_lat, destination_long);

        Common.Log.i("sourceLatLng.toString() : " + sourceLatLng.toString());
        Common.Log.i("destinationLatLng.toString() :  " + destinationLatLng.toString());
//
//        CameraUpdate center = CameraUpdateFactory.newLatLng(destinationLatLng);

        //      googleMap.moveCamera(center);
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();

        Common.Log.i("route.size() : " + route.size());

        polylines = new ArrayList<>();

        int dist = route.get(0).getDistanceValue();
        int pos = 0;

        for (int i = 1; i < route.size(); i++) {
            if (dist > route.get(i).getDistanceValue()) {
                dist = route.get(i).getDistanceValue();
                pos = i;
            }
        }


        //In case of more than 5 alternative routes
        int colorIndex = pos % COLORS.length;
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(COLORS[colorIndex]));
        polyOptions.width(15 + pos * 2);
        polyOptions.addAll(route.get(pos).getPoints());
        Polyline polyline = googleMap.addPolyline(polyOptions);
        polylines.add(polyline);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int j = 0; j < route.get(pos).getPoints().size(); j++) {
            builder.include(route.get(pos).getPoints().get(j));
            j = j + 5;
        }
        //  builder.include(destinationLatLngSelected);


        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);


        if(destinationMarker != null)
        {
            destinationMarker.remove();
        }

        // End marker
        MarkerOptions options = new MarkerOptions();

        options.position(destinationLatLng);

//        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin));

        options.icon(BitmapDescriptorFactory.fromBitmap(Common.getBitmapFromView(getLayoutInflater().inflate(R.layout.marker_custom_view, null))));

        destinationMarker = googleMap.addMarker(options);


    }

    @Override
    public void onRoutingCancelled() {
        Common.Log.i("Routing was cancelled.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.v(LOG_LOG, connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {

        onConnecedCall = true;
        startLocationUpdates();
        progressDialog = ProgressDialog.show(this, "", Common.getStringResourceText(R.string.please_wait), false);
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_SAVED_LOCATIONS,null);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.getUiSettings().setRotateGesturesEnabled(false);

        if (googleMap != null) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            if (String.valueOf(source_lat).length() > 3) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(source_lat, source_long));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);

                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
            }


            Log.d("methoscalling", "811 Map reddy");

        }

    }

    public void myCurrentLocationCamera(Location location) {
        if (location != null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            address = Common.getCompleteAddressString(OnTripMapActivity.this, location.getLatitude(), location.getLongitude()).getCompleteAddress();
         //   startloc.setText(address);
        }
    }

    public void displayGoogleLocationSettingPage(final Activity activity) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();

                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        //Toast.makeText(activity, "RESOLUTION_REQUIRED", Toast.LENGTH_SHORT).show();
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });


    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);

        Log.d(LOG, "OnresultAxtivity");

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to

                        break;
                    default:
                        break;
                }
                break;
            case Constants.GOOGLE_SUGGETIONS_SOURCE:

                Log.d(LOG, "OnresultAxtivity");

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        String address = data.getStringExtra("address");
                        Bundle bundle = data.getParcelableExtra("bundle");
                        LatLng latLng = bundle.getParcelable("latlong");
                       // startloc.setText(address);
                        zoomCamera(latLng.latitude, latLng.longitude);
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to

                        break;
                    default:
                        break;
                }
                break;


        }
    }





    public void zoomCamera(Double lat, Double lng) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng)).zoom(Constants.ZOOM_LEVEL).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.my_current_location:
                if (lastknownLocation != null) {
                    myCurrentLocationCamera(lastknownLocation);
                }
                break;
            case R.id.save_button:
                saveButton.setEnabled(false);
                if (action == 3) {
                    Bundle bundle = new Bundle();
                    Intent intent1 = new Intent();
                    intent1.putExtra("address", address);
                    bundle.putParcelable("latlong", new LatLng(latitude, longitude));
                    intent1.putExtra("bundle", bundle);
                    setResult(Activity.RESULT_OK, intent1);
                    finish();//finishing activity

                }
                saveButton.setEnabled(true);
                break;
            case R.id.contact:

                Intent intent = new Intent(this, CallMessageActivity.class);
                intent.putExtra(Constants.MOBILE, mobile);
                startActivity(intent);

                break;

            case R.id.cancel_order:


                HashMap<String, String> hashMap = new HashMap<String, String>();

                hashMap.put(ServiceUrls.RequestParams.ROLE_ID, Constants.CUSTOMER_ROLE_ID);

                new RetrofitRequester(OnTripMapActivity.this).sendStringRequest(ServiceUrls.RequestNames.GET_REASONS_FOR_CANCEL, hashMap);




                break;
            case R.id.farestimte:
                farestimte.setEnabled(false);
                Intent intent3 = new Intent(this, FareEstimateActivity.class);
                intent3.putExtra(Constants.IntentKeys.SOURCE_ADDRESS, tvSourceAddressToShow.getText().toString());
                intent3.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS, tvDestinationAddressToShow.getText().toString());
                startActivity(intent3);

                break;

        }
    }




    @Override
    public void onStop() {
        super.onStop();


    }




    public void setUIAction(int action) {
        if (action == 1 || action == 3) {
            //pickerimg.setVisibility(View.VISIBLE);
            relativeLayoutSearch.setVisibility(View.VISIBLE);
            order_layout.setVisibility(View.VISIBLE);
            source_dest_layout.setVisibility(View.GONE);
            dragView.setVisibility(View.GONE);
            if (action == 3) {
                setTitleText(Common.getStringResourceText(R.string.select_location));
                saveButton.setText(Common.getStringResourceText(R.string.ok));
            } else {
                setTitleText(Common.getStringResourceText(R.string.add_location));
            }

        } else if (action == 2) {
           // pickerimg.setVisibility(View.GONE);
            relativeLayoutSearch.setVisibility(View.GONE);
            order_layout.setVisibility(View.GONE);
            source_dest_layout.setVisibility(View.VISIBLE);
            dragView.setVisibility(View.VISIBLE);
            setTitleText(Common.getStringResourceText(R.string.en_route));

            // mLayout.setPanelHeight((int) getResources().getDimension(R.dimen._88sdp));


        } else if (action == 4) {
           // pickerimg.setVisibility(View.GONE);
            relativeLayoutSearch.setVisibility(View.GONE);
            order_layout.setVisibility(View.GONE);
            source_dest_layout.setVisibility(View.VISIBLE);
            dragView.setVisibility(View.VISIBLE);
            setTitleText(Common.getStringResourceText(R.string.on_trip));

            cancelOrder.setVisibility(View.GONE);
/*

            mLayout.setPanelHeight((int) getResources().getDimension(R.dimen._118sdp));


*/


            //  mLayout.setPanelHeight((int) getResources().getDimension(R.dimen._88sdp));


        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            Log.d("location", "onLocationChanged..." + location.getLatitude() + "   " + location.getLongitude());
            lastknownLocation = location;
            Log.d("methoscalling", "455 onLocationChanged");

            if (onConnecedCall) {
                onConnecedCall = false;
                myCurrentLocationCamera(lastknownLocation);
                if (Double.toString(source_long).length() > 3 && Double.toString(lastknownLocation.getLatitude()).length() > 3) {
                    if (Common.haveInternet(OnTripMapActivity.this)) {
//                        route(new LatLng(lastknownLocation.getLatitude(), lastknownLocation.getLongitude()),new LatLng(source_lat, source_long));
                        // route(new LatLng(lastknownLocation.getLatitude(), lastknownLocation.getLongitude()),new LatLng(destination_lat, destination_long));





                        MarkerOptions options = new MarkerOptions();
                        options.position(new LatLng(source_lat, source_long));
                        options.icon(BitmapDescriptorFactory.fromBitmap(Common.getBitmapFromView(getLayoutInflater().inflate(R.layout.marker_custom_view_black, null))));
                        markerCustomerPickUp = googleMap.addMarker(options);


                        // End marker
                        options = new MarkerOptions();
                        options.position(new LatLng(lastknownLocation.getLatitude(), lastknownLocation.getLongitude()));
                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_3));
                        currentLocationMarker = googleMap.addMarker(options);


                    } else {
                        Common.customToast(getApplicationContext(), Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
                    }
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }


/*if (action==4) {
    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
    CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
    googleMap.moveCamera(center);
    googleMap.animateCamera(zoom);
}*/
        }


    }




    public class MyBroadcastReceiver_Update extends BroadcastReceiver {
        int code = 0;

        @Override
        public void onReceive(Context context, Intent intent) {

            Common.Log.i("Inside OnTripMapActivity BroadCastReceiver.");

            Common.Log.i("intent.getStringExtra(\"from\") : " + intent.getStringExtra("from"));

            Bundle bundle = intent.getExtras();
            String from = bundle.getString("from");
            if (from.equalsIgnoreCase("gcm")) {
                Log.d("MyBroadcastReceiver", "MyBroadcastReceiver_Update from AddMap activity");
                if (bundle.containsKey("code")) {
                    code = bundle.getInt("code");
                }
                if (code == 20001) {
//                    titleTV.setText("En-Route");

                    setTitleText(Common.getStringResourceText(R.string.on_trip));

                    cancelOrder.setVisibility(View.GONE);
                    rlEditDestination.setVisibility(View.VISIBLE);
/*

                    mLayout.setPanelHeight((int) getResources().getDimension(R.dimen._118sdp));

*/

                    //          mLayout.setPanelHeight((int) getResources().getDimension(R.dimen._88sdp));


                    action = 4;
                    if (markerCustomerPickUp != null) {
                        markerCustomerPickUp.remove();
                        markerCustomerPickUp=null;
                    }
                    Log.d("MyBroadcastReceiver", "MyBroadcastReceiver_Update from GoogleMap activity 20001"+markerCustomerPickUp);
                    if (lastknownLocation != null && destination_lat != 0) {

                        if (Common.haveInternet(OnTripMapActivity.this)) {
                            route(new LatLng(lastknownLocation.getLatitude(), lastknownLocation.getLongitude()), new LatLng(destination_lat, destination_long));


                        } else {
                            Common.customToast(getApplicationContext(), Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
                        }

                    }
                } else {

                }
            }
        }
    }


    @Override
    public void onSocketMessageReceived(final ApiResponseVo apiResponseVo, int requestId) {
        Common.Log.i("Inside onSocketMessageReceived()");

        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());

        if (apiResponseVo.code != Constants.SUCCESS) {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        Common.Log.i("apiResponseVo.requestname : " + apiResponseVo.requestname);

        Common.Log.i("apiResponseVo.data : " + apiResponseVo.data);

        Common.Log.i("Switch Case Started");

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.ON_SOCKET_OPEN:
                Common.Log.i("apiResponseVo.toString() : " + apiResponseVo.toString());
                break;


            case ServiceUrls.RequestNames.SPECIFIC_BROADCAST:

                if (isClickedEditDestination){
                    return;
                }

                BroadcastLocationVo broadcastLocationVoSB = Common.getSpecificDataObject(apiResponseVo.data, BroadcastLocationVo.class);

                //JSONObject jsonObjectData = new JSONObject(new Gson().toJson(broadcastLocationVo));
                List<Driver> driversList1 = broadcastLocationVoSB.drivers;

                Common.Log.i("NEW apiResponseVo.toString() : " + driversList1.toString());

                if (currentLocationMarker != null && driversList1.size() > 0) {
                    Common.Log.i("NEW apiResponseVo.toString() : " + " ! null");

                    double cabCurrentLat = driversList1.get(0).latitude;
                    double cabCurrentLng = driversList1.get(0).longitude;

                    moveMarker(currentLocationMarker, currentLocationMarker.getPosition(), new LatLng(cabCurrentLat, cabCurrentLng));

                    Location cabCurrentLocation = new Location("");
                    cabCurrentLocation.setLatitude(cabCurrentLat);
                    cabCurrentLocation.setLongitude(cabCurrentLng);

                    updateCameraBearing(googleMap, cabCurrentLocation.getBearing());

                     if(action == 2 || action == 4){
//                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(cabCurrentLocation.getLatitude(), cabCurrentLocation.getLongitude()));
//                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
//                        googleMap.moveCamera(center);
//                        googleMap.animateCamera(zoom);
                    }


                }

                String destinationReachTime = broadcastLocationVoSB.destinationReachTime;


                View inflatedView = getLayoutInflater().inflate(R.layout.marker_custom_view_black, null);

                TextView inflatedViewTvDestinationReachTime = (TextView) inflatedView.findViewById(R.id.tv_destination_reach_time);

                if (destinationReachTime != null) {


//                    inflatedViewTvDestinationReachTime.setText(destinationReachTime+"\nMin");
                    inflatedViewTvDestinationReachTime.setText((int) Double.parseDouble(destinationReachTime) + "\nMin");


                } else {

                    inflatedViewTvDestinationReachTime.setText(null);

                }

                if (destinationMarker != null) {
                    destinationMarker.setIcon(BitmapDescriptorFactory.fromBitmap(Common.getBitmapFromView(inflatedView)));
                }else if (markerCustomerPickUp != null && destinationMarker == null){
                    markerCustomerPickUp.setIcon(BitmapDescriptorFactory.fromBitmap(Common.getBitmapFromView(inflatedView)));
                }


                break;

            case ServiceUrls.RequestNames.CANCEL_TRIP:

                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                Toast.makeText(this, Common.getStringResourceText(R.string.trip_cancelled), Toast.LENGTH_SHORT).show();

                break;


            case Constants.BROADCAST_LOCATION:

                BroadcastLocationVo broadcastLocationVo = Common.getSpecificDataObject(apiResponseVo.data, BroadcastLocationVo.class);

                Common.Log.i("broadcastLocationVo.drivers.size() : " + broadcastLocationVo.drivers.size());
                break;

            case Constants.ORDER_CANCELED_BY_DRIVER:


                OrderCanceledByDriverVo orderCanceledByDriverVo = Common.getSpecificDataObject(apiResponseVo.data, OrderCanceledByDriverVo.class);

                Common.Log.i(orderCanceledByDriverVo.message);


                showTripCancelledAlertDialog(this, Common.getStringResourceText(R.string.sorry), orderCanceledByDriverVo.message);


                break;

            case ServiceUrls.RequestNames.CHANGE_DESTINATION:

                Common.customToast(this, apiResponseVo.status);

                ChangeDestinationVo changeDestinationVo=Common.getSpecificDataObject(apiResponseVo.data,ChangeDestinationVo.class);

                if (requestingCustomerAlertDialog!=null && requestingCustomerAlertDialog.isShowing())
                {
                    requestingCustomerAlertDialog.dismiss();
                }
                
                 changeDestinationAlertDialog = new ChangeDestinationAlertDialog(this, "", new ChangeDestinationAlertDialog.DialogClickListener()
                 {
                    @Override
                    public void onOkClick()
                    {
                        changeDestinationAlertDialog.dismiss();
                    }

                });





                if (lastknownLocation!=null&&changedLatLng!=null)
                {
//                    googleMap.clear();



                    destination_lat = changedLatLng.latitude;
                    destination_long = changedLatLng.longitude;

                    route(new LatLng(lastknownLocation.getLatitude(),lastknownLocation.getLongitude()),changedLatLng);

//                    destinationMarker.setPosition(changedLatLng);

                    Common.Log.i("? - Inside  if (lastknownLocation!=null&&changedLatLng!=null).");

                }

                changeDestinationAlertDialog.setCancelable(true);

                changeDestinationAlertDialog.show();

                break;


        }


    }

    private void updateCameraBearing(GoogleMap googleMap, float bearing) {
        if (googleMap == null) return;
        CameraPosition camPos = CameraPosition
                .builder(
                        googleMap.getCameraPosition() // current Camera
                )
                .bearing(bearing)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }


    public AlertDialog showTripCancelledAlertDialog(final Context context, String title, String Message) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Message)
                .setCancelable(false)
//                .setPositiveButton("GO ONLINE", null)
                .setPositiveButton(Common.getStringResourceText(R.string.ok), null)
//                .setNegativeButton("GO OFFLINE", null)
                .create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {


                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Common.restartApp(OnTripMapActivity.this);


                    }
                });


                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        sendSocketRequestToOnSocketOpenService(0);

                    }
                });


            }

        });


        alertDialog.show();

        return alertDialog;

    }


    public void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }


    private void animateMarker(GoogleMap googleMap, final Marker marker, final LatLng toPosition,
                               final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 5000;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public void moveMarker(Marker marker, LatLng previousLatLng, LatLng currentLatLng) {

        Location previousLocation = new Location("");
        previousLocation.setLatitude(previousLatLng.latitude);
        previousLocation.setLongitude(previousLatLng.longitude);

        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatLng.latitude);
        currentLocation.setLongitude(currentLatLng.longitude);

        float rotationValue = previousLocation.bearingTo(currentLocation);
        if (previousLocation.distanceTo(currentLocation) > 10) {
            rotateMarker(marker, rotationValue);

            animateMarker(googleMap, marker, currentLatLng, false);
        }


    }

    private void setSlideUpToViewText(int textColor, int backgroundColor, int viewVisibiltiy) {

        tvMakeAndModel.setTextColor(textColor);
        slideUpToViewText.setVisibility(viewVisibiltiy);
        tvDriverName.setTextColor(textColor);
        linearl.setBackgroundColor(backgroundColor);
    }

    private void showChooseReasonForCancelDialog(List<Reason> reasons) {

     /*   Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.reasons_for_cancel);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

*/

        final Dialog dialog = Common.getAppThemeCustomDialog(this, R.layout.reasons_for_cancel, Common.getDimensionResourceValue(R.dimen._35sdp));


        ListView lvReasons = (ListView) dialog.findViewById(R.id.lv_reasons);


        lvReasons.setBackgroundResource(R.drawable.rounded_corner_bg);

        lvReasons.setAdapter(new CustomArrayAdapterReasons(this, R.layout.reasons_item, reasons));

        lvReasons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Reason reason = (Reason) ((CustomArrayAdapterReasons) parent.getAdapter()).getItem(position);

                dialog.cancel();

                long timeDifference = System.currentTimeMillis() - activityStartTime;

                Common.Log.i("? - timeDifference : " + timeDifference);

                long timeDifferenceInMin = timeDifference / (1000 * 60);

                Common.Log.i("? - timeDifferenceInMin : " + timeDifferenceInMin);
                if (timeDifferenceInMin > 2) {
                    titleTxt = cancellationCharges + " " + Common.getStringResourceText(R.string.cancellation_charges_may_apply_do_you_want_to_continue);

                    timeExceeded = 1;

                } else {
                    titleTxt = Common.getStringResourceText(R.string.are_sure_you);

                    startTimerToCancelOrder((2*60*1000)-timeDifference);




                }


                customerAlertDialog = new CustomerAlertDialog(OnTripMapActivity.this, titleTxt, new DialogClickListener() {
                    @Override
                    public void onCancelClick() {
                        customerAlertDialog.dismiss();
                    }

                    @Override
                    public void onOkClick() {

                        customerAlertDialog.dismiss();



                        if (action == 4) {
                            Common.customToast(OnTripMapActivity.this, Common.getStringResourceText(R.string.you_are_on_trip), Common.TOAST_TIME);

                        } else {
                            if (reason.reasonId == "-1") {
                                dialog.cancel();
                                showCustomReasonForCancelDialog(reason);
                                return;
                            }

                            HashMap<String, String> params = new HashMap();
                            params.put(ServiceUrls.RequestParams.TRIPID, String.valueOf(tripid));
                            params.put(ServiceUrls.RequestParams.ORDERID, String.valueOf(orderid));
                            params.put(ServiceUrls.RequestParams.DEVICE, "ANDROID");
                            params.put(ServiceUrls.RequestParams.REQUESTING, "0");
                            params.put(ServiceUrls.RequestParams.TIME_EXCEEDED, String.valueOf(timeExceeded));
                            params.put(ServiceUrls.RequestParams.REASON_ID, reason.reasonId);
                            params.put(ServiceUrls.RequestParams.CUSTOM_REASON, reason.reason);
                            TATX.getInstance().sendSocketRequest(OnTripMapActivity.this, ServiceUrls.RequestNames.CANCEL_TRIP, params);
                        }



                    }
                });

                customerAlertDialog.show();






            }

        });


        dialog.show();


    }


    private void showCustomReasonForCancelDialog(final Reason reason) {

        final Dialog dialog = Common.getAppThemeCustomDialog(this, R.layout.reasons_for_cancel_other_comment, Common.getDimensionResourceValue(R.dimen._35sdp));

        final EditText etReasons = (EditText) dialog.findViewById(R.id.et_reasons);
        Button submit = (Button) dialog.findViewById(R.id.btn_submit);
        //etReasons.setBackgroundResource(R.drawable.rounded_corner_bg);

        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.root_layout);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("methodcalling", "methodcalling");
                Common.hideSoftKeyboardFromDialog(dialog, OnTripMapActivity.this);
                return true;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etReasons.getText().toString().trim().length() < 1) {
                    Common.customToast(OnTripMapActivity.this, Common.getStringResourceText(R.string.enter_your_reason));
                    return;
                }


                HashMap<String, String> params = new HashMap();
                params.put(ServiceUrls.RequestParams.TRIPID, String.valueOf(tripid));
                params.put(ServiceUrls.RequestParams.ORDERID, String.valueOf(orderid));
                params.put(ServiceUrls.RequestParams.DEVICE, "ANDROID");
                params.put(ServiceUrls.RequestParams.REQUESTING, "0");
                params.put(ServiceUrls.RequestParams.TIME_EXCEEDED, String.valueOf(timeExceeded));
                params.put(ServiceUrls.RequestParams.REASON_ID, reason.reasonId);
                params.put(ServiceUrls.RequestParams.CUSTOM_REASON, etReasons.getText().toString().trim());
                TATX.getInstance().sendSocketRequest(OnTripMapActivity.this, ServiceUrls.RequestNames.CANCEL_TRIP, params);


            }
        });


        dialog.show();


    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS) {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.GET_REASONS_FOR_CANCEL:

                GetReasonsForCancelVo getReasonsForCancelVo = Common.getSpecificDataObject(apiResponseVo.data, GetReasonsForCancelVo.class);
                Reason reason = new Reason();
                reason.reason = Common.getStringResourceText(R.string.my_reason_is_not_listed);
                reason.reasonId = "-1";
                getReasonsForCancelVo.reasons.add(reason);
                showChooseReasonForCancelDialog(getReasonsForCancelVo.reasons);

                break;

            case ServiceUrls.RequestNames.GET_SAVED_LOCATIONS:

                getSavedLocationVo = Common.getSpecificDataObject(apiResponseVo.data, GetSavedLocationVo.class);

                Common.Log.i("updateDeviceTokenVo.message : " + getSavedLocationVo.toString());

                /*FavLocation favLocation1=new FavLocation();
                favLocation1.name=Common.getStringResourceText(R.string.saved_locations);

                favLocationArrayList.add(0,favLocation1);*/

                favLocationArrayList.addAll(getSavedLocationVo.favLocations);


                for (RecentLocation recent:getSavedLocationVo.recentLocations){
                    FavLocation favLocation=new FavLocation();
                    favLocation.latitude=recent.pickLatitude;
                    favLocation.longitude=recent.pickLongitude;
                    favLocation.name=recent.pickupLocation;

                    favLocationArrayList.add(favLocation);
                }



                break;


           /* case ServiceUrls.RequestNames.CHANGE_DESTINATION:
                if (requestingCustomerAlertDialog != null && requestingCustomerAlertDialog.isShowing()) {
                    requestingCustomerAlertDialog.dismiss();
                }

                break;*/
        }
    }

    protected void startLocationUpdates() {
        PendingResult<Status> fgfdg = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }


    private void startTimer() {
        Log.d("countDownTimer","countDownTimer...0");
        countDownTimer = new CountDownTimer(1000, 500)
        {

            public void onTick(long millisUntilFinished)
            {
                // tvTimeSec.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                Log.d("countDownTimer","countDownTimer...1");
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (isClickedEditDestination && llSuggtionView.getVisibility()==View.GONE ) {
                            callCameraChangeListner(googleMap.getCameraPosition());
                            Log.d("countDownTimer","countDownTimer...3");
                        }


                    }
                });

            }

        };
    }

    public void callCameraChangeListner(final CameraPosition position) {
        new Handler().post(new Runnable() {
            public GeoDetails geoDetails;

            @Override
            public void run() {
                Log.d(LOG, "onCameraChange..socket...");

                changedLatLng = position.target;
                       // longitude = position.target.longitude;

                Log.d("countDownTimer","countDownTimer...2");
                    geoDetails = Common.getCompleteAddressString(OnTripMapActivity.this, changedLatLng.latitude, changedLatLng.longitude);
                    String addressSoruce = geoDetails.getCompleteAddress();
                    tvDestinationAddressToShow.setText(addressSoruce);

                Log.d("countDownTimer","countDownTimer...5"+ changedLatLng.toString()+"add: "+addressSoruce);



            }
        });

    }



    private void startTimerToChangeDestination() {
        countDownTimerForChangeDestination = new CountDownTimer(15000, 1000) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {

            }
            @Override
            public void onFinish() {

                if (requestingCustomerAlertDialog != null) {
                    requestingCustomerAlertDialog.dismiss();
                }
                countDownTimerForChangeDestination.cancel();



            }
        }.start();

    }



    private void startTimerToCancelOrder(long time) {
        countDownTimerForCancelOrder = new CountDownTimer(time, 500) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {

            }
            @Override
            public void onFinish() {

                timeExceeded = 1;

                titleTxt = cancellationCharges + " " + Common.getStringResourceText(R.string.cancellation_charges_may_apply_do_you_want_to_continue);

                customerAlertDialog.setTvTitle(titleTxt);
            }
        }.start();

    }


}

