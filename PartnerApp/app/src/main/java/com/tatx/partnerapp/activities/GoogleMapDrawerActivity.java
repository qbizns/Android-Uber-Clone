package com.tatx.partnerapp.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.PushNotificationListenerActivity;
import com.tatx.partnerapp.abstractclasses.PushNotificationListenerActivity.OnUnReadNotificationCountChangeListener;
import com.tatx.partnerapp.adapter.CustomArrayAdapterReasons;
import com.tatx.partnerapp.commonutills.CircleTransform;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.customviews.ChangeDestinationAlertDialog;
import com.tatx.partnerapp.customviews.CustomTypefaceSpan;
import com.tatx.partnerapp.customviews.CustomerAlertDialog;
import com.tatx.partnerapp.customviews.TripCanceledAlertDialog;
import com.tatx.partnerapp.database.SqliteDB;
import com.tatx.partnerapp.dataset.LoginData;
import com.tatx.partnerapp.dataset.Type;
import com.tatx.partnerapp.dataset.WayLocationsLog;
import com.tatx.partnerapp.interfaces.DialogClickListener;
import com.tatx.partnerapp.library.AbstractRouting;
import com.tatx.partnerapp.library.Route;
import com.tatx.partnerapp.library.RouteException;
import com.tatx.partnerapp.library.Routing;
import com.tatx.partnerapp.library.RoutingListener;
import com.tatx.partnerapp.menuactivity.AboutActivity;
import com.tatx.partnerapp.menuactivity.BonusActivity;
import com.tatx.partnerapp.menuactivity.EarningsActivity;
import com.tatx.partnerapp.menuactivity.OnlineHoursActivity;
import com.tatx.partnerapp.menuactivity.DisplayProfileActivity;
import com.tatx.partnerapp.menuactivity.DocumentsActivity;
import com.tatx.partnerapp.menuactivity.HelpActivity;
import com.tatx.partnerapp.menuactivity.InviteAFriend;
import com.tatx.partnerapp.menuactivity.Notifications;
import com.tatx.partnerapp.menuactivity.Rating;
import com.tatx.partnerapp.menuactivity.TopPerformersActivity;
import com.tatx.partnerapp.menuactivity.TripHistoryActivity;
import com.tatx.partnerapp.menuactivity.VehiclesActivity;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.network.SocketResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.ChangeDestinationPojo;
import com.tatx.partnerapp.pojos.DriverCancelTripVo;
import com.tatx.partnerapp.pojos.DriverOnTripVo;
import com.tatx.partnerapp.pojos.EndTripVo;
import com.tatx.partnerapp.pojos.GetBonusDetailsVo;
import com.tatx.partnerapp.pojos.GetDriverProfileVo;
import com.tatx.partnerapp.pojos.GetReasonsForCancelVo;
import com.tatx.partnerapp.pojos.GetTopPerformersDetails;
import com.tatx.partnerapp.pojos.OnSocketOpenVo;
import com.tatx.partnerapp.pojos.Ontrip;
import com.tatx.partnerapp.pojos.OrderCanceledVo;
import com.tatx.partnerapp.pojos.OrderReceivedVo;
import com.tatx.partnerapp.pojos.PickupCustomerVo;
import com.tatx.partnerapp.pojos.PushNotificationResponseVo;
import com.tatx.partnerapp.pojos.Reason;
import com.tatx.partnerapp.pojos.SocketInitiationVo;
import com.tatx.partnerapp.pojos.StartTripVo;
import com.tatx.partnerapp.pojos.UserLogoutVo;
import com.tatx.partnerapp.services.MyService;


public class GoogleMapDrawerActivity extends PushNotificationListenerActivity implements View.OnClickListener, LocationListener, NavigationView.OnNavigationItemSelectedListener, RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, SocketResponseListener, RetrofitResponseListener, GoogleMap.OnCameraChangeListener, OnUnReadNotificationCountChangeListener, OnCheckedChangeListener {


    private static final String LOG = GoogleMapDrawerActivity.class.getSimpleName();
    public static final String TAG_ONLINEOFFLINE = "TAG_ONLINEOFFLINE";
    private static final long TWO_MINITUS = 2 * 60 * 1000;
    private GoogleMap googleMap;
    protected LatLng start;
    protected LatLng end;
    private String imagePath;
    private List<LoginData> userProfile = new ArrayList<>();


    LocationRequest mLocationRequest = new LocationRequest().
            setInterval(INTERVAL).
            setFastestInterval(FASTEST_INTERVAL).
            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 2;

//    private static final float MINIMUM_DISPLACEMENT_IN_METERS = 35;

    protected GoogleApiClient mGoogleApiClient;

    private Marker currentLocationMarker;
    private Location mCurrentLocation = null;
    private Dialog progressDialog;
    //    private SocketServer socketServer;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private int userid;
    private String address = "";
//    private TatxDataSource tatxDataSource;

    private TextView driverfname;
    private TextView make;
    private TextView model;
    private TextView type;
    private TextView driverlname;

    private int tripid;
    private int orderid;
    private SqliteDB sqliteDB;
    private double distance;
    private double totalDistance = 0.0;
    private long startTime;
    private long endTime;
    //    private long totalTime;
    private ArrayList<Polyline> polylines;

    private static final int[] COLORS = new int[]{R.color.primary_dark, R.color.primary, R.color.primary_light, R.color.accent, R.color.primary_dark_material_light};

    private int typeid;
    ImageView prifileimg;


    List<Type> profileList = new ArrayList<>();
//    private String types;

    private int lastTripValue = 0;


    private boolean isMarkerRotating;
    private List<WayLocationsLog> list;

    private DriverOnTripVo driverOnTripVo;


    /* @BindView(R.id.btn_online_offline)
     Button btnOnlineOffline;
 */
    @BindView(R.id.switch_on_off)
    Switch swOnlineOffline;

    @BindView(R.id.btn_contact)
    LinearLayout btnContact;

    @BindView(R.id.fl_cancel_order_car_breakdown)
    FrameLayout carBreakdownORCancelOrder;

    @BindView(R.id.tv_driver_current_destination_address)
    TextView tvDriverCurrentDestinationAddress;

    @BindView(R.id.ll_driver_current_destination_address)
    LinearLayout llDriverCurrentDestinationAddress;

    @BindView(R.id.btn_trip_current_status)
    Button btnTripCurrentStatus;

    @BindView(R.id.card_view)
    CardView contactsLayout;

    @BindView(R.id.btn_navigate)
    LinearLayout btnNavigate;

    @BindView(R.id.navigation_layout)
    RelativeLayout navigationLayout;

    @BindView(R.id.make_last_trip)
    CheckBox makeLastTrip;

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;

    @BindView(R.id.navigation)
    ImageView navigationButton;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.tv_rating)
    TextView tvRating;

    @BindView(R.id.iv_profile_pic)
    ImageView ivProfilePic;

    @BindView(R.id.lv_car_breakdown)
    ImageView lvCarBreakdown;

    @BindView(R.id.ll_cancel_order)
    LinearLayout llCancelOrder;

    @BindView(R.id.nav_view)
    NavigationView navigationView;


    private OrderReceivedVo orderReceivedVo;
    private double pickupLat;
    private double pickupLng;
    private double dropLat;
    private double dropLng;
    private String customerName;
    private String customerMobileNumber;
    private String customerAvgRating;
    private String profilePicUrl;
    private LatLng dropPoint = null;
    private String totalTimeWithSec = "0.0";
    private static final String SKP = "SKP";
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;
    private boolean cabActiveStatus;
    private float ZOOM_LEVEL = 17f;
    private boolean currentZoom = false;
    private Dialog tripCancelledAlertDialog;
    public static final int[] carIcons = new int[]{R.drawable.car, R.drawable.car_2, R.drawable.car_3, R.drawable.car_4, R.drawable.car_4, R.drawable.car_4, R.drawable.car_4};
    private Collection<Object> typeId;
    private int count = 0;

    private boolean _80MetersPushSent;
    private boolean _250MetersPushSent;
    private boolean _500MetersPushSent;
    private boolean _1250MetersPushSent;

    private static final int _80 = 80;
    private static final int _250 = 250;
    private static final int _500 = 500;
    private static final int _1250 = 1250;
    private boolean onConnectCall = false;
    private int ANIMATE_CAMERA_DELAY = 5000;
    private int ACCURACY = 25;
    private float mDeclination;
    private float[] mRotationMatrix = new float[16];
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TriggerEventListener mTriggerEventListener;
    private ChangeDestinationAlertDialog changeDestinationAlertDialog;
    private ChangeDestinationPojo changeDestinationPojo;
    private int notifyID = 9001;
    private int onlineOfflineStatus = 1;
    private boolean fromInvoice = false;
    private long pickupTime;
    private long elapsedTime;
    private CustomerAlertDialog carBreakdownDialog;
    private  Dialog tripCanceledAlertDialog;
    private boolean mIsRestoredToTop;

    public static GoogleMapDrawerActivity getInstance() {
        return instance;
    }

    private static GoogleMapDrawerActivity instance;

    private boolean isOrderReceived = false;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        instance = this;

        Common.Log.i("onCreate() : " + this.getClass().getSimpleName() + " : " + this.hashCode());


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();


        Common.Log.i("After onCreate.build() : " + this.getClass().getSimpleName() + this.hashCode());

        Common.Log.i("this.getClass().getSimpleName() : " + this.getClass().getSimpleName());

        Common.Log.i("this.hashCode() Before setSocketResponseListener() : " + this.hashCode());


//        sendSocketInitiationRequest();

//        sendSocketRequestToOnSocketOpenService(1,Constants.RequestCodes.ONCREATE_REQUEST_CODE);

//        TATX.getInstance().setSocketResponseListener(this);

        Common.Log.i("this.getClass().getSimpleName() : " + this.getClass().getSimpleName());
        Common.Log.i("this.hashCode() After setSocketResponseListener: " + this.hashCode());

        if (!ServiceUrls.CURRENT_ENVIRONMENT.getEnvironmentType().equals("PRODUCTION ENVIRONMENT")) {
            Common.customToast(this, ServiceUrls.CURRENT_ENVIRONMENT.getEnvironmentType());
//            Common.customToast(this, DensityType.getEnumField(getResources().getDisplayMetrics().density).getDensityString());

        }


        if (!Common.isGooglePlayServicesAvailable(this)) {
            finish();
        }


        Common.Log.i("After mGoogleApiClient.build() : " + this.getClass().getSimpleName() + this.hashCode());


        MapsInitializer.initialize(this);


        Common.Log.i("After mGoogleApiClient.connect() : " + this.getClass().getSimpleName() + this.hashCode() + mGoogleApiClient.isConnected());


        setContentView(R.layout.activity_googlemap_drawer);

        ButterKnife.bind(this);

        setDisableHideKeyBoard(true);

        displayGoogleLocationSettingPage(this);

        setUnReadNotificationCountChangeListener(this);

        initilaizedAll();













    }


    private void sendSocketInitiationRequest() {


        HashMap<String, String> params = new HashMap<>();
        params.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);
        params.put(ServiceUrls.ApiRequestParams.OS, Constants.ANDROID);

        MyService.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.SOCKET_INITIATION, params, false);

        Common.Log.i("sendSocketInitiationRequest : " + this.getClass().getSimpleName() + " : " + this.hashCode());


        Common.Log.i("Inside sendSocketInitiationRequest().");

    }

    private void updateBottomCardViewLayout(double pickLatitude, double pickLongitude, double dropLatitude, double dropLongitude, String customerMobileNumber, String profilePicUrl, String customerName, String customerRating) {
        updateBottomCardViewLayout(pickLatitude, pickLongitude, dropLatitude, dropLongitude, customerMobileNumber, profilePicUrl, customerName, customerRating, true);
    }

    private void updateBottomCardViewLayout(double pickLatitude, double pickLongitude, double dropLatitude, double dropLongitude, String customerMobileNumber, String profilePicUrl, String customerName, String customerRating, boolean drawRoute) {

        Common.Log.i("updateBottomCardViewLayout() Starts.");

        Common.Log.i("dropLatitude : " + dropLatitude);
        Common.Log.i("dropLongitude : " + dropLongitude);

        String destinationText = Common.getCompleteAddressString(this, dropLatitude, dropLongitude);

        Common.Log.i("destinationText : " + destinationText);


        if (dropLatitude != 0.0 && dropLongitude != 0.0) {

            tvDriverCurrentDestinationAddress.setText(Common.getCompleteAddressString(this, dropLatitude, dropLongitude));

            dropPoint = new LatLng(dropLatitude, dropLongitude);

            if (drawRoute) {
                route(new LatLng(pickLatitude, pickLongitude), dropPoint);
            }


        } else {
            llDriverCurrentDestinationAddress.setVisibility(View.GONE);
        }


        btnContact.setTag(customerMobileNumber);

        btnNavigate.setTag(R.string.one, dropLatitude);

        btnNavigate.setTag(R.string.two, dropLongitude);

        Common.Log.i("profilePicUrl : " + profilePicUrl);

//        profilePicUrl = Constants.STATIC_PROFILE_PIC_URL;

//        Picasso.with(this).load(profilePicUrl).into(ivProfilePic);

        Common.setRoundedCroppedBackgroundImage(this, profilePicUrl, ivProfilePic, 100);


        tvCustomerName.setText(customerName);

        tvRating.setText(Common.getRatingText(Double.valueOf(customerRating)));


        Common.Log.i("updateBottomCardViewLayout() Ends.");


    }


    @Override
    protected void onPushNotificationReceived(PushNotificationResponseVo pushNotificationResponseVo) {
        Log.d("PushResponseCodes", "ServiceUrls.PushResponseCodes" + pushNotificationResponseVo);
        switch (pushNotificationResponseVo.code)
        {
            case ServiceUrls.PushResponseCodes._10001:

                Common.Log.i("????? - GoogleMapDrawerActivity :: sendLogoutRequest()");

//                Common.sendLogoutRequest(this,1);

                break;


            case ServiceUrls.PushResponseCodes.FROM_NOTIFICATION_MANAGEMENT_20008:
                sendNotification(pushNotificationResponseVo.message);
                //setNotificationIconCountText(pushNotificationResponseVo.unreadNotificationsCount);
                setMenuCounter(R.id.nav_notification, pushNotificationResponseVo.unreadNotificationsCount);
                break;
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        editor.putString("socketToken", "");

        editor.commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        socketServer.disconnect();

        editor.putString("socketToken", "");

        editor.commit();

        stopLocationUpdates();

        mGoogleApiClient.disconnect();

        if (tripCancelledAlertDialog!=null){
            tripCancelledAlertDialog.cancel();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
       /* sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);*/

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    protected void startLocationUpdates() {
        PendingResult<Status> gfgf = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }



    public void initilaizedAll()
    {

        fromInvoice = getIntent().getBooleanExtra(Constants.FROM_INVOICE, false);

        if ((fromInvoice &&  makeLastTripValue(this)== 0) || (getIntent().getBooleanExtra(Constants.FROM_DISPLAY_ACT, false)  && getIntent().getIntExtra(Constants.ONLINE_STATUS ,0)==1) )
        {
            swOnlineOffline.setChecked(true);
        }
        else
        {
            fromInvoice = false;
        }




//        tatxDataSource = new TatxDataSource(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sqliteDB = new SqliteDB(this);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        editor = sp.edit();

        userid = sp.getInt("userid", 0);

        polylines = new ArrayList<>();

//        socketServer = new SocketServer(this);

//        socketServer.Connect("2", "1", "1,2,3");
        lastTripValue = makeLastTripValue(this);
        makeLastTrip.setChecked(BooleanUtils.toBoolean(lastTripValue));

        btnNavigate.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        makeLastTrip.setOnClickListener(this);
        carBreakdownORCancelOrder.setOnClickListener(this);
        //swOnlineOffline.setOnCheckedChangeListener(this);
      //  swOnlineOffline.setOnClickListener(this);
        getDriverProfileApi();

        navigationLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    if (imagePath != null) {

                        Picasso.with(GoogleMapDrawerActivity.this).load(imagePath).transform(new CircleTransform()).into(prifileimg);

                    }
                    drawer.openDrawer(GravityCompat.START);
                }
                return false;
            }
        });


        navigationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    if (imagePath != null) {

                        Picasso.with(GoogleMapDrawerActivity.this).load(imagePath).transform(new CircleTransform()).into(prifileimg);

                    }
                    drawer.openDrawer(GravityCompat.START);
                }
                return false;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();


        prifileimg = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.prifileimg);
        driverfname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.driverfname);
        driverlname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.driverlname);
        make = (TextView) navigationView.getHeaderView(0).findViewById(R.id.make);
        model = (TextView) navigationView.getHeaderView(0).findViewById(R.id.model);
        type = (TextView) navigationView.getHeaderView(0).findViewById(R.id.type);

        navigationView.setNavigationItemSelectedListener(this);

        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        userProfile = sqliteDB.getUserProfile();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {

            mapFragment = SupportMapFragment.newInstance();

            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        }

        mapFragment.getMapAsync(this);


    }

    private void setData() {

        userProfile = sqliteDB.getUserProfile();

        if (userProfile.size() != 0) {

            driverfname.setText(userProfile.get(0).getFirst_name());

            driverlname.setText(userProfile.get(0).getLast_name());

        }

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.nav_profile:

                Intent intent = new Intent(this, DisplayProfileActivity.class);

                startActivityForResult(intent, Constants.CALL_API_FROM_UPDATE_PROFILE);

                break;


            case R.id.nav_history:

                Intent intent1 = new Intent(this, TripHistoryActivity.class);

                startActivity(intent1);

                break;


            case R.id.nav_earnings:

                Intent intent3 = new Intent(this, EarningsActivity.class);

                startActivity(intent3);

                break;


            case R.id.nav_rating:


                Intent intent4 = new Intent(this, Rating.class);

                startActivity(intent4);

                break;


            case R.id.nav_doc:


                Intent intentLocation = new Intent(this, DocumentsActivity.class);

                startActivity(intentLocation);

                break;

            case R.id.nav_vehicle:


                Intent intentVehicle = new Intent(this, VehiclesActivity.class);

                startActivity(intentVehicle);

                break;


            case R.id.nav_bonus:

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_BONUS_DETAILS, null);

                break;


            case R.id.nav_top_performers:

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_TOP_PERFORMERS_DETAILS, null);

                break;


            case R.id.nav_friend:

//                Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();

                Intent intent6 = new Intent(this, InviteAFriend.class);

                startActivity(intent6);

                break;


            case R.id.nav_help:

                Intent intent7 = new Intent(this, HelpActivity.class);

                startActivity(intent7);

                break;

/*

            case R.id.nav_settings:

                Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();

                Intent intent8 = new Intent(this, Settings.class);

                startActivity(intent8);

                break;
*/


            case R.id.nav_notification:

                //  Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();

                Intent intent9 = new Intent(this, Notifications.class);

                startActivity(intent9);

                break;


            case R.id.nav_about:

                //  Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();

                Intent intent10 = new Intent(this, AboutActivity.class);

                startActivity(intent10);

                break;


            case R.id.nav_logout:
                if (btnTripCurrentStatus.getVisibility() == View.GONE && !swOnlineOffline.isChecked()) {
                    Common.sendLogoutRequest(this,0);
                } else if (btnTripCurrentStatus.getVisibility() == View.VISIBLE) {
                    Common.customToast(this, Common.getStringResourceText(R.string.please_complete_trip));
                } else {
                    Common.customToast(this, Common.getStringResourceText(R.string.please_go_offline));
                }

                break;
            case R.id.nav_online_hours:

                Intent intent2 = new Intent(this, OnlineHoursActivity.class);
                startActivity(intent2);

                break;


        }


        drawer.closeDrawer(GravityCompat.START);

        return true;

    }


    @Override
    public void onConnected(Bundle bundle) {


        Common.Log.i("onConnected : " + this.getClass().getSimpleName() + " : " + this.hashCode());


        startLocationUpdates();
        onConnectCall = true;

        Common.Log.i("After mGoogleApiClient.connect() : " + this.getClass().getSimpleName() + this.hashCode() + mGoogleApiClient.isConnected());
        /*if (mCurrentLocation==null){
            Common.Log.i("mCurrentLocation :1 "+mCurrentLocation);
            while (count<100) {
                Common.Log.i("mCurrentLocation :2 "+mCurrentLocation);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mCurrentLocation!=null){
                    break;
                }
            }
        }*/
       /* if (mCurrentLocation==null){
            Common.customToast(this,"Unable to current fetch location");
            finish();


        }*/

        Common.Log.i("mCurrentLocation : " + mCurrentLocation);

    }

    private Marker addCarMarkerToMap(double lat, double lng) {

        int id = 0;

        if (typeId != null) {
            id = Integer.parseInt(typeId.toString().substring(1, 2)) - 1;

            Log.d("typeId.toString()", typeId.toString());

        }


        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(carIcons[id])));

        // icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        marker.showInfoWindow();

        return marker;


    }

    private void sendDriverCurrentLocationToServer(double currentLat, double currentLng, float locationAccuracy) {

        Common.Log.i("locationAccuracy : " + locationAccuracy);
//        if (locationAccuracy > 75f)
       /* if (locationAccuracy > ACCURACY) {

            Common.Log.i("Driver Current Location not Sent to Server. Due to less Location Accuracy.");
            return;


        }*/


        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put(ServiceUrls.ApiRequestParams.LATITUDE, String.valueOf(currentLat));
        requestParams.put(ServiceUrls.ApiRequestParams.LONGITUDE, String.valueOf(currentLng));

        if (Common.haveInternet(this))
        {
//            requestParams.put(ServiceUrls.ApiRequestParams.CITY, Common.getCompleteAddressString(this, currentLat, currentLng));
        }

        requestParams.put(ServiceUrls.ApiRequestParams.CATEGORY, "1");

        Common.Log.i("this.getClass().getSimpleName() : " + this.getClass().getSimpleName());
        Common.Log.i("this.getClass().getName() : " + this.getClass().getName());
        Common.Log.i("this.getClass().getCanonicalName() : " + this.getClass().getCanonicalName());
        Common.Log.i("SET_DRIVER_LOC - this.hashCode() : " + this.hashCode());

//        Common.customToast(this, "Before Sending Driver Location.");
        Common.Log.i("Before Sending Driver Location.");

        MyService.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.SET_DRIVER_LOC, requestParams, false);


    }

    @Override
    public void onConnectionSuspended(int i) {
        Common.Log.i("After mGoogleApiClient.connect() : " + this.getClass().getSimpleName() + this.hashCode() + mGoogleApiClient.isConnected());

    }


    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(LOG, "onMapReady...");

        googleMap = map;

        //   googleMap.getUiSettings().setRotateGesturesEnabled(false);

        googleMap.setOnCameraChangeListener(this);
        // googleMap.setMyLocationEnabled(true);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }


    }


    public void myCurrentLocationCamera(Location location, float ZOOM_LEVEL1) {
        if (location != null && googleMap != null) {
            // ZOOM_LEVEL = googleMap.getCameraPosition().zoom;
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZOOM_LEVEL1);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            currentZoom = true;
        }
    }


    @OnClick(R.id.btn_trip_current_status)
    void btnTripCurrentStatusClickFunctionality(Button btnTripCurrentStatus) {
        btnTripCurrentStatus.setEnabled(false);

        Common.Log.i("btnTripCurrentStatus.getText().toString() : " + btnTripCurrentStatus.getText().toString());

        if (btnTripCurrentStatus.getText().toString().equals(Common.getStringFromResources(R.string.pickup_customer))) {


            MyService.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.PICKUP_CUSTOMER, getPickupCustomerOrStartTripHM(), true);
/*

            googleMap.clear();
            currentLocationMarker = addCarMarkerToMap(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
*/


            /*

            btnTripCurrentStatus.setText(Common.getStringFromResources(R.string.start_trip));
            btnTripCurrentStatus.setBackgroundResource(R.drawable.accept_ride_button_bg);

            */


        } else if (btnTripCurrentStatus.getText().toString().equals(Common.getStringFromResources(R.string.start_trip))) {

            MyService.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.START_TRIP, getPickupCustomerOrStartTripHM(), true);


        } else if (btnTripCurrentStatus.getText().toString().equals(Common.getStringFromResources(R.string.complete_trip))) {


            ProgressDialog progressDialog = Common.showProgressDialog(this);

            sqliteDB.updateDestination(tripid, String.valueOf(mCurrentLocation.getLatitude()), String.valueOf(mCurrentLocation.getLongitude()));

            endTime = System.currentTimeMillis();

            if (sp.contains(Constants.START_TRIP_TIME)) {
                startTime = sp.getLong(Constants.START_TRIP_TIME, 0);
            }
            if (sp.contains(Constants.PICKUP_TRIP_TIME)) {
                pickupTime = sp.getLong(Constants.PICKUP_TRIP_TIME, 0);
            }

/*

            Common.Log.i("DurationInSec - startTime : "+startTime);

            Common.Log.i("DurationInSec - endTime : "+endTime);

            totalTime = (endTime - startTime) / (60 * 1000);

            long remainingSeconds = (endTime - startTime) % (60 * 1000);

            Common.Log.i("DurationInSec - totalTime (Before Adding Seconds) : "+totalTime);
            Common.Log.i("DurationInSec - remainingSeconds : "+remainingSeconds);
            Common.Log.i("DurationInSec - totalTime (After Adding Seconds) : "+totalTime);

            totalTime = totalTime+remainingSeconds;



*/
            long timeDifferencePickUpToStart =  startTime - pickupTime ;

            if (timeDifferencePickUpToStart>TWO_MINITUS){

                elapsedTime=endTime - (pickupTime+TWO_MINITUS);
            }
            else {

                 elapsedTime = endTime - startTime;
            }



            Common.Log.i("DurationInSec - elapsedTime : " + elapsedTime);


//            String.format("%02d min, %02d sec", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            totalTimeWithSec = String.format(Locale.ENGLISH, "%02d.%02d", TimeUnit.MILLISECONDS.toMinutes(elapsedTime), TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)));


            Common.Log.i("DurationInSec - totalTimeWithSec : " + totalTimeWithSec);


            list = sqliteDB.getWayLocationsLogList();

            totalDistance = 0;

            Common.Log.i("list.size() : " + list.size());

            if (list.size() != 0) {

                for (int i = 0; i < list.size() - 1; i++) {

                    Location location = new Location("");
                    location.setLatitude(Double.parseDouble(list.get(i).getLatitude()));
                    location.setLongitude(Double.parseDouble(list.get(i).getLongitude()));

                    Location location2 = new Location("");
                    location2.setLatitude(Double.parseDouble(list.get(i + 1).getLatitude()));
                    location2.setLongitude(Double.parseDouble(list.get(i + 1).getLongitude()));
                    distance = location.distanceTo(location2);

                    Log.i("Calc-distance", "distance : " + distance);

                    totalDistance = (totalDistance + distance);

                    Log.i("Calc-distance", "In-for-loop-totalDistance : " + totalDistance);


                }

                Log.i("Calc-distance", "After-for-loop-totalDistance : " + totalDistance);


                totalDistance = totalDistance / 1000;

                Log.i("Calc-distance", "totalDistance (in kms) : " + totalDistance);

                Common.Log.i("Total Distance in Local Language : " + new DecimalFormat("##.##").format(totalDistance));

                totalDistance = Double.parseDouble(new DecimalFormat("##.##").getInstance(Locale.ENGLISH).format(totalDistance));


                Log.i("Calc-distance", "totalDistance (in kms) rounded : " + totalDistance);


            }
            Common.dismissProgressDialog(progressDialog);
            callEndTripSocket("0");


        }


    }

    private HashMap<String, String> getPickupCustomerOrStartTripHM() {
        HashMap<String, String> params = new HashMap();
        params.put(ServiceUrls.ApiRequestParams.TRIPID, String.valueOf(tripid));
        params.put(ServiceUrls.ApiRequestParams.ORDERID, String.valueOf(orderid));
        params.put(ServiceUrls.ApiRequestParams.LATITUDE, String.valueOf(mCurrentLocation.getLatitude()));
        params.put(ServiceUrls.ApiRequestParams.LONGITUDE, String.valueOf(mCurrentLocation.getLongitude()));
        params.put(ServiceUrls.ApiRequestParams.LOCATION, Common.getCompleteAddressString(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        params.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);
        params.put(ServiceUrls.ApiRequestParams.STATUS, "1");
        params.put(ServiceUrls.ApiRequestParams.TYPE, String.valueOf(typeid));

        return params;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fl_cancel_order_car_breakdown:
                btnTripCurrentStatus.setEnabled(false);

                if (btnTripCurrentStatus.getVisibility() == View.VISIBLE && (btnTripCurrentStatus.getText().toString().equals(Common.getStringFromResources(R.string.pickup_customer)) || btnTripCurrentStatus.getText().toString().equals(Common.getStringFromResources(R.string.start_trip)))) {

                    HashMap<String, String> hashMap = new HashMap<String, String>();

                    hashMap.put(ServiceUrls.ApiRequestParams.ROLE_ID, Constants.DRIVER_ROLE_ID);

                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_REASONS_FOR_CANCEL, hashMap);


                } else {


                    lastTripValue = 1;
                    carBreakdownDialog= new CustomerAlertDialog(GoogleMapDrawerActivity.this, Common.getStringFromResources(R.string.do_you_want_to_continue), new DialogClickListener() {
                        @Override
                        public void onCancelClick() {

                            Common.dismissDialog(carBreakdownDialog);
                        }

                        @Override
                        public void onOkClick() {
                            callEndTripSocket("1");
                            Common.dismissDialog(carBreakdownDialog);
                        }
                    });
                    carBreakdownDialog.show();


                }

                btnTripCurrentStatus.setEnabled(true);

                break;


            case R.id.btn_contact:

                btnTripCurrentStatus.setEnabled(false);

                Toast.makeText(getApplicationContext(), Common.getStringResourceText(R.string.please_wait), Toast.LENGTH_LONG).show();


                final Dialog dialog = Common.getAppThemeCustomDialog(GoogleMapDrawerActivity.this, R.layout.contact_number_dialog, 100);

                TextView tvContactNumber = (TextView) dialog.findViewById(R.id.tv_contact_number);

                tvContactNumber.setText("+" + btnContact.getTag());

                LinearLayout llCall = (LinearLayout) dialog.findViewById(R.id.ll_call);

                LinearLayout llMessage = (LinearLayout) dialog.findViewById(R.id.ll_message);

                llCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:+" + btnContact.getTag()));

                        if (ActivityCompat.checkSelfPermission(GoogleMapDrawerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        startActivity(intent);


                        dialog.dismiss();


                    }

                });


                llMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", String.valueOf(btnContact.getTag()), null)));

                        dialog.dismiss();


                    }

                });


                dialog.show();


/*




                try
                {

                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:+" + String.valueOf(mobile)));
                    intent.setData(Uri.parse("tel:+" + btnContact.getTag()));

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }

                    startActivity(intent);

                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), Common.getStringResourceText(R.string.error_in_your_phone_call) + e.getMessage(), Toast.LENGTH_LONG).show();
                }
*/

                btnTripCurrentStatus.setEnabled(true);

                break;


            case R.id.btn_navigate:
                btnTripCurrentStatus.setEnabled(false);
                if ((Double) btnNavigate.getTag(R.string.one) != 0 && (Double) btnNavigate.getTag(R.string.two) != 0) {

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + btnNavigate.getTag(R.string.one) + "," + btnNavigate.getTag(R.string.two)));
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            createFloatingButton();
                        }
                    }, 5000);


                } else {
                    Common.customToast(this, Common.getStringResourceText(R.string.destination_details_are_not_available));
                }

                btnTripCurrentStatus.setEnabled(true);
                break;


            case R.id.make_last_trip:
                if (makeLastTrip.isChecked()) {
                    lastTripValue = 1;
                } else {
                    lastTripValue = 0;
                }
                Common.getDefaultSP(this).edit().putInt(Constants.SharedPreferencesKeys.LAST_TRIP_VALUE, lastTripValue).commit();
                break;
        }
    }

    private void createFloatingButton()
    {


        Common.Log.i("Inside createFloatingButton().");



        floatingFaceBubble = new ImageView(this);
        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.mipmap.ic_launcher);
        windowManager = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x = 25;
        myParams.y = 100;
        // add a floatingfacebubble icon in window
        windowManager.addView(floatingFaceBubble, myParams);
//        windowManager.updateViewLayout(floatingFaceBubble, myParams);

        try {
            //for moving the picture on touch and slide
            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                    if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                        windowManager.removeView(floatingFaceBubble);
                        return false;
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            if (initialTouchX == event.getRawX() && initialTouchY == event.getRawY()) {
                                return false;// to handle Click
                            }


                            break;

                        case MotionEvent.ACTION_UP:
                            if (initialTouchX == event.getRawX() && initialTouchY == event.getRawY()) {
                                return false;// to handle Click
                            }
                            break;

                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;

                    }
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        floatingFaceBubble.setClickable(true);


        floatingFaceBubble.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Common.Log.i("Inside floatingFaceBubble onClick().");


                if (Build.VERSION.SDK_INT > 19)
                {
//                Intent intent = new Intent(GoogleMapDrawerActivity.this, GoogleMapDrawerActivity.class);

                    Intent intent = new Intent(getApplicationContext(), GoogleMapDrawerActivity.class);


                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);


                    startActivity(intent);
                }
                else
                {

                    removeFloatingFaceBubble();

                    Common.restartApp(GoogleMapDrawerActivity.this);

                }


                floatingFaceBubble.setClickable(false);

                Common.Log.i("After startActivity().");


//                Common.killPackageProcesses(getApplicationContext(),"com.google.android.apps.maps");



            }

        });


    }









    @Override
    protected void onRestart() {

        Log.i(SKP, "Inside onRestart().");

        super.onRestart();

        removeFloatingFaceBubble();


    }

    private void removeFloatingFaceBubble()
    {

        if (windowManager != null && floatingFaceBubble.isShown())
        {
            windowManager.removeView(floatingFaceBubble);
        }

    }


  /*  @OnClick(R.id.btn_online_offline)
    void changeOnlineOfflineStatus() {

        Log.i(TAG_ONLINEOFFLINE, "Inside changeOnlineOfflineStatus().");

        int onlineOfflineStatus = btnOnlineOffline.getText().toString().equals(Common.getStringFromResources(R.string.go_online)) ? 1 : 0;

        sendSocketRequestToOnSocketOpenService(onlineOfflineStatus);

    }*/

    private void sendSocketRequestToOnSocketOpenService(int onlineOfflineStatus) {
        sendSocketRequestToOnSocketOpenService(onlineOfflineStatus, -1);
    }

    private void sendSocketRequestToOnSocketOpenService(int onlineOfflineStatus, int requestCode)
    {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);
        params.put(ServiceUrls.ApiRequestParams.ONLINE, String.valueOf(onlineOfflineStatus));
        params.put(ServiceUrls.ApiRequestParams.OS, Constants.ANDROID);

        Common.Log.i("? - MyService.getInstance() : "+ MyService.getInstance());

        MyService.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.ON_SOCKET_OPEN, params, requestCode);

    }


    public Location previousLocation;

    @Override
    public void onLocationChanged(Location location)
    {

     /*   GeomagneticField field = new GeomagneticField(
                (float)location.getLatitude(),
                (float)location.getLongitude(),
                (float)location.getAltitude(),
                System.currentTimeMillis()
        );

        // getDeclination returns degrees
        mDeclination = field.getDeclination();*/

        Common.Log.i("onLocationChanged : " + this.getClass().getSimpleName() + " : " + this.hashCode());
        Common.Log.i("GoogleMapDrawerActivity.this.hascode() : " + this.getClass().getSimpleName() + " : " + GoogleMapDrawerActivity.this.hashCode());

        Common.Log.i("Inside onLocationChanged(): " + location.getAccuracy());
        Common.Log.i("googleMap : " + googleMap + "Location " + location.getLatitude() + "," + location.getLongitude());

//
   //     updateCameraBearing(googleMap, location.getBearing());


        Common.Log.i("Inside onLocationChanged() afer updateCameraBearing : " + location.getAccuracy());


        mCurrentLocation = location;

        if (mCurrentLocation != null) {
            if (onConnectCall) {
                onConnectCall = false;

                if (googleMap != null) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);

                }

                driverOnTripVo = (DriverOnTripVo) getIntent().getSerializableExtra(Constants.KEY_1);

                Common.Log.i("? - driverOnTripVo : "+driverOnTripVo);

                if (driverOnTripVo != null)
                {

                    switch (driverOnTripVo.driverStatus) {
                        case Constants.DriverStatuses.OFFLINE:
                            swOnlineOffline.setChecked(false);
                            //    updateOnlineOfflineButtonUI(false);
//                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                    navigationLayout.setVisibility(View.VISIBLE);
                            break;

                        case Constants.DriverStatuses.ONLINE:
                            swOnlineOffline.setChecked(true);
                            //                       updateOnlineOfflineButtonUI(true);
//                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            //                   navigationLayout.setVisibility(View.VISIBLE);
                            break;


                        case Constants.DriverStatuses.RIDE_ACCEPTED:
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            navigationLayout.setVisibility(View.GONE);
                            lvCarBreakdown.setVisibility(View.GONE);
                            llCancelOrder.setVisibility(View.VISIBLE);
                            afterAcceptRideView();

                            Ontrip ontrip = driverOnTripVo.ontrip;

                            updateBottomCardViewLayout(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), ontrip.pickLatitude, ontrip.pickLongitude, ontrip.mobile, ontrip.profile, ontrip.username, ontrip.rating, true);

                            break;


                        case Constants.DriverStatuses.PICKED_UP_CUSTOMER:
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            navigationLayout.setVisibility(View.GONE);
                            lvCarBreakdown.setVisibility(View.GONE);
                            llCancelOrder.setVisibility(View.VISIBLE);

                            afterAcceptRideView();

                            btnTripCurrentStatus.setText(Common.getStringFromResources(R.string.start_trip));

                            btnTripCurrentStatus.setBackgroundResource(R.drawable.accept_ride_button_bg);

                            ontrip = driverOnTripVo.ontrip;

                            updateBottomCardViewLayout(ontrip.pickLatitude, ontrip.pickLongitude, ontrip.dropLatitude, ontrip.dropLongitude, ontrip.mobile, ontrip.profile, ontrip.username, ontrip.rating, false);


                            break;


                        case Constants.DriverStatuses.TRIP_STARTED:
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            navigationLayout.setVisibility(View.GONE);

                            lvCarBreakdown.setVisibility(View.VISIBLE);
                            llCancelOrder.setVisibility(View.GONE);

                            afterAcceptRideView();
                            btnTripCurrentStatus.setText(Common.getStringFromResources(R.string.complete_trip));

                            btnTripCurrentStatus.setBackgroundResource(R.drawable.decline_button_bg);

                            ontrip = driverOnTripVo.ontrip;

                            updateBottomCardViewLayout(ontrip.pickLatitude, ontrip.pickLongitude, ontrip.dropLatitude, ontrip.dropLongitude, ontrip.mobile, ontrip.profile, ontrip.username, ontrip.rating, true);


                            break;


                    }


                    Ontrip ontrip = driverOnTripVo.ontrip;


                    if (ontrip != null) {

                        tripid = ontrip.tripid;

                        orderid = ontrip.orderid;

                        typeid = ontrip.typeid;

                        pickupLat = ontrip.pickLatitude;

                        pickupLng = ontrip.pickLongitude;

                        dropLat = ontrip.dropLatitude;

                        dropLng = ontrip.dropLongitude;

                        customerName = ontrip.username;

                        customerMobileNumber = ontrip.mobile;

                        profilePicUrl = ontrip.profile;

                        customerAvgRating = ontrip.rating;


                    }


                }
                else
                {
                    Common.Log.i("? - driverOnTripVo : else"+driverOnTripVo);
                    swOnlineOffline.setChecked(false);
                    // updateOnlineOfflineButtonUI(false);
                }


            }


            myCurrentLocationCamera(location, ZOOM_LEVEL);
                    /*Remove this code once they send driver last location details in driverontrip service call*/
//            btnNavigate.setTag(R.string.one, mCurrentLocation.getLatitude());
//            btnNavigate.setTag(R.string.two, mCurrentLocation.getLongitude());
                    /*Remove this code once they send driver last location details in driverontrip service call*/

            if (currentLocationMarker == null) {
                previousLocation = location;

                Log.i("MJP", "rotationValue ak1: " + previousLocation + " " + previousLocation.distanceTo(location));

                currentLocationMarker = addCarMarkerToMap(location.getLatitude(), location.getLongitude());

                return;

            } else if (previousLocation.distanceTo(location) > ACCURACY && location.getAccuracy() <= ACCURACY) {
                float rotationValue = previousLocation.bearingTo(location);

                Log.i("MJP", "rotationValue ak2: " + previousLocation.distanceTo(location));

                rotateMarker(currentLocationMarker, rotationValue);

                animateMarker(googleMap, currentLocationMarker, new LatLng(location.getLatitude(), location.getLongitude()), false);

                Common.Log.i("new LatLng(location.getLatitude(), location.getLongitude()) : " + new LatLng(location.getLatitude(), location.getLongitude()));
                previousLocation = location;
                // currentLocationMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
            Log.i("MJP", "rotationValue ak2: " + previousLocation.distanceTo(location));

            Common.Log.i("Before Inside Online Status 1");

            if (!address.equalsIgnoreCase("")) {
                address = Common.getAddressString(getApplicationContext(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            }

            Common.Log.i("Before Inside Online Status 2");

            if (swOnlineOffline.isChecked() && !isOrderReceived) {

                Common.Log.i("Inside Online Status");


                sendDriverCurrentLocationToServer(previousLocation.getLatitude(), previousLocation.getLongitude(), previousLocation.getAccuracy());


            }


            if (btnTripCurrentStatus.getVisibility() == View.VISIBLE && btnTripCurrentStatus.getText().toString().equals(Common.getStringFromResources(R.string.pickup_customer)))
            {

                Common.Log.i("Inside PickuScreen Condition.");

                Common.Log.i("pickupLat" + pickupLat);

                Common.Log.i("pickupLng" + pickupLng);

//                if (pickupLat != 0 && pickupLng != 0) {
                if (pickupLat != 0 && pickupLng != 0) {
                    Location location1 = new Location("");
                    location1.setLatitude(pickupLat);
                    location1.setLongitude(pickupLng);

                    float distanceToPickUpLocation = mCurrentLocation.distanceTo(location1);

                    Common.Log.i("distanceToPickUpLocation : " + distanceToPickUpLocation);


                    if (distanceToPickUpLocation > _500 && distanceToPickUpLocation <= _1250 && !_1250MetersPushSent) {
                        sendCabArrivedRequest(_1250, orderid, tripid);

                        _1250MetersPushSent = true;

                    } else if (distanceToPickUpLocation > _250 && distanceToPickUpLocation <= _500 && !_500MetersPushSent) {
                        sendCabArrivedRequest(_500, orderid, tripid);

                        _500MetersPushSent = true;
                    } else if (distanceToPickUpLocation > _80 && distanceToPickUpLocation <= _250 && !_250MetersPushSent) {
                        sendCabArrivedRequest(_250, orderid, tripid);
                        _250MetersPushSent = true;
                    } else if (distanceToPickUpLocation <= _80 && !_80MetersPushSent) {
                        sendCabArrivedRequest(_80, orderid, tripid);
                        _80MetersPushSent = true;
                    }


                }


                sendDriverCurrentLocationToServer(previousLocation.getLatitude(), previousLocation.getLongitude(), previousLocation.getAccuracy());


            }


            if (btnTripCurrentStatus.getVisibility() == View.VISIBLE && btnTripCurrentStatus.getText().toString().equals(Common.getStringFromResources(R.string.complete_trip)) && location.getAccuracy() <= ACCURACY) {

                Common.Log.i("Check Insert DB - " + "Current LatLng (action == 2) :" + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());

                long insertedRowId = sqliteDB.insertWayDetailsLog(String.valueOf(mCurrentLocation.getLatitude()), String.valueOf(mCurrentLocation.getLongitude()), "0", "", "");

                Common.Log.i("Check Insert DB - " + "insertedRowId : " + insertedRowId);
                Common.Log.i("Check Insert DB - " + "btnTripCurrentStatus.getText() : " + btnTripCurrentStatus.getText());

                sendDriverCurrentLocationToServer(previousLocation.getLatitude(), previousLocation.getLongitude(), previousLocation.getAccuracy());


            }


//                            Common.Log.i("Arabic Text Comparison : "+"الاسم الأول".equals("الاسم الأول"));


        }


    }


    private void updateCameraBearing(GoogleMap googleMap, float bearing) {


        Common.Log.i("googleMap : " + googleMap + " bearing " + bearing);

        if (googleMap == null) return;

        CameraPosition camPos = CameraPosition.builder(googleMap.getCameraPosition() /*Current Camera*/).bearing(bearing).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));

    }


    private void sendCabArrivedRequest(int meters, int orderid, int tripid) {

        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put(ServiceUrls.ApiRequestParams.ARRIVED, String.valueOf(meters));
        requestParams.put(ServiceUrls.ApiRequestParams.ORDERID, String.valueOf(orderid));
        requestParams.put(ServiceUrls.ApiRequestParams.TRIPID, String.valueOf(tripid));

        MyService.getInstance().sendSocketRequest(GoogleMapDrawerActivity.this, ServiceUrls.RequestNames.CAB_ARRIVED, requestParams, false);

    }


    private void rotateMarker(final Marker marker, final float toRotation) {
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Common.Log.i("After monConnectionFailed : " + this.getClass().getSimpleName() + this.hashCode() + mGoogleApiClient.isConnected());

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

            case ServiceUrls.RequestNames.PICKUP_CUSTOMER:

                PickupCustomerVo pickupCustomerVo = Common.getSpecificDataObject(apiResponseVo.data, PickupCustomerVo.class);


                if (pickupCustomerVo.cancelledByCustomer) {
                    tripCancelledAlertDialog = showTripCancelledAlertDialog(this, "Sorry !", pickupCustomerVo.status);
                } else {

                    googleMap.clear();
                    currentLocationMarker = addCarMarkerToMap(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                    llDriverCurrentDestinationAddress.setVisibility(View.GONE);

                    btnTripCurrentStatus.setText(Common.getStringFromResources(R.string.start_trip));
                    btnTripCurrentStatus.setBackgroundResource(R.drawable.accept_ride_button_bg);

//                updateBottomCardViewLayout(pickupLat,pickupLng,dropLat,dropLng,customerMobileNumber,profilePicUrl,customerName,customerAvgRating);
                    updateBottomCardViewLayout(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), dropLat, dropLng, customerMobileNumber, profilePicUrl, customerName, customerAvgRating, false);

                    long pickupTime = System.currentTimeMillis();

                    editor.putLong(Constants.PICKUP_TRIP_TIME, pickupTime);

                    editor.commit();
                }

                btnTripCurrentStatus.setEnabled(true);


                break;


            case ServiceUrls.RequestNames.START_TRIP:

                // carBreakdownORCancelOrder.setBackgroundResource(R.drawable.carbroakdown);

                lvCarBreakdown.setVisibility(View.VISIBLE);
                llCancelOrder.setVisibility(View.GONE);


                StartTripVo startTripVo = Common.getSpecificDataObject(apiResponseVo.data, StartTripVo.class);

                Common.Log.i("startTripVo : " + startTripVo);

                googleMap.clear();

                currentLocationMarker = addCarMarkerToMap(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                llDriverCurrentDestinationAddress.setVisibility(View.VISIBLE);

                btnTripCurrentStatus.setText(Common.getStringFromResources(R.string.complete_trip));

                btnTripCurrentStatus.setBackgroundResource(R.drawable.decline_button_bg);

                startTime = System.currentTimeMillis();

                editor.putLong(Constants.START_TRIP_TIME, startTime);

                editor.commit();

                Integer numberOfRowsDeleted = sqliteDB.deleteWayLocationLogs();

                Common.Log.i("Check Insert DB - numberOfRowsDeleted : " + numberOfRowsDeleted);

                updateBottomCardViewLayout(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), dropLat, dropLng, customerMobileNumber, profilePicUrl, customerName, customerAvgRating);
                btnTripCurrentStatus.setEnabled(true);

                break;


            case ServiceUrls.RequestNames.DESTINATION_REACH_TIME_BROADCAST:
/*
                String destinationReachTime = broadcastLocationVoSB.destinationReachTime;

                if (destinationReachTime != null)
                {

                    tvDestinationReachTime.setText("Destination Reach Time : " + destinationReachTime + " min");

                }
                else
                {
                    tvDestinationReachTime.setText("Destination not Selected");

                }
                */

                Common.Log.i("apiResponseVo.data.toString() : " + apiResponseVo.data.toString());


                break;


            case ServiceUrls.RequestNames.END_TRIP:


                ProgressDialog progressDialog = Common.showProgressDialog(this);

                if (list != null) {
                    String polyString = Common.encodePolylinrByLntLatList(list);

                    Log.d("polyString", polyString);

                    sqliteDB.updatePolylines(tripid, polyString);

                }

                Common.dismissProgressDialog(progressDialog);


                EndTripVo endTripVo = Common.getSpecificDataObject(apiResponseVo.data, EndTripVo.class);

                Intent intent = new Intent(GoogleMapDrawerActivity.this, InvoiceActivity.class);

                intent.putExtra(Constants.KEY_1, endTripVo);

                intent.putExtra(Constants.IntentKeys.LAST_TRIP, lastTripValue);

                startActivity(intent);

                finish();

                break;


            case ServiceUrls.RequestNames.ON_SOCKET_OPEN:

                OnSocketOpenVo onSocketOpenVo = Common.getSpecificDataObject(apiResponseVo.data, OnSocketOpenVo.class);

                if (requestId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {
                    break;
                }


//                boolean isOnline = btnOnlineOffline.getText().toString().equals(Common.getStringFromResources(R.string.go_online));
//
//                updateOnlineOfflineButtonUI(isOnline);
                Common.Log.i("onCheckedChanged..2 "+swOnlineOffline.isChecked() +onSocketOpenVo.onlineStatus);
                if (onSocketOpenVo.onlineStatus==1) {
                    swOnlineOffline.setChecked(true);
                } else {
                    swOnlineOffline.setChecked(false);
                }
                Common.Log.i("onCheckedChanged..3 "+swOnlineOffline.isChecked());
                Common.Log.i("tripCancelledAlertDialog : " + tripCancelledAlertDialog);

                if (tripCancelledAlertDialog != null) {
                    Common.Log.i("tripCancelledAlertDialog.isShowing() : " + tripCancelledAlertDialog.isShowing());
                }

                if (tripCancelledAlertDialog != null && tripCancelledAlertDialog.isShowing()) {
                    Intent launchIntentForPackage = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    launchIntentForPackage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntentForPackage);

                    Common.Log.i("Inside tripCancelledAlertDialog Condition.");

                    tripCancelledAlertDialog.dismiss();

                }


                break;


            case ServiceUrls.RequestNames.ORDER_RECEIVED:


                isOrderReceived = true;


                orderReceivedVo = Common.getSpecificDataObject(apiResponseVo.data, OrderReceivedVo.class);

                tripid = orderReceivedVo.tripid;
                orderid = orderReceivedVo.orderid;
                typeid = orderReceivedVo.typeid;
                pickupLat = Double.parseDouble(orderReceivedVo.pickLatitude);
                pickupLng = Double.parseDouble(orderReceivedVo.pickLongitude);
                dropLat = Double.parseDouble(orderReceivedVo.dropLatitude);
                dropLng = Double.parseDouble(orderReceivedVo.dropLongitude);
                customerName = orderReceivedVo.customername;
                customerMobileNumber = orderReceivedVo.mobile;
                profilePicUrl = orderReceivedVo.profile;
                customerAvgRating = orderReceivedVo.rating;

                Common.Log.i("customerName - ORDER_RECEIVED: " + customerName);

                Common.Log.i("this.hashCode() before startActivity: " + this.hashCode());

                Intent acceptRejectIntent = new Intent(GoogleMapDrawerActivity.this, AcceptRejectRideActivity.class);

                acceptRejectIntent.putExtra(Constants.KEY_1, orderReceivedVo);

                instance = this;

                Common.Log.i("instance.hashCode() : " + instance.hashCode());

                Common.Log.i("mGoogleApiClient.isConnected() : " + mGoogleApiClient.isConnected());
                Common.Log.i("Before startActivityForResult." + this.getClass().getSimpleName() + " : " + this.hashCode());

//                startActivityForResult(acceptRejectIntent,Constants.RequestCodes.RIDE_ACCEPTED_REQUEST_CODE);
                startActivity(acceptRejectIntent);


                break;

            case ServiceUrls.RequestNames.ORDER_CANCELED:

                OrderCanceledVo orderCanceledVo = Common.getSpecificDataObject(apiResponseVo.data, OrderCanceledVo.class);
                Common.makeAppInForground(this,GoogleMapDrawerActivity.class);

                tripCancelledAlertDialog = showTripCancelledAlertDialog(this, Common.getStringResourceText(R.string.sorry), orderCanceledVo.message);


                break;

            case ServiceUrls.RequestNames.SOCKET_INITIATION:

                SocketInitiationVo socketInitiationVo = Common.getSpecificDataObject(apiResponseVo.data, SocketInitiationVo.class);

                Common.Log.i("socketInitiationVo.success : " + socketInitiationVo.success);

                if (socketInitiationVo.onlineStatus == Constants.DriverStatuses.SOCKET_DISCONNECTED)
                {
                    sendSocketRequestToOnSocketOpenService(1,-1);
                }

                break;

            case ServiceUrls.RequestNames.DRIVER_CANCEL_TRIP:

                DriverCancelTripVo driverCancelTripVo = Common.getSpecificDataObject(apiResponseVo.data, DriverCancelTripVo.class);

                Common.customToast(this, driverCancelTripVo.status);

                Common.restartApp(this);


                break;

            case ServiceUrls.RequestNames.DESTINATION_CHANGED:

                Common.makeAppInForground(this,GoogleMapDrawerActivity.class);

                changeDestinationPojo = Common.getSpecificDataObject(apiResponseVo.data, ChangeDestinationPojo.class);

                changeDestinationAlertDialog = new ChangeDestinationAlertDialog(this, changeDestinationPojo.dropLocation, new ChangeDestinationAlertDialog.DialogClickListener() {
                    @Override
                    public void onOkClick() {
                        HashMap<String, String> params = new HashMap();
                        params.put(ServiceUrls.ApiRequestParams.TRIPID, String.valueOf(tripid));
                        params.put(ServiceUrls.ApiRequestParams.ORDERID, String.valueOf(orderid));
                        params.put(ServiceUrls.ApiRequestParams.DROP_LATITUDE, changeDestinationPojo.dropLatitude);
                        params.put(ServiceUrls.ApiRequestParams.DROP_LONGITUDE, changeDestinationPojo.dropLongitude);
                        params.put(ServiceUrls.ApiRequestParams.DROP_LOCATION, changeDestinationPojo.dropLocation);

                        // new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CHANGE_DESTINATION,params,true);
                        MyService.getInstance().sendSocketRequest(GoogleMapDrawerActivity.this, ServiceUrls.RequestNames.CONFIRM_CHANGE_DESTINATION, params);
                        dropLat = Double.parseDouble(changeDestinationPojo.dropLatitude);
                        dropLng = Double.parseDouble(changeDestinationPojo.dropLongitude);
                        btnNavigate.setTag(R.string.one, dropLat);

                        btnNavigate.setTag(R.string.two, dropLng);

                      //  Log.d("btnNavigate..",""+btnNavigate.getTag(R.string.two));

                        changeDestinationAlertDialog.dismiss();


                    }
                });
                changeDestinationAlertDialog.setCancelable(false);
                changeDestinationAlertDialog.show();

                break;


            case ServiceUrls.RequestNames.CONFIRM_CHANGE_DESTINATION:

                // updateBottomCardViewLayout(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),Double.parseDouble(changeDestinationPojo.dropLatitude),Double.parseDouble(changeDestinationPojo.dropLongitude),customerMobileNumber,profilePicUrl,customerName,customerAvgRating);
if ( changeDestinationAlertDialog!=null &&  changeDestinationAlertDialog.isShowing()){
    changeDestinationAlertDialog.dismiss();
}

                if (changeDestinationPojo != null) {
                    llDriverCurrentDestinationAddress.setVisibility(View.VISIBLE);
                    tvDriverCurrentDestinationAddress.setText(changeDestinationPojo.dropLocation);

                    if (mCurrentLocation != null && changeDestinationPojo.dropLatitude != null) {
                        route(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), new LatLng(Double.parseDouble(changeDestinationPojo.dropLatitude), Double.parseDouble(changeDestinationPojo.dropLongitude)));
                    }
                }
                break;


        }

    }

  /*  private void updateOnlineOfflineButtonUI(boolean isOnline)
    {
        int stringId = isOnline ? R.string.go_offline :R.string.go_online;

        int drawableId = isOnline ? R.drawable.offline_button_bg :R.drawable.online_button_bg;

        btnOnlineOffline.setText(Common.getStringFromResources(stringId));

        btnOnlineOffline.setBackgroundResource(drawableId);
    }
*/


    public Bitmap createMarker(Drawable shape) {
        Bitmap mDotMarkerBitmap;
        int px = getResources().getDimensionPixelSize(R.dimen.sp30);
        mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
        shape.draw(canvas);
        return mDotMarkerBitmap;
    }

    public Marker addMrkerFromBitMap(LatLng latLng, Drawable shape) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.fromBitmap(createMarker(shape))));
        return marker;
    }


    /*public void sendLogoutRequest(int logoutDueToPush)
    {

        Common.Log.i("????? - GoogleMapDrawerActivity :: sendLogoutRequest()");

        HashMap<String, String> requestParams = new HashMap();
        requestParams.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);
        requestParams.put(ServiceUrls.ApiRequestParams.DEVICE_ID, Common.getDeviceId(this));
        requestParams.put(ServiceUrls.ApiRequestParams.LOGOUT_DUE_TO_PUSH, String.valueOf(logoutDueToPush));

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.USER_LOGOUT, requestParams);


    }*/


    public void afterAcceptRideView() {

        Common.Log.i("? -Inside afterAcceptRideView() start.");

        swOnlineOffline.setVisibility(View.GONE);
        btnTripCurrentStatus.setVisibility(View.VISIBLE);
        contactsLayout.setVisibility(View.VISIBLE);

        Common.Log.i("Inside afterAcceptRideView() end.");

    }

    public Dialog showTripCancelledAlertDialog(final Context context, String title, String Message) {

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ring);
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(1000);
        mp.start();
        mp.setScreenOnWhilePlaying(true);
        mp.setLooping(true);

        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Common.Log.i("Current_Activity"+cn.getClassName());

        if (cn.getClassName().equalsIgnoreCase("com.tatx.partnerapp.activities.AcceptRejectRideActivity")){
            AcceptRejectRideActivity.getInstance().stopRing();
        }

        tripCanceledAlertDialog=new TripCanceledAlertDialog(this, new TripCanceledAlertDialog.DialogClickListener() {
            @Override
            public void onOkClick() {
                Common.dismissDialog(tripCanceledAlertDialog);


                mp.stop();

                // sendSocketRequestToOnSocketOpenService(1);

                ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                Common.Log.i("Current_Activity"+cn);


                DriverOnTripVo driverOnTripVo = new DriverOnTripVo();
                if (GoogleMapDrawerActivity.makeLastTripValue(GoogleMapDrawerActivity.this)==0)
                {

                    driverOnTripVo.driverStatus = 1;
                }else{
                    driverOnTripVo.driverStatus = 0;
                }
                Common.Log.i("updateDeviceTokenVo.message : " + driverOnTripVo.toString());

                Intent intent = new Intent(GoogleMapDrawerActivity.this, GoogleMapDrawerActivity.class);

                intent.putExtra(Constants.KEY_1, driverOnTripVo);
                intent.putExtra(Constants.FROM_INVOICE, true);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

        tripCanceledAlertDialog.show();


        Common.Log.i("Current_Activity 3"+cn.getClassName());


        return tripCanceledAlertDialog;

    }

    public void route(LatLng start, LatLng end) {
        if (start == null || end == null) {
            if (start == null) {
                /*if (sourceText.getText().length() > 0) {
                    sourceText.setError("Choose location from dropdown.");
                } else {*/
                Toast.makeText(this, Common.getStringResourceText(R.string.please_choose_source), Toast.LENGTH_SHORT).show();
                //  }
            }
            if (end == null) {
               /* if (destination.getText().length() > 0) {
                    destination.setError("Choose location from dropdown.");
                } else {*/
                Toast.makeText(this, Common.getStringResourceText(R.string.please_choose_destination), Toast.LENGTH_SHORT).show();
                //}
            }
        } else {
            Log.d("routepoint", String.valueOf(start.latitude) + " " + String.valueOf(start.longitude) + "end " + String.valueOf(end.latitude) + " " + String.valueOf(end.longitude));

            Common.Log.i("this.getClass().getSimpleName() : " + this.getClass().getSimpleName());
            Common.Log.i("this.getClass().getName() :  " + this.getClass().getName());
            Common.Log.i("this.getClass().getCanonicalName() :   " + this.getClass().getCanonicalName());

            Common.Log.i("this.hashCode() before progressDialog: " + this.hashCode());

//            progressDialog = ProgressDialog.show(this, "Please wait.", "Fetching route information.", true);
//            progressDialog = ProgressDialog.show(GoogleMapDrawerActivity.this, "Please wait.", "Fetching route information.", true);

//            Common.Log.i("PD Issue instance.hashCode() (Before Showing PD):"+instance.hashCode());


            if (!isFinishing()) {
             //   progressDialog = ProgressDialog.show(GoogleMapDrawerActivity.this, Common.getStringResourceText(R.string.please_wait), Common.getStringResourceText(R.string.fetching_route_information));
            }


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
       Common.dismissDialog(progressDialog);
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }


        polylines = new ArrayList<>();

        Log.d("route.length", "" + route.size());
        int dist = route.get(0).getDistanceValue();
        int pos = 0;

        for (int i = 1; i < route.size(); i++) {
            if (dist > route.get(i).getDistanceValue()) {
                dist = route.get(i).getDistanceValue();
                pos = i;
            }
        }


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
/*

        Toast.makeText(getApplicationContext(), "Route " + (pos + 1) + ": distance - " + route.get(pos).getDistanceValue() + ": duration - " + route.get(pos
        ).getDurationValue(), Toast.LENGTH_SHORT).show();
*/

        if (dropPoint != null) {
            // Start marker
            MarkerOptions options = new MarkerOptions();
            options.position(dropPoint);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin));
            googleMap.addMarker(options);
        }


    }

    @Override
    public void onRoutingCancelled() {
        Log.i(LOG, "Routing was cancelled.");
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
                // final LocationSettingsStates s= result.getLocationSettingsStates();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        Toast.makeText(activity, "RESOLUTION_REQUIRED", Toast.LENGTH_SHORT).show();
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
        super.onActivityResult(requestCode, resultCode, data);
        Common.Log.i("o.toString() 3 : " + "onActivityResult - GooglemapDrawerActivity");

        Log.d(LOG, "OnresultAxtivity");

        Common.Log.i("statusorder - o.toString() 3 : " + "requestCode : " + requestCode);
        Common.Log.i("statusorder - o.toString() 3 : " + "resultCode : " + resultCode);
        Common.Log.i("statusorder - o.toString() 3 : " + "data : " + data + getIntent().getStringExtra(Constants.IntentKeys.PROFILE_PIC));


        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        // Toast.makeText(this, "hi i", Toast.LENGTH_SHORT).show();

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        finish();
                        break;


                }

                break;


            case Constants.RequestCodes.RIDE_ACCEPTED_REQUEST_CODE:

                switch (resultCode) {
                    case Constants.ResponseCodes.RIDE_ACCEPTED_RESPONSE_CODE:
                        rideAcceptedFunctionality();
                        break;
                }

                break;


            case Constants.CALL_API_FROM_UPDATE_PROFILE:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        imagePath = data.getStringExtra(Constants.IntentKeys.PROFILE_PIC);
                        Log.d(LOG, "OnresultAxtivity" + imagePath);
                        Picasso.with(GoogleMapDrawerActivity.this).load(imagePath).memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(prifileimg);
                        break;
                }

                break;


        }


//        super.onActivityResult(requestCode,resultCode,data);


    }


    void onResponseFromAcceptRejectActivity(int resultCode) {
        Log.d("statusOrder", "statusOrder 8 ");
        Common.Log.i("Inside onResponseFromAcceptRejectActivity()");

//        Common.Log.i("statusorder - o.toString() 3 : "+"requestCode : "+requestCode);
        Common.Log.i("statusorder - o.toString() 3 : " + "resultCode : " + resultCode);
//        Common.Log.i("statusorder - o.toString() 3 : "+"data : "+data);


        isOrderReceived = false;


        switch (resultCode) {
            case Constants.ResponseCodes.RIDE_ACCEPTED_RESPONSE_CODE:
                Log.d("statusOrder", "statusOrder 9 ");
                rideAcceptedFunctionality();
                makeLastTrip.setChecked(BooleanUtils.toBoolean(makeLastTripValue(this)));
                lastTripValue = makeLastTripValue(this);

                break;
        }


    }


    private void rideAcceptedFunctionality() {

        Common.Log.i("rideAcceptedFunctionality().");

        Common.Log.i("orderReceivedVo : " + orderReceivedVo);

        Common.Log.i("Before afterAcceptRideView().");

        afterAcceptRideView();

        updateBottomCardViewLayout(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), Double.parseDouble(orderReceivedVo.pickLatitude), Double.parseDouble(orderReceivedVo.pickLongitude), customerMobileNumber, profilePicUrl, customerName, customerAvgRating);

        sqliteDB.insertTripDetails(String.valueOf(orderReceivedVo.pickLatitude), String.valueOf(orderReceivedVo.pickLongitude), String.valueOf(orderReceivedVo.dropLatitude), String.valueOf(orderReceivedVo.dropLongitude), String.valueOf(orderReceivedVo.orderid), String.valueOf(orderReceivedVo.tripid), String.valueOf(orderReceivedVo.mobile), "", orderReceivedVo.customername);


    }


    public void getDriverProfileApi() {
        /*String params = "";

        tatxDataSource.callRestApi(String.valueOf(userid), Constants.getDriverProfile, params, Constants.CALL_API_FROM_MAP_ACTIVITY);
*/
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_DRIVER_PROFILE, null);


  /*      progressDialog = ProgressDialog.show(this, "", "Please wait...", true, true);*/

    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS) {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname) {
            case ServiceUrls.RequestNames.GET_DRIVER_PROFILE:

                GetDriverProfileVo getDriverProfileVo = Common.getSpecificDataObject(apiResponseVo.data, GetDriverProfileVo.class);

                make.setText(getDriverProfileVo.make);
                model.setText(getDriverProfileVo.model);
                driverfname.setText(getDriverProfileVo.firstName);
                driverlname.setText(getDriverProfileVo.lastName);
                imagePath = getDriverProfileVo.image;


                Collection<Object> types = CollectionUtils.collect(getDriverProfileVo.type, TransformerUtils.invokerTransformer("getType"));
                cabActiveStatus = getDriverProfileVo.cabActiveStatus;
                Common.Log.i("types.toString() : " + types.toString());
                if (cabActiveStatus) {
//                    typeId = CollectionUtils.collect(getDriverProfileVo.type, TransformerUtils.invokerTransformer("getId"));
                    typeId = CollectionUtils.collect(getDriverProfileVo.type, TransformerUtils.invokerTransformer("getTypeId"));
                    type.setText(TextUtils.join(",", types));
                    if (mCurrentLocation != null) {
                        if (currentLocationMarker == null) {
                            previousLocation = mCurrentLocation;
                            Log.i("MJP", "rotationValue ak1: " + previousLocation + " " + previousLocation.distanceTo(mCurrentLocation));
                            currentLocationMarker = addCarMarkerToMap(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                        }
                    }
                } else {
                    type.setText(Common.getStringResourceText(R.string.vehicle_not_active));
                    swOnlineOffline.setVisibility(View.GONE);
                    Common.customToast(this, Common.getStringResourceText(R.string.you_vehicle_is_not_active_please_contact_tatx_customer_care));
                }
                //  setNotificationIconCountText(getDriverProfileVo.unreadNotificationsCount);
                //  setMenuCounter(R.id.nav_notification,getDriverProfileVo.unreadNotificationsCount);
                setMenuCounter(R.id.nav_notification, getDriverProfileVo.unreadNotificationsCount);
                break;


            case ServiceUrls.RequestNames.USER_LOGOUT:

                Common.Log.i("????? - GoogleMapDrawerActivity :: case ServiceUrls.RequestNames.USER_LOGOUT:");


                /*Common.Log.i("????? - GoogleMapDrawerActivity :: case ServiceUrls.RequestNames.USER_LOGOUT:");

                UserLogoutVo userLogoutVo = Common.getSpecificDataObject(apiResponseVo.data, UserLogoutVo.class);

                Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, false).commit();

                Common.getDefaultSP(this).edit().putInt(Constants.SharedPreferencesKeys.USERID, 0).commit();

                Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.REG_ID, "").commit();

                Common.Log.i("Language Goo"+Locale.getDefault().getDisplayLanguage());

                Common.customToast(this, userLogoutVo.status);

                MyService.getInstance().disconnectSocket();

                startActivity(new Intent(this, SplashActivity.class));

                finish();*/

                Common.logoutResponseFunctionality(this, apiResponseVo);


                break;


            case ServiceUrls.RequestNames.GET_REASONS_FOR_CANCEL:

                GetReasonsForCancelVo getReasonsForCancelVo = Common.getSpecificDataObject(apiResponseVo.data, GetReasonsForCancelVo.class);
                Reason reason = new Reason();
                reason.reason = Common.getStringFromResources(R.string.my_reason_is_not_listed);
                reason.reasonId = "-1";
                getReasonsForCancelVo.reasons.add(reason);
                showChooseReasonForCancelDialog(getReasonsForCancelVo.reasons);

                break;

/*

            case ServiceUrls.RequestNames.DRIVER_CANCEL_TRIP:

                DriverCancelTripVo driverCancelTripVo = Common.getSpecificDataObject(apiResponseVo.data, DriverCancelTripVo.class);

                Common.customToast(this,driverCancelTripVo.status);


                break;
*/


            case ServiceUrls.RequestNames.GET_BONUS_DETAILS:

                GetBonusDetailsVo getBonusDetailsVo = Common.getSpecificDataObject(apiResponseVo.data, GetBonusDetailsVo.class);


                Intent bonusIntent = new Intent(this, BonusActivity.class);


                bonusIntent.putExtra(Constants.KEY_1, getBonusDetailsVo);

                startActivity(bonusIntent);


                break;


            case ServiceUrls.RequestNames.GET_TOP_PERFORMERS_DETAILS:

                GetTopPerformersDetails getTopPerformersDetails = Common.getSpecificDataObject(apiResponseVo.data, GetTopPerformersDetails.class);

                Intent topPerformersIntent = new Intent(this, TopPerformersActivity.class);

                topPerformersIntent.putExtra(Constants.KEY_1, getTopPerformersDetails);

                startActivity(topPerformersIntent);

                break;


        }


    }

    private void showChooseReasonForCancelDialog(List<Reason> reasons) {

        /*
        final Dialog dialog = new Dialog(this);

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

                Reason reason = (Reason) ((CustomArrayAdapterReasons) parent.getAdapter()).getItem(position);
                if (reason.reasonId == "-1") {
                    dialog.cancel();
                    showCustomReasonForCancelDialog(reason);
                    return;
                }

                HashMap<String, String> params = new HashMap<String, String>();

                params.put(ServiceUrls.ApiRequestParams.TRIPID, String.valueOf(tripid));
                params.put(ServiceUrls.ApiRequestParams.ORDERID, String.valueOf(orderid));
                params.put(ServiceUrls.ApiRequestParams.REASON_ID, reason.reasonId);
                params.put(ServiceUrls.ApiRequestParams.CUSTOM_REASON, reason.reason);


//                new RetrofitRequester(GoogleMapDrawerActivity.this).sendStringRequest(ServiceUrls.RequestNames.DRIVER_CANCEL_TRIP,params);

                MyService.getInstance().sendSocketRequest(GoogleMapDrawerActivity.this, ServiceUrls.RequestNames.DRIVER_CANCEL_TRIP, params);


            }
        });


        dialog.show();


    }

    private void showCustomReasonForCancelDialog(final Reason reason) {

       /* Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.reasons_for_cancel_other_comment);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
*/

        final Dialog dialog = Common.getAppThemeCustomDialog(this, R.layout.reasons_for_cancel_other_comment, Common.getDimensionResourceValue(R.dimen._35sdp));


        final EditText etReasons = (EditText) dialog.findViewById(R.id.et_reasons);
        Button submit = (Button) dialog.findViewById(R.id.btn_submit);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.root_layout);
        // Common.setupUI(this,rootLayout);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("methodcalling", "methodcalling");
                Common.hideSoftKeyboardFromDialog(dialog, GoogleMapDrawerActivity.this);
                return true;
            }
        });
        //etReasons.setBackgroundResource(R.drawable.rounded_corner_bg);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etReasons.getText().toString().trim().length() < 1) {
                    Common.customToast(GoogleMapDrawerActivity.this, Common.getStringResourceText(R.string.enter_your_reason));
                    return;
                }

                HashMap<String, String> params = new HashMap<String, String>();

                params.put(ServiceUrls.ApiRequestParams.TRIPID, String.valueOf(tripid));
                params.put(ServiceUrls.ApiRequestParams.ORDERID, String.valueOf(orderid));
                params.put(ServiceUrls.ApiRequestParams.REASON_ID, reason.reasonId);
                params.put(ServiceUrls.ApiRequestParams.CUSTOM_REASON, etReasons.getText().toString().trim());

                MyService.getInstance().sendSocketRequest(GoogleMapDrawerActivity.this, ServiceUrls.RequestNames.DRIVER_CANCEL_TRIP, params);


            }
        });


        dialog.show();


    }

    public void callEndTripSocket(String break_down) {
        HashMap<String, String> params = new HashMap();
        params.put(ServiceUrls.ApiRequestParams.TRIPID, String.valueOf(tripid));
        params.put(ServiceUrls.ApiRequestParams.ORDERID, String.valueOf(orderid));
        params.put(ServiceUrls.ApiRequestParams.LATITUDE, String.valueOf(mCurrentLocation.getLatitude()));
        params.put(ServiceUrls.ApiRequestParams.LONGITUDE, String.valueOf(mCurrentLocation.getLongitude()));
        params.put(ServiceUrls.ApiRequestParams.LOCATION, Common.getCompleteAddressString(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        params.put(ServiceUrls.ApiRequestParams.DEVICE, Constants.ANDROID);
        params.put(ServiceUrls.ApiRequestParams.DISTANCE, String.valueOf(totalDistance));
//            params.put(ServiceUrls.ApiRequestParams.DURATION, String.valueOf(totalTime));
        params.put(ServiceUrls.ApiRequestParams.DURATION, String.valueOf(totalTimeWithSec));
        params.put(ServiceUrls.ApiRequestParams.TIP, Constants.TIP_AMOUNT);
        params.put(ServiceUrls.ApiRequestParams.TYPE, String.valueOf(typeid));
        params.put(ServiceUrls.ApiRequestParams.LAST_TRIP, String.valueOf(lastTripValue));
        params.put(ServiceUrls.ApiRequestParams.BREAK_DOWN, break_down);


        MyService.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.END_TRIP, params, true);
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d("onCameraChange", "onCameraChange");
        if (currentZoom) {
            // ZOOM_LEVEL = cameraPosition.zoom;
            Log.d("ZOOM_LEVEL", "ZOOM_LEVEL" + ZOOM_LEVEL);
        }
    }


    @Override
    protected void onResume() {

        //sendSocketInitiationRequest();

        Common.Log.i("onResume() : " + this.getClass().getSimpleName() + " : " + this.hashCode());

//        TATX.getInstance().setSocketResponseListener(this);

        sendSocketInitiationRequest();

        super.onResume();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), Constants.CUSTOM_FONT_PATH);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public static GoogleMapDrawerActivity getInstatnce() {
        return instance;
    }


    public static int makeLastTripValue(Context context) {
        if (Common.getDefaultSP(context).contains(Constants.SharedPreferencesKeys.LAST_TRIP_VALUE)) {
            return Common.getDefaultSP(context).getInt(Constants.SharedPreferencesKeys.LAST_TRIP_VALUE, 0);
        }
        return 0;
    }

    /*  @Override
      public void onSensorChanged(SensorEvent event) {
          Log.d("bearing..1", String.valueOf(event.sensor.getType()));
          if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

              SensorManager.getRotationMatrixFromVector(
                      mRotationMatrix , event.values);
              float[] orientation = new float[3];
              SensorManager.getOrientation(mRotationMatrix, orientation);
              float bearing =(float) Math.toDegrees(orientation[1]) + mDeclination;
              Log.d("bearing..2", String.valueOf(bearing));
              if (String.valueOf(bearing).equalsIgnoreCase("NaN") || String.valueOf(bearing).equalsIgnoreCase("null")){
                  return;
              }
              updateCamera(bearing);
          }
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {

      }
  */
    private void updateCamera(float bearing) {
        if (googleMap != null) {
            CameraPosition oldPos = googleMap.getCameraPosition();

            CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
            //  googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
            CameraUpdate center = CameraUpdateFactory.newCameraPosition(pos);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

//            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//            CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZOOM_LEVEL1);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

        }
    }

    @Override
    public void onUnReadNotificationCountChage(int count) {
        Log.d("onUnReadNotification", "onUnReadNotificationCountChage : " + count);

        // setNotificationIconCountText(count);
        setMenuCounter(R.id.nav_notification, count);

    }

    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
        view.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    private void setNotificationIconCountText(int count) {

        Menu menu = navigationView.getMenu();

        MenuItem navNotification = menu.findItem(R.id.nav_notification);

        if (count <= 0) {
            // navNotification.setIcon(R.drawable.notifications);

        } else {
            View inflatedView = getLayoutInflater().inflate(R.layout.actionbar_badge_layout, null);

            TextView tvUnReadNotificationCount = (TextView) inflatedView.findViewById(R.id.tv_un_read_notification_count);

            tvUnReadNotificationCount.setText(String.valueOf(count));

            Bitmap bitmapFromView = Common.getBitmapFromView(inflatedView);

            navNotification.setIcon(new BitmapDrawable(getResources(), bitmapFromView));

        }


    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }

    private void sendNotification(String msg) {
        Intent resultIntent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

     /*   if (isChecked) {
            onlineOfflineStatus = 1;
            //  Common.customToast(this,"isChecked");
        } else {
            //   Common.customToast(this,"not Checked");
            onlineOfflineStatus = 0;
            fromInvoice = false;
        }
        swOnlineOffline.setChecked(false);
        Common.Log.i("onCheckedChanged..1 "+swOnlineOffline.isChecked());

        if (!fromInvoice) {
            sendSocketRequestToOnSocketOpenService(onlineOfflineStatus);
            Common.Log.i("-? sendSocketRequestToOnSocketOpenService called.");
        }
        fromInvoice = false;

*/

    }

@OnClick (R.id.switch_on_off) void switchOnOff(){


    if (swOnlineOffline.isChecked()) {
        onlineOfflineStatus = 1;
        //  Common.customToast(this,"isChecked");
    } else {
        //   Common.customToast(this,"not Checked");
        onlineOfflineStatus = 0;
        fromInvoice = false;
    }
    Common.Log.i("onCheckedChanged..1 "+swOnlineOffline.isChecked());
    swOnlineOffline.setChecked(!swOnlineOffline.isChecked());

    if (!fromInvoice) {
        sendSocketRequestToOnSocketOpenService(onlineOfflineStatus);
        Common.Log.i("-? sendSocketRequestToOnSocketOpenService called.");
    }
    fromInvoice = false;

}

}
