package com.tatx.userapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
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
import com.google.gson.Gson;
import com.google.maps.model.Bounds;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.PushNotificationListenerActivity;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.customviews.BackAwareEditText;
import com.tatx.userapp.customviews.CustomButton;
import com.tatx.userapp.customviews.CustomTextView;
import com.tatx.userapp.customviews.CustomTypefaceSpan;
import com.tatx.userapp.customviews.CustomerAlertDialog;
import com.tatx.userapp.enums.CabTypes;
import com.tatx.userapp.enums.DensityType;
import com.tatx.userapp.enums.PaymentType;
import com.tatx.userapp.googlemapadapters.Util;
import com.tatx.userapp.helpers.GeoDetails;
import com.tatx.userapp.interfaces.DialogClickListener;
import com.tatx.userapp.interfaces.GoogleMapOnTouchListener;
import com.tatx.userapp.library.AbstractRouting;
import com.tatx.userapp.library.Route;
import com.tatx.userapp.library.RouteException;
import com.tatx.userapp.library.Routing;
import com.tatx.userapp.library.RoutingListener;
import com.tatx.userapp.menuactivity.AboutActivity;
import com.tatx.userapp.menuactivity.AccountActivity;
import com.tatx.userapp.menuactivity.AddLocationActivity;
import com.tatx.userapp.menuactivity.DisplayProfileActivity;
import com.tatx.userapp.menuactivity.HelpActivity;
import com.tatx.userapp.menuactivity.NotificationsActivity;
import com.tatx.userapp.menuactivity.OfferActivity;
import com.tatx.userapp.menuactivity.ShareAndEarn;
import com.tatx.userapp.menuactivity.TripHistoryActivity;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.network.SocketResponseListener;
import com.tatx.userapp.pojos.AccountsCustomerVo;
import com.tatx.userapp.pojos.AirportByCountryVo;
import com.tatx.userapp.pojos.AirportDetail;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.BroadcastLocationVo;
import com.tatx.userapp.pojos.Driver;
import com.tatx.userapp.pojos.FavLocation;
import com.tatx.userapp.pojos.GetCustomerProfileVo;
import com.tatx.userapp.pojos.GetFareVo;
import com.tatx.userapp.pojos.GetSavedLocationVo;
import com.tatx.userapp.pojos.GetLoyalityPointVo;
import com.tatx.userapp.pojos.OnTrip;
import com.tatx.userapp.pojos.OrderInitiatedVo;
import com.tatx.userapp.pojos.PlaceOrderVo;
import com.tatx.userapp.pojos.PromoValidityVo;
import com.tatx.userapp.pojos.PushNotificationResponseVo;
import com.tatx.userapp.pojos.RecentLocation;
import com.tatx.userapp.pojos.SetCustomerLoc;
import com.tatx.userapp.pojos.StateListDrawableWithBitmap;
import com.tatx.userapp.pojos.Type;
import com.tatx.userapp.pojos.UpdateDeviceTokenVo;
import com.tatx.userapp.pojos.UpdatePaymentTypeVo;
import com.tatx.userapp.pojos.UserLogoutVo;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import ademar.phasedseekbar.PhasedListener;
import ademar.phasedseekbar.PhasedOnItemClickListener;
import ademar.phasedseekbar.PhasedSeekBar;
import ademar.phasedseekbar.SimplePhasedAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GoogleMapDrawerActivity extends PushNotificationListenerActivity implements
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        RoutingListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback,
        GoogleMapOnTouchListener,
        SocketResponseListener,
        RetrofitResponseListener,
        View.OnTouchListener, PushNotificationListenerActivity.OnUnReadNotificationCountChangeListener {

    public static final int notifyID = 9001;
    private static final String LOG = GoogleMapDrawerActivity.class.getSimpleName();
    private static final int DEFAULT_MIN_TIME_TO_REACH = 5000;
    private static final String NO_DRIVER = "No";
    private  int PHASED_SEEKBAR_DEFAULT_INDEX = 0;
    public static String currentPoromocode;
    private GoogleMap googleMap = null;
    protected LatLng end;
    private static final String LOG_TAG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
  //  private PlaceAutoCompleteAdapter mAdapter;
    private ArrayList<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark, R.color.primary, R.color.primary_light, R.color.accent, R.color.primary_dark_material_light};
    LocationRequest mLocationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 2;
    private static final float MINIMUM_DISPLACEMENT_IN_METERS = 35;
    double latitude = 0;
    double longitude = 0;
    private DrawerLayout drawer;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    private Location lastknownLocation = null;
    private boolean isMarkerRotating = false;
  //  private int cabType;
    private LatLng destinationLatLngSelected = null;
    private boolean confirmValue = false;
    private String addressSoruce;
    private SlidingUpPanelLayout mLayout;
    private SlidingUpPanelLayout.PanelState stateNew;
    private LatLng sourceLatLngSelected;
    private String sourceAddress = "";
    int currentLeft = 5;
    int currentRight = 5;
    private String drop_address="";
    private String pick_address;
    private double destLat;
    private double destLong;
    private ImageView profilepic;
    private TextView username;
    private TextView userlname;
    private TextView creditpoints;
    private ImageView loyaltiicon;



    private boolean destinationPressed=false;
    private boolean sourcePressed=true;
    private String addressDestination;
    private CameraPosition currentCameraPosition;
    private Dialog selectPaymentTypeDialog;
    private Resources resources;
    private BroadcastLocationVo broadcastLocationVo;
    private boolean previousResponseSuccess=true;
    private CountDownTimer countDownTimer=null;
    private SetCustomerLoc setCustomerLoc;
    private AccountsCustomerVo accountsCustomerVo;
    private int paymentTypeId;
    private MyBroadcastReceiver myBroadcastReceiver;
    private int[] cabCatagoriesId=null;
    private int cabTypeId=1;
    public static GoogleMapDrawerActivity instance;
    private LinearLayout llMarker;
    private LinearLayout llTimeMin;
    private ImageView ivFilledCircle;
    private ImageView ivLoaderGif;
    private ImageView ivTvTimeBg;
    private TextView tvMin;
    private ImageView ivLoaderFlash;
    private GetSavedLocationVo getSavedLocationVo;
    private BitmapDrawable categoryCarDrawable;
    private String countryNameGeo=null;
    private AirportByCountryVo airportByCountryVo;
    private double previousLat=0.0;
    private double previousLng=0.0;
    private boolean isKeyboardOpen=false;
    private boolean onConnectedCall=false;
    private boolean isAirPortBoundary=false;
    private Marker sourceMapMarker;
    private Marker destinationMapMarker;
    private View[] editableViews ;
    private CustomerAlertDialog customerAlertDialog;
    private Bitmap markerCategoryCarDrawable;
    private Bitmap bitmapImg;
    private StateListDrawableWithBitmap stateListDrawableWithBitmap;
    private Bitmap[] markerBitmapImgs;
//    public Bundle savedInstanceState;

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(MINIMUM_DISPLACEMENT_IN_METERS);
    }



    @BindView(R.id.btn_promo_code) CustomButton btnPromoCode;

    @BindView(R.id.tv_estimate_time) TextView tvEstimateTime;

    @BindView(R.id.tv_time) TextView tvTime;

    @BindView(R.id.iv_picker_icon) ImageView ivPickerIcon;

    @BindView(R.id.tv_source_selected) BackAwareEditText tvSourceSelected;

    @BindView(R.id.tv_destination_selected)public BackAwareEditText tvDestinationSelected;

   // @BindView(R.id.multicabs) ImageView multi;

    @BindView(R.id.my_current_location) ImageButton myCurrentLocation;

    @BindView(R.id.order_button) Button btnOrder;

    @BindView(R.id.navigationClickRelativeL) RelativeLayout navigationClickRelativeL;
    @BindView(R.id.navigation) ImageView navigationMenuIcon;

    @BindView(R.id.dragView1) LinearLayout relativeLayoutSeekbar;

    @BindView(R.id.order_layout) RelativeLayout orderLayout;

    @BindView(R.id.enroute_layout) LinearLayout coifirmatonlinearLayout;

    @BindView(R.id.farestimte) TextView farestimate;

    @BindView(R.id.btn_cash) Button btncash;

    @BindView(R.id.imageButton) ImageView imageButton;

    @BindView(R.id.searchlayout) RelativeLayout searchlayout;

    @BindView(R.id.tv_source_address_to_show) TextView sourceText;

    @BindView(R.id.tv_destination_address_to_show) TextView destinationText;

    @BindView(R.id.tv_cab_capacity) TextView tvCabCapacity;

    @BindView(R.id.tv_sar_per_km) TextView tvSarPerKm;

    @BindView(R.id.tv_min_fare) TextView tvMinFare;

    @BindView(R.id.tv_base_fare) TextView tvBaseFare;

    @BindView(R.id.tv_price_per_min) TextView tvPricePerMin;

    @BindView(R.id.cab_name) TextView cab_name;

    @BindView(R.id.psb_hor) PhasedSeekBar psbHorizontal;

    @BindView(R.id.btn_confirm_order) Button btnConfirmOrder;

    @BindView(R.id.bottomll) RelativeLayout hideRelativeLayout;

    @BindView(R.id.clear_source) RelativeLayout clearSource;

    @BindView(R.id.clear_destination) RelativeLayout clearDestination;

    @BindView(R.id.ll_category_names) LinearLayout llCategoryNames;

    @BindView(R.id.relSeekbarLayout) RelativeLayout relSeekbarLayout;

    @BindView(R.id.faredetail_layout) RelativeLayout fareDetailLayout ;

    @BindView(R.id.ll_drag_view_main) LinearLayout llDragViewMain;
    @BindView(R.id.custom_animate_marker) View customAnimateMarker;
    @BindView(R.id.nav_view)  NavigationView navigationView;

   // @BindView(R.id.tv_recent_location)  TextView tvRecentLocation;
    @BindView(R.id.coordinator_ll) CoordinatorLayout coordinator_ll;
    @BindView(R.id.v_marker_bottom_line) View vMarkerBottomLine;


    @BindView(R.id.rv_google_search)
    RecyclerView mRecyclerView;
    @BindView(R.id.recyclerview_saved_locatons)  RecyclerView recyclerviewSavedLocatons;
    @BindView(R.id.ll_suggtion_view)  LinearLayout llSuggtionView;

    @BindView(R.id.recyclerView1)
    RecyclerView rvGoogleSearchDestination;
    @BindView(R.id.recyclerview_saved_locatons1)  RecyclerView rvSavedLocatonsDestination;
    @BindView(R.id.ll_suggtion_view1)  LinearLayout llSuggtionViewDestination;


    @BindView(R.id.tv_normal)  TextView tvNormal;
    @BindView(R.id.tv_satellite)  TextView tvSatellite;
    @BindView(R.id.fl_map_view_tpye) FrameLayout flMapViewTpye;

    private ArrayList<FavLocation> favLocationArrayList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        Fabric.with(this, new Crashlytics());

        instance = this;

        logUser();

        setContentView(R.layout.activity_googlemap_drawer);

        ButterKnife.bind(this);

        setDisableHideKeyBoard(true);

        if (!ServiceUrls.CURRENT_ENVIRONMENT.getEnvironmentType().equals("PRODUCTION ENVIRONMENT"))
        {
            Common.customToast(this, ServiceUrls.CURRENT_ENVIRONMENT.getEnvironmentType());
            Common.customToast(this, DensityType.getEnumField(getResources().getDisplayMetrics().density).getDensityString());

        }


        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());


        navigationClickRelativeL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else
                {
                    Common.hideSoftKeyboard(GoogleMapDrawerActivity.this);


                    drawer.openDrawer(GravityCompat.START);
                }
                return false;
            }
        });

        navigationMenuIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else
                {
                    Common.hideSoftKeyboard(GoogleMapDrawerActivity.this);


                    drawer.openDrawer(GravityCompat.START);
                }
                return false;
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);

        profilepic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profilepic);
        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        userlname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userlname);
        creditpoints = (TextView) navigationView.getHeaderView(0).findViewById(R.id.creditpoints);
        loyaltiicon = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.loyaltiicon);


        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }


        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(GoogleMapDrawerActivity.this,DisplayProfileActivity.class);
                startActivity(intent);

            }
        });
        navigationView.setNavigationItemSelectedListener(this);

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

        displayGoogleLocationSettingPage(this);


        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ServiceUrls.RequestParams.ROLE,Constants.CUSTOMER);
        params.put(ServiceUrls.RequestParams.ONLINE,"1");
        params.put(ServiceUrls.RequestParams.OS,Constants.ANDROID);

        TATX.getInstance().sendSocketRequest(this, ServiceUrls.RequestNames.ON_SOCKET_OPEN,params);
        initializedAll();

        sildingUpPanel();

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_CUSTOMER_PROFILE,null);


        //Careem Marker Start.


        RelativeLayout rlParent = (RelativeLayout) findViewById(R.id.rl_parent);
        llMarker = (LinearLayout) findViewById(R.id.ll_marker);
        llTimeMin = (LinearLayout) findViewById(R.id.ll_time_min);
        ivFilledCircle = (ImageView) findViewById(R.id.iv_filled_circle);
        ivLoaderGif = (ImageView) findViewById(R.id.iv_loader_gif);
        ivTvTimeBg = (ImageView) findViewById(R.id.iv_tv_time_bg);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvMin = (TextView) findViewById(R.id.tv_min);
        ivLoaderFlash = (ImageView) findViewById(R.id.iv_loader_flash);



        setAnimatedMarkerColor(R.color.button_color);


        setUnReadNotificationCountChangeListener(this);



    }

    private void setNotificationIconCountText(int count)
    {

        Menu menu = navigationView.getMenu();

        MenuItem navNotification = menu.findItem(R.id.nav_notification);

        if(count <=0)
        {
            navNotification.setIcon(R.drawable.notifications);

        }
        else
        {
            View inflatedView = getLayoutInflater().inflate(R.layout.actionbar_badge_layout, null);

            TextView tvUnReadNotificationCount = (TextView) inflatedView.findViewById(R.id.tv_un_read_notification_count);

            tvUnReadNotificationCount.setText(String.valueOf(count));

            Bitmap bitmapFromView = Common.getBitmapFromView(inflatedView);

            navNotification.setIcon(new BitmapDrawable(getResources(),bitmapFromView));

        }


    }

    @Override
    protected void onPushNotificationReceived(PushNotificationResponseVo pushNotificationResponseVo)
    {


        Common.Log.i("Inside CancelOrderActivity - onPushNotificationReceived().");

        Common.Log.i("pushNotificationResponseVo.toString() : "+pushNotificationResponseVo.toString());

        switch (pushNotificationResponseVo.code)
        {


            case 20000:


                OnTrip onTrip = Common.getSpecificDataObject(pushNotificationResponseVo.data, OnTrip.class);

                Intent intent = new Intent(this, OnTripMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("action", 2);
                /*intent.putExtra("source", sourceAdd);
                intent.putExtra("destination", destinationAdd);*/
                intent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS, Common.getCompleteAddressString(this,onTrip.pickLatitude,onTrip.pickLongitude).getCompleteAddress());
                intent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS, Common.getCompleteAddressString(this,onTrip.dropLatitude,onTrip.dropLongitude).getCompleteAddress());
                intent.putExtra(Constants.KEY_1, onTrip);

                finish();
                startActivity(intent);



                break;
            case ServiceUrls.PushResponseCodes._20007:
                logoutApi(1);
                break;
            case ServiceUrls.PushResponseCodes.FROM_NOTIFICATION_MANAGEMENT_20008:
                sendNotification(pushNotificationResponseVo.message);
                break;



        }



    }





    @Override
    protected void onStart()
    {
        super.onStart();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    public void initializedAll()
    {



        tvSourceSelected.setBackPressedListener(new BackAwareEditText.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareEditText editText) {
                isKeyboardOpen = false;
            }

        });
        tvDestinationSelected.setBackPressedListener(new BackAwareEditText.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareEditText editText) {
                isKeyboardOpen = false;
            }

        });
        String promoImage = getIntent().getStringExtra(Constants.IntentKeys.PROMO_IMG);

        Common.Log.i("?? - promoImage : "+promoImage);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String thisDate = dateFormat.format(new Date());
        Log.d("date_today","date_today"+thisDate);
        if (promoImage!=null)
        {

            if ((Common.getDefaultSP(this).contains(Constants.SharedPreferencesKeys.SHOW_OFFER_SCREEN) && !Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.SHOW_OFFER_SCREEN, "").equalsIgnoreCase(thisDate)) || !Common.getDefaultSP(this).contains(Constants.SharedPreferencesKeys.SHOW_OFFER_SCREEN))
            {
                showOfferDialog(promoImage);

                Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.SHOW_OFFER_SCREEN, thisDate).commit();

                Log.d("date_today", "date_today  " + Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.SHOW_OFFER_SCREEN, ""));

            }



        }

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        editor = sp.edit();

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_LOYALITY_POINT,null,Constants.RequestCodes.ONCREATE_REQUEST_CODE);


        tvSourceSelected.setCursorVisible(false);
        tvDestinationSelected.setCursorVisible(false);
        btncash.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        farestimate.setOnClickListener(this);
        myCurrentLocation.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        btnConfirmOrder.setOnClickListener(this);

      //  multi.setOnClickListener(this);
        btnConfirmOrder.setOnClickListener(this);
        tvSourceSelected.setOnClickListener(this);
        tvDestinationSelected.setOnClickListener(this);
        //tvSourceSelected.requestFocus();
        clearSource.setOnTouchListener(this);
        clearDestination.setOnTouchListener(this);

        tvSourceSelected.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Common.hideSoftKeyboard(GoogleMapDrawerActivity.this);
                Log.d("onFocusChange A", "" + hasFocus);
                if (hasFocus) {

                    Common.setViewsEnableStatuss(true,tvSourceSelected);
                    destinationPressed = false;
                    sourcePressed = true;
                    tvSourceSelected.setBackgroundDrawable(getResources().getDrawable(R.drawable.google_search_background_selected));
                    tvDestinationSelected.setBackgroundDrawable(getResources().getDrawable(R.drawable.google_search_background));
                    tvDestinationSelected.setTextColor(getResources().getColor(R.color.divider));
                    tvSourceSelected.setTextColor(getResources().getColor(R.color.black));

//                    vMarkerBottomLine.setBackgroundColor(Common.getColorFromResource(R.color.button_color));


                    setAnimatedMarkerColor(R.color.button_color);


                    tvEstimateTime.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.VISIBLE);
                    Location location=new Location("");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    myCurrentLocationCamera(location);
                    //suggetionSetAdapter(tvSourceSelected.getText().length());
                    Log.d("suggetionSetAdapter", "suggetionSetAdapter 747");

                    removeSourceMapMarker();

                    addDestinationMapMarker();

                    moveCameraToPosition(sourceLatLngSelected);





                } else{
                    sourcePressed = false;
//                    Common.setViewsEnableStatuss(false,tvDestinationSelected);

            }

            }
        });
        tvDestinationSelected.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("onFocusChange B",""+hasFocus);
                Common.hideSoftKeyboard(GoogleMapDrawerActivity.this);
                if (hasFocus)
                {
                    clearDestination.setVisibility(View.VISIBLE);

                    Common.setViewsEnableStatuss(true,tvDestinationSelected);
                    sourcePressed = false;
                    destinationPressed = true;
                    tvDestinationSelected.setBackgroundDrawable(getResources().getDrawable(R.drawable.google_search_background_selected));
                    tvSourceSelected.setBackgroundDrawable(getResources().getDrawable(R.drawable.google_search_background));
                    tvSourceSelected.setTextColor(getResources().getColor(R.color.divider));
                    tvDestinationSelected.setTextColor(getResources().getColor(R.color.black));

                    setAnimatedMarkerColor(R.color.black);



                    tvEstimateTime.setVisibility(View.GONE);
                    tvTime.setVisibility(View.VISIBLE);

//                    tvDestinationSelected.setText("");
                    //suggetionSetAdapter(tvDestinationSelected.getText().length());
                    Log.d("suggetionSetAdapter", "suggetionSetAdapter 774");

                    removeDestinationMapMarker();

                    addSourceMapMarker();

                    moveCameraToPosition(destinationLatLngSelected);




                }
                else
                {
//                    Common.setViewsEnableStatuss(false,tvSourceSelected);
                    destinationPressed = false;

                }


            }
        });

        confirmationLayout(confirmValue);
        resources = getResources();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter_update = new IntentFilter(Constants.ACTION_MyUpdate);
        intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter_update);
    }

    private void setAnimatedMarkerColor(int colorResourceId)
    {

        tvTime.setText("");

        GradientDrawable ivLoaderGifGradient = (GradientDrawable)ivLoaderGif.getBackground();
        ivLoaderGifGradient.setColor(Common.getColorFromResource(colorResourceId));

        GradientDrawable ivFilledCircleGradient = (GradientDrawable)ivFilledCircle.getBackground();
        ivFilledCircleGradient.setColor(Common.getColorFromResource(colorResourceId));

        GradientDrawable ivTvTimeBgGradient = (GradientDrawable)ivTvTimeBg.getBackground();
        ivTvTimeBgGradient.setColor(Common.getColorFromResource(colorResourceId));

        vMarkerBottomLine.setBackgroundColor(Common.getColorFromResource(colorResourceId));


    }


    public void sildingUpPanel() {

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(LOG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(LOG, "onPanelStateChanged " + newState);
                Common.Log.i("onGoogleMapTouch - Down."+"setFadeOnClickListener"+ newState);
                stateNew = newState;

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "onPanelStateChanged, offset " + "setFadeOnClickListener");
                Common.Log.i("onGoogleMapTouch - Down."+"setFadeOnClickListener");
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            }
        });

        countDownTimer = new CountDownTimer(2000, 1000)
        {

            public void onTick(long millisUntilFinished)
            {
                // tvTimeSec.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (sourcePressed)
                            callCameraChangeListner(currentCameraPosition);
                        else if (destinationPressed) {
                            selectDestinationByMapMove(currentCameraPosition);
                        }
                    }
                });



            }

        };
    }



    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (confirmValue)
        {

            confirmValue = false;

            confirmationLayout(confirmValue);

        } else if (String.valueOf(stateNew).equalsIgnoreCase("EXPANDED")) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else  if (llSuggtionView.getVisibility()==View.VISIBLE || llSuggtionViewDestination.getVisibility()==View.VISIBLE){
            llSuggtionView.setVisibility(View.GONE);
            llSuggtionViewDestination.setVisibility(View.GONE);

            return;

        }else {
            onBackPressed2();
        }
    }



    public void onBackPressed2()
    {


       showExitConfirmationDialog(false);



    }

    private void showExitConfirmationDialog(final boolean logoutStatus)
    {

        String message = logoutStatus ? Common.getStringResourceText(R.string.are_you_sure_you_want_to_logout) : Common.getStringResourceText(R.string.do_you_want_to_exit);
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton( Common.getStringResourceText(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(logoutStatus)
                        {
                            logoutApi(0);
                        }
                        else
                        {
                            finish();
                        }
                    }
                })
                .setNegativeButton(Common.getStringResourceText(R.string.no),null).create().show();
    }


    public void callCameraChangeListner(final CameraPosition position) {
        new Handler().post(new Runnable() {
            public GeoDetails geoDetails;

            @Override
            public void run() {
                Log.d(LOG, "onCameraChange..socket...");
                if (previousResponseSuccess) {
                    previousResponseSuccess = false;
                    LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
//                    mAutoCompleteAdapter.setBounds(bounds);

                    if (!confirmValue) {
                        latitude = position.target.latitude;
                        longitude = position.target.longitude;
                    }

                    sourceLatLngSelected = new LatLng(latitude, longitude);

                    geoDetails = Common.getCompleteAddressString(GoogleMapDrawerActivity.this, latitude, longitude);
                    addressSoruce = geoDetails.getCompleteAddress();
                    tvSourceSelected.setText(addressSoruce);


                        addSourceMapMarker();



                    Log.d("Anil11111", "onCameraChange..socket...");
                   // tvSourceSelected.performCompletion();
                    sourceAddress = geoDetails.getAddress();

                    sendSocketRequestToSetCustomerLocationService();

                 //   airPortFunctionality(geoDetails);


                }


            }
        });

    }

    private void addSourceMapMarker() {


        removeSourceMapMarker();

        //


        if (!sourcePressed && !TextUtils.isEmpty(tvSourceSelected.getText()) && sourceLatLngSelected!=null)
        {
            sourceMapMarker = googleMap.addMarker(new MarkerOptions().position(sourceLatLngSelected));

            View inflatedView = getLayoutInflater().inflate(R.layout.marker_custom_view, null);

            CustomTextView tvEstimateCabReachTime = (CustomTextView) inflatedView.findViewById(R.id.tv_estimate__cab_reach_time);

            tvEstimateCabReachTime.setText("S");

            sourceMapMarker.setIcon(BitmapDescriptorFactory.fromBitmap(Common.getBitmapFromView(inflatedView)));



        }


        //
    }



    private void addDestinationMapMarker()
    {

        removeDestinationMapMarker();


        //




        if (!destinationPressed && !TextUtils.isEmpty(tvDestinationSelected.getText()) && destinationLatLngSelected != null)
        {

            destinationMapMarker = googleMap.addMarker(new MarkerOptions().position(destinationLatLngSelected));

            View inflatedView = getLayoutInflater().inflate(R.layout.marker_custom_view_black, null);

            destinationMapMarker.setIcon(BitmapDescriptorFactory.fromBitmap(Common.getBitmapFromView(inflatedView)));

        }


        //

    }



    private void sendSocketRequestToSetCustomerLocationService()
    {



        Common.Log.i("? - Inside sendSocketRequestToSetCustomerLocationService().");

        HashMap<String, String> params = new HashMap();

        params.put(ServiceUrls.RequestParams.LATITUDE, String.valueOf(latitude));

        params.put(ServiceUrls.RequestParams.LONGITUDE, String.valueOf(longitude));

        params.put(ServiceUrls.RequestParams.CITY, sourceAddress);

        TATX.getInstance().sendSocketRequest(GoogleMapDrawerActivity.this,ServiceUrls.RequestNames.SET_CUSTOMER_LOC,params,false);


    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {

        int id = item.getItemId();



        switch (id)
        {

            case R.id.nav_profile:
                Intent intent = new Intent(this, DisplayProfileActivity.class);
               // Intent intent = new Intent(this, SuggetionClass.class);
                startActivity(intent);
                break;

            case R.id.nav_accout:
                Intent intent1 = new Intent(this, AccountActivity.class);
                startActivity(intent1);
                break;

            case R.id.nav_trip_istory:
                Intent intent2 = new Intent(this, TripHistoryActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_notification:
                //Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(this, NotificationsActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_offers:
               // Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(this, OfferActivity.class);
                startActivity(intent4);
                break;

            case R.id.nav_loc:
                Intent intentLocation = new Intent(this, AddLocationActivity.class);
                startActivityForResult(intentLocation,Constants.RESULT_FROM_LOCATIONS);
                break;

            case R.id.nav_share:
                //Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(this, ShareAndEarn.class);
                startActivity(intent5);
                break;

            case R.id.nav_help:
               // Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent(this, HelpActivity.class);
                startActivity(intent6);
                break;

            case R.id.nav_about:
                //Toast.makeText(this, item.getTitle() + " section is under development", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(this, AboutActivity.class);
                startActivity(intent7);
                break;

            case R.id.nav_logout:

//                logoutApi();
                showExitConfirmationDialog(true);


                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logoutApi(int logoutDueToPush)
    {

        HashMap<String, String> requestParams = new HashMap();
        requestParams.put(ServiceUrls.RequestParams.DEVICE, Constants.ANDROID);
        requestParams.put(ServiceUrls.RequestParams.DEVICE_ID,Common.getDeviceId(this));
        requestParams.put(ServiceUrls.RequestParams.LOGOUT_DUE_TO_PUSH, String.valueOf(logoutDueToPush));


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.USER_LOGOUT,requestParams);

    }

    public void route(LatLng start, LatLng end) {
        if (start == null || end == null) {
            if (start == null) {
                if (sourceText.getText().length() > 0) {
                    sourceText.setError(Common.getStringResourceText(R.string.please_choose_source));
                } else {
                    Toast.makeText(this, Common.getStringResourceText(R.string.please_choose_source), Toast.LENGTH_SHORT).show();
                }
            }
            if (end == null) {
                if (tvDestinationSelected.getText().length() > 0) {
                    tvDestinationSelected.setError(Common.getStringResourceText(R.string.please_choose_destination));
                } else {
                    Toast.makeText(this,  Common.getStringResourceText(R.string.please_choose_destination), Toast.LENGTH_SHORT).show();
                }
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
        progressDialog.dismiss();
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,  Common.getStringResourceText(R.string.something_went_wrong_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        progressDialog.dismiss();

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        polylines = new ArrayList<>();

        int dist = route.get(0).getDistanceValue();
        int pos=0;

        for (int i = 1; i < route.size(); i++) {
            if (dist > route.get(i).getDistanceValue()) {
                dist = route.get(i).getDistanceValue();
                pos=i;
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
            for (int j = 0; j < route.get(pos).getPoints().size(); j++){
                builder.include(route.get(pos).getPoints().get(j));
                j=j+5;
        }
          //  builder.include(destinationLatLngSelected);


            LatLngBounds bounds = builder.build();
            int padding = 100; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cu);

            //Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();


        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(sourceLatLngSelected);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin));
        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(destinationLatLngSelected);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.blackpin));
        googleMap.addMarker(options);


    }

    @Override
    public void onRoutingCancelled() {
        Log.i(LOG_TAG, "Routing was cancelled.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.v(LOG_TAG, connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        progressDialog = ProgressDialog.show(this, "",Common.getStringResourceText(R.string.please_wait), false);
        onConnectedCall=true;
        Common.Log.i("onConnected");


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location)
                {

//                    updateCameraBearing(googleMap,location.getBearing());



                    if (location!=null)
                    {
                        lastknownLocation=location;
                        if (onConnectedCall)
                        {

                            onConnectedCall=false;

                            sourceLatLngSelected = new LatLng(lastknownLocation.getLatitude(), lastknownLocation.getLongitude());

                            CameraUpdate center = CameraUpdateFactory.newLatLng(sourceLatLngSelected);

                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);

                            sourceAddress = Common.getAddressString(getApplicationContext(), sourceLatLngSelected.latitude,sourceLatLngSelected.longitude);

                            googleMap.moveCamera(center);

                            googleMap.animateCamera(zoom);

                            Common.Log.i("address : "+ sourceAddress);

                            Common.Log.i("onConnected.");

                            Common.Log.i("sourceLatLngSelected : "+sourceLatLngSelected);

                            Common.getCityBoundaries(GoogleMapDrawerActivity.this,sourceLatLngSelected);

                            callCameraChangeListner(googleMap.getCameraPosition());

                            if (progressDialog != null)
                            {
                                progressDialog.dismiss();
                            }


                        }


                    }



                }
            });



    }






    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

     //   googleMap.getUiSettings().setRotateGesturesEnabled(false);
        //googleMap.setMyLocationEnabled(true);

        if (googleMap != null) {
            Log.d(LOG, "onMapReady...");

            float currentBottomF = getResources().getDimension(R.dimen._130sdp);
            googleMap.setPadding(currentLeft, Math.round(currentBottomF), currentRight, Math.round(currentBottomF));

            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    currentCameraPosition=cameraPosition;

                    Log.d("sourcePressed", "sourcePressed " + sourcePressed + " destinationPressed " + destinationPressed);

                        Common.Log.i("Inside onCameraChange().");

                        Common.Log.i("Camera LatLng : " + cameraPosition.target.latitude + "," + cameraPosition.target.longitude);
                        Location location = new Location("");
                        location.setLatitude(cameraPosition.target.latitude);
                        location.setLongitude(cameraPosition.target.longitude);
                    if (lastknownLocation != null) {
                        float distance = location.distanceTo(lastknownLocation);
                        if (Math.round(distance) < 30) {
                            myCurrentLocation.setVisibility(View.GONE);
                        } else {
                            myCurrentLocation.setVisibility(View.VISIBLE);
                        }
                    }


            }

            });


        }

    }

    public void myCurrentLocationCamera(Location location) {
        Log.d("lastknownLocation",location.toString());
        if (location != null&&googleMap!=null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            Log.d("lastknownLocation",location.toString()+" "+googleMap.getCameraPosition());
            if (sourcePressed)
                callCameraChangeListner(googleMap.getCameraPosition());
            else if (destinationPressed) {
                selectDestinationByMapMove(googleMap.getCameraPosition());
            }
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
                // final LocationSettingsStates s= result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //Toast.makeText(activity, "success...", Toast.LENGTH_SHORT).show();
                   /*      Location location = lastknownLocation;
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            String address = Common.getCompleteAddressString(GoogleMapDrawerActivity.this, latitude, longitude).getCompleteAddress();
                           // tvSourceSelected.setText(address);

                            LatLng lng = new LatLng(latitude, longitude);
                            startLntLng = lng;
                            Log.d(LOG, String.valueOf(location.getLatitude()) + address);
                            Log.d(LOG, "onCameraChange 1366" + latitude + "   " + longitude);
                        }*/
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
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        Log.d(LOG, "OnresultAxtivity1");
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:


                        Log.d(LOG, "OnresultAxtivity4");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        finish();

                        break;

                    default:
                        break;
                }
                break;



            case Constants.RESULT_FROM_LOCATIONS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_SAVED_LOCATIONS,null);
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                      //  finish();
                        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_SAVED_LOCATIONS,null);
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
            case R.id.tv_source_selected:
                isKeyboardOpen = true;
                Log.d("suggetionSetAdapter", "suggetionSetAdapter 1832");
                llSuggtionViewDestination.setVisibility(View.GONE);
                llSuggtionView.setVisibility(View.VISIBLE);

                Common.setSuggetions(this,mGoogleApiClient,recyclerviewSavedLocatons,mRecyclerView,favLocationArrayList,tvSourceSelected);


                break;
            case R.id.tv_destination_selected:
                isKeyboardOpen = true;
                Log.d("suggetionSetAdapter", "suggetionSetAdapter 1838");
                llSuggtionView.setVisibility(View.GONE);
                llSuggtionViewDestination.setVisibility(View.VISIBLE);

                Common.setDestinationSuggetions(this,mGoogleApiClient,rvSavedLocatonsDestination,rvGoogleSearchDestination,favLocationArrayList,tvDestinationSelected);



                break;
            case R.id.my_current_location:
                if (lastknownLocation != null) {
                    myCurrentLocationCamera(lastknownLocation);
                }
                break;
            case R.id.order_button:
/*

                if (true)
                {
                    throw new RuntimeException("New Exception Raised from User App.");
                }
*/ Log.d("GeoDetails anil"," latitude "+latitude+" longitude "+longitude);
                 Common.hideSoftKeyboard(this);
                GeoDetails geoDetails = Common.getCompleteAddressString(GoogleMapDrawerActivity.this, latitude,longitude);
                Log.d("GeoDetails anil",geoDetails.toString()+" latitude "+latitude+" longitude "+longitude);


                if(TextUtils.isEmpty(tvSourceSelected.getText()))

                {
                    Common.customToast(this,Common.getStringResourceText(R.string.please_choose_source));

                    return;

                }

                String estimateTimeText = minTimeToReach == DEFAULT_MIN_TIME_TO_REACH ? NO_DRIVER : minTimeToReach + "\nMin";


                if (!estimateTimeText.equals(NO_DRIVER))
                {
                    airPortFunctionality(geoDetails);

//                    getAccountDetailsApi();

                    new RetrofitRequester(GoogleMapDrawerActivity.this).sendStringRequest(ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER,null,R.id.order_button);

                    if (currentPoromocode != null)
                    {
                        btnPromoCode.setText(currentPoromocode);
//                        btnPromoCode.setClickable(false);
                    }


                    if (tvSourceSelected.getText().toString().length() > 3 && tvDestinationSelected.getText().toString().length() > 3)
                    {


                        Common.Log.i("sourceLatLngSelected 2: "+sourceLatLngSelected);

                        Bounds sourceBounds =  Common.getCityBoundaries(GoogleMapDrawerActivity.this,sourceLatLngSelected);

                        Common.Log.i("destinationLatLngSelected : "+destinationLatLngSelected);
                        Bounds destinationBounds = Common.getCityBoundaries(GoogleMapDrawerActivity.this,destinationLatLngSelected);

                        Common.Log.i("? - sourceBounds : "+sourceBounds);
                        Common.Log.i("? - destinationBounds : "+destinationBounds);

                        Common.Log.i("????? - sourceBounds : "+sourceBounds);

                        Common.Log.i("sourceBounds.northeast : "+sourceBounds.northeast);

                        if (destinationBounds==null)
                        {
                            Common.customToast(this,Common.getStringResourceText(R.string.invalid_destination));
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
                        Common.Log.i("northEarstDistance : "+ northEastDistance);

                        float southWestDistance = sourceSouthWestLocation.distanceTo(destinationSouthWestLocation);
                        Common.Log.i("southWestDistance : "+southWestDistance);

                        LatLngBounds sourceLatLngBounds = new LatLngBounds(new LatLng(sourceSouthWestLocation.getLatitude(),sourceSouthWestLocation.getLongitude()),new LatLng(sourceNorthEastLocation.getLatitude(),sourceNorthEastLocation.getLongitude()));

                        boolean inSideBoundary = sourceLatLngBounds.contains(destinationLatLngSelected);

                        Common.Log.i("sourceLatLngBounds.contains(destinationLatLngSelected) : "+sourceLatLngBounds.contains(destinationLatLngSelected));
                        Common.Log.i("sourceNorthEastLocation.toString() : "+sourceNorthEastLocation.toString());
                        Common.Log.i("sourceSouthWestLocation.toString() : "+sourceSouthWestLocation.toString());
                        Common.Log.i("destinationNorthEastLocation.toString() : "+destinationNorthEastLocation.toString());
                        Common.Log.i("destinationSouthWestLocation.toString() :  "+destinationSouthWestLocation.toString());


                        if(southWestDistance <=20000 || northEastDistance <= 20000 || inSideBoundary)
                        {

                            googleMap.clear();
                            if (sourceLatLngSelected != null && destinationLatLngSelected != null)
                            {
                                Common.Log.i("sourceLatLngSelected 2:route new"+sourceLatLngSelected);

                                Common.Log.i("destinationLatLngSelected :route new"+destinationLatLngSelected);
                               // googleMap.setPadding(0,0,0, (int) getResources().getDimension(R.dimen.sp100));
                                route(sourceLatLngSelected, destinationLatLngSelected);

                            }

                            confirmValue = true;

                            confirmationLayout(confirmValue);
                        }
                        else
                        {
                            Common.customToast(this, Common.getStringResourceText(R.string.the_destination_entered_is_outside_our_coverage_area), Common.TOAST_TIME);
                            return;

                        }
                    }
                    else
                    {

                        resetCabsData();

                        if (sourceLatLngSelected != null && destinationLatLngSelected != null) {
                            Common.Log.i("sourceLatLngSelected 2:route else "+sourceLatLngSelected);

                            Common.Log.i("destinationLatLngSelected :route else"+destinationLatLngSelected);

                            route(sourceLatLngSelected, destinationLatLngSelected);
                        }

                        confirmValue = true;
                        confirmationLayout(confirmValue);
                    }

                } else {
                    Common.customToast(this, Common.getStringResourceText(R.string.No_cab_found_please_try_after_some_time), Common.TOAST_TIME);
                }
//                btnOrder.setEnabled(true);
                break;

          /*  case R.id.multicabs:
                Common.customToast(this, "Comming soon...", Common.TOAST_TIME);
                break;*/

            case R.id.iv_picker_icon:
                confirmValue = true;
                confirmationLayout(confirmValue);
                break;

            case R.id.farestimte:
//                farestimate.setEnabled(false);
                if (destinationText.getText().toString().length() > 3)
                {
                    String cvId = null;
                    for (int i = 0; i < setCustomerLoc.types.size(); i++) {
                        if (setCustomerLoc.types.get(i).typeId.equalsIgnoreCase(String.valueOf(cabTypeId))) {

                            cvId = setCustomerLoc.types.get(i).vcId;
                        }
                    }

                    HashMap<String, String> requestparams = new HashMap<>();

//                    requestparams.put(ServiceUrls.RequestParams.TYPE, String.valueOf(broadcastLocationVo.types.get(cabType).typeId));
                    requestparams.put(ServiceUrls.RequestParams.VC_ID, cvId);
                    requestparams.put(ServiceUrls.RequestParams.PICK_LATITUDE, String.valueOf(sourceLatLngSelected.latitude));
                    requestparams.put(ServiceUrls.RequestParams.PICK_LONGITUDE,String.valueOf(sourceLatLngSelected.longitude));
                    requestparams.put(ServiceUrls.RequestParams.DROP_LATITUDE,String.valueOf(destinationLatLngSelected.latitude));
                    requestparams.put(ServiceUrls.RequestParams.DROP_LONGITUDE,String.valueOf(destinationLatLngSelected.longitude));


                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_FARE,requestparams);




                }
                else
                {
                    Common.customToast(this, Common.getStringResourceText(R.string.please_select_destination), Common.TOAST_TIME);

                }
//                farestimate.setEnabled(true);
                break;

            case R.id.btn_cash:
                showPaymentOptionPopup();
                break;

            case R.id.btn_confirm_order:

                if (destinationLatLngSelected != null) {
                    destLat = destinationLatLngSelected.latitude;
                    destLong = destinationLatLngSelected.longitude;
                    drop_address = Common.getCompleteAddressString(this, destLat, destLong).getCompleteAddress();
                } else {
                    destLat = 0;
                    destLong = 0;

                }

                if (String.valueOf(latitude).length()>3&&String.valueOf(longitude).length()>3) {
                    pick_address = Common.getCompleteAddressString(this, latitude, longitude).getCompleteAddress();
                }

                String cvId = null;
                for (int i = 0; i < setCustomerLoc.types.size(); i++) {
                    if (setCustomerLoc.types.get(i).typeId.equalsIgnoreCase(String.valueOf(cabTypeId))) {

                        cvId = setCustomerLoc.types.get(i).vcId;
                    }
                    }

                HashMap<String, String> params = new HashMap();
                params.put(ServiceUrls.RequestParams.PICK_LATITUDE, String.valueOf(latitude));
                params.put(ServiceUrls.RequestParams.PICK_LONGITUDE, String.valueOf(longitude));
                params.put(ServiceUrls.RequestParams.DROP_LATITUDE, String.valueOf(destLat));
                params.put(ServiceUrls.RequestParams.DROP_LONGITUDE, String.valueOf(destLong));
//                params.put(ServiceUrls.RequestParams.TYPE, String.valueOf(cabType));
                params.put(ServiceUrls.RequestParams.TYPE, String.valueOf(cabTypeId));
                params.put(ServiceUrls.RequestParams.VC_ID,cvId);
//                params.put(ServiceUrls.RequestParams.TYPE, String.valueOf(setCustomerLoc.types.get(cabType).typeId));
//                params.put(ServiceUrls.RequestParams.VC_ID, String.valueOf(setCustomerLoc.types.get(cabType).vcId));
//                params.put(ServiceUrls.RequestParams.PAYMENT_TYPE, "1");
//                params.put(ServiceUrls.RequestParams.PAYMENT_TYPE, String.valueOf(accountsCustomerVo.paymentTypeId));
                params.put(ServiceUrls.RequestParams.PAYMENT_TYPE, String.valueOf(getPaymentTypeId()));
                params.put(ServiceUrls.RequestParams.PICK_LOCATION, pick_address);
                params.put(ServiceUrls.RequestParams.DROP_LOCATION, drop_address);

                if (!btnPromoCode.getText().toString().equalsIgnoreCase(Common.getStringResourceText(R.string.add_promo_code)))
                {
                    params.put(ServiceUrls.RequestParams.PCODE, String.valueOf(btnPromoCode.getText()));
                }


                params.put(ServiceUrls.RequestParams.isAirport,Common.booleanToIntString(isAirPortBoundary));

                TATX.getInstance().sendSocketRequest(this,ServiceUrls.RequestNames.PLACE_ORDER,params);
/*

                coifirmatonlinearLayout.setVisibility(View.GONE);
                Intent intent2 = new Intent(this, CancelOrderActivity.class);
                intent2.putExtra("sourceAdd", sourceText.getText().toString());
                intent2.putExtra("destinationAdd", destinationText.getText().toString());
                intent2.putExtra("source_lat", latitude);
                intent2.putExtra("source_long", longitude);
                intent2.putExtra("destination_lat", destLat);
                intent2.putExtra("destination_long", destLong);

                startActivity(intent2);

*/

                break;






        }
    }

    private void moveCameraToPosition(LatLng latLngSelected)
    {

        Common.Log.i("? - latLngSelected : "+latLngSelected);

        if (latLngSelected != null)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngSelected));
/*

            CameraUpdate center = CameraUpdateFactory.newLatLng(latLngSelected);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
*/

            Common.Log.i("? - latLngSelected : "+latLngSelected);


        }


    }


    private void showPaymentOptionPopup()
    {
        final TextView online, cash, credits;
        selectPaymentTypeDialog = new Dialog(this);
        selectPaymentTypeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectPaymentTypeDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        selectPaymentTypeDialog.setContentView(R.layout.payment_option_popup);
        online = (TextView) selectPaymentTypeDialog.findViewById(R.id.online);
        cash = (TextView) selectPaymentTypeDialog.findViewById(R.id.cash);
        credits = (TextView) selectPaymentTypeDialog.findViewById(R.id.credits);

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                new RetrofitRequester(GoogleMapDrawerActivity.this).sendStringRequest(ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER,null,R.id.online);

            }
        });


        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdatePaymetAPI(String.valueOf(PaymentType.CASH.getId()));
                selectPaymentTypeDialog.cancel();
            }
        });


        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RetrofitRequester(GoogleMapDrawerActivity.this).sendStringRequest(ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER,null,R.id.credits);


            }
        });
        selectPaymentTypeDialog.setCancelable(true);
        selectPaymentTypeDialog.show();
    }


    @Override
    public void onStop() {
        super.onStop();

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



    public void confirmationLayout(boolean confirmValue) {
        if (confirmValue)
        {


            cab_name.setText(CabTypes.sliderDetails(cabTypeId).cabType);

            navigationClickRelativeL.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
            searchlayout.setVisibility(View.GONE);
            ivPickerIcon.setVisibility(View.GONE);
            tvEstimateTime.setVisibility(View.GONE);
            customAnimateMarker.setVisibility(View.GONE);
            coifirmatonlinearLayout.setVisibility(View.VISIBLE);
            relativeLayoutSeekbar.setVisibility(View.GONE);
            orderLayout.setVisibility(View.GONE);
            sourceText.setText(tvSourceSelected.getText().toString());
            destinationText.setText(tvDestinationSelected.getText().toString());
            flMapViewTpye.setVisibility(View.GONE);

        } else {
            if (googleMap != null) {
                resetCabsData();
            }
            navigationClickRelativeL.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.VISIBLE);
            searchlayout.setVisibility(View.VISIBLE);
            ivPickerIcon.setVisibility(View.VISIBLE);
            tvEstimateTime.setVisibility(View.VISIBLE);
            customAnimateMarker.setVisibility(View.VISIBLE);
            coifirmatonlinearLayout.setVisibility(View.GONE);
            relativeLayoutSeekbar.setVisibility(View.VISIBLE);
            orderLayout.setVisibility(View.VISIBLE);
            flMapViewTpye.setVisibility(View.VISIBLE);


        }
    }

    @Override
    public void onMapLoaded() {
        Log.d("OnLoad", "OnLoad.......");
    }

    @Override
    public void onGoogleMapTouch(MotionEvent event)
    {
        switch (event.getAction())
        {

            case MotionEvent.ACTION_DOWN:
                Common.Log.i("onGoogleMapTouch - Down.");

                if (confirmValue){
                    return;
                }
                llSuggtionView.setVisibility(View.GONE);
                llSuggtionViewDestination.setVisibility(View.GONE);
                 if (String.valueOf(stateNew).equalsIgnoreCase("EXPANDED")||String.valueOf(stateNew).equalsIgnoreCase("ANCHORED")) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                     return;
            }
                Common.Log.i("onGoogleMapTouch - Down.2");
                if (countDownTimer!=null) {
                    countDownTimer.cancel();
                }
                tvSourceSelected.setHint(Common.getStringResourceText(R.string.go_to_pin));

                if (!confirmValue) {
                    slideToBottom(hideRelativeLayout);
                    slideToBottom(relativeLayoutSeekbar);
                    fareDetailLayout.setVisibility(View.GONE);
                   // sildingUpPanel();
                    Common.hideSoftKeyboard(this);
                }


                //Careem Marker
                ScaleAnimation fade_out =  new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.2f);
                fade_out.setDuration(1000);
                fade_out.setFillAfter(true);
                llMarker.startAnimation(fade_out);
                ivFilledCircle.setVisibility(View.VISIBLE);
                ivTvTimeBg.setVisibility(View.GONE);
                tvTime.setVisibility(View.GONE);
//                tvMin.setVisibility(View.GONE);
                //Careem Marker






                break;

            case MotionEvent.ACTION_UP:
                Common.Log.i("onGoogleMapTouch - Up.");

                if (confirmValue){
                    return;
                }

                 if (String.valueOf(stateNew).equalsIgnoreCase("EXPANDED")||String.valueOf(stateNew).equalsIgnoreCase("ANCHORED")) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                     return;
            }

                if (!confirmValue) {
                    animateViewFromBottomToTop(hideRelativeLayout);
                    animateViewFromBottomToTop(relativeLayoutSeekbar);
//                    fareDetailLayout.setVisibility(View.VISIBLE);

                    // sildingUpPanel();
                }

                if (countDownTimer!=null) {
                    countDownTimer.start();
                }

                Log.d("Pressed::","sourcePressed "+sourcePressed+" destinationPressed "+destinationPressed+"confirmValue"+confirmValue);



                //Careem Marker

                ScaleAnimation fade_in =  new ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.2f);
                fade_in.setDuration(1000);
                fade_in.setFillAfter(true);
                llMarker.startAnimation(fade_in);
                ivFilledCircle.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivTvTimeBg.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                ScaleAnimation fade_in =  new ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                fade_in.setDuration(500);
                                fade_in.setFillAfter(true);

                                llTimeMin.setVisibility(View.VISIBLE);
                                llTimeMin.startAnimation(fade_in);


                                ScaleAnimation loaderFlashAnim =  new ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                loaderFlashAnim.setDuration(500);

                                ivLoaderFlash.startAnimation(loaderFlashAnim);


                                tvTime.setVisibility(View.VISIBLE);



//                                tvMin.setVisibility(View.VISIBLE);


                            }
                        },200);

                    }
                },1000);


                //Careem Marker



                break;


        }


    }


    @Override
    public void onSocketMessageReceived(final ApiResponseVo apiResponseVo, int requestId)
    {

//        Common.Log.i("Inside onSocketMessageReceived()");

        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());

        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        Common.Log.i("apiResponseVo.requestname : "+apiResponseVo.requestname);

        Common.Log.i("apiResponseVo.data : "+apiResponseVo.data);

        Common.Log.i("Switch Case Started");

        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.ON_SOCKET_OPEN:
                Common.Log.i("apiResponseVo.toString() : "+apiResponseVo.toString());
                break;


            case ServiceUrls.RequestNames.SET_CUSTOMER_LOC:

//                BroadcastLocationVo broadcastLocationVo = Common.getSpecificDataObject(apiResponseVo.data, BroadcastLocationVo.class);
                setCustomerLoc = Common.getSpecificDataObject(apiResponseVo.data, SetCustomerLoc.class);


                Common.Log.i("? - setCustomerLoc.types.size() : "+setCustomerLoc.types.size());

                if (setCustomerLoc.types.size() > 0)
                {



   //                 setCabDetails(PHASED_SEEKBAR_DEFAULT_INDEX);

//                    cabView(setCustomerLoc.types);

//                    int [] drawable=new int [setCustomerLoc.types.size()];

                    final StateListDrawable [] stateListDrawables = new StateListDrawable[setCustomerLoc.types.size()];
                    markerBitmapImgs=new Bitmap[setCustomerLoc.types.size()];


                    cabCatagoriesId = new int[setCustomerLoc.types.size()];

                    llCategoryNames.removeAllViews();

                    for (int i = 0; i< setCustomerLoc.types.size(); i++)
                    {

//                        drawable[i]=CabTypes.sliderDetails(Integer.parseInt(setCustomerLoc.types.get(i).typeId)).drawableImg;


                        String carImageUrl = setCustomerLoc.categoryCarImageUrl+setCustomerLoc.types.get(i).categoryCarImage;

                        Common.Log.i("carImageUrl : "+carImageUrl);

                        StateListDrawableWithBitmap stateListDrawableWithBitmap = getDrawableFromUrl(carImageUrl);

                        stateListDrawables[i] = stateListDrawableWithBitmap.getStateListDrawable();

                        String mapCarImageUrl = setCustomerLoc.mapCarImageUrl+setCustomerLoc.types.get(i).categoryCarImage;

                        markerBitmapImgs[i] = getBitmapFromUrl(mapCarImageUrl);

                        Common.Log.i("?????-Integer.parseInt(setCustomerLoc.types.get(i).typeId) : "+Integer.parseInt(setCustomerLoc.types.get(i).typeId));

                        cabCatagoriesId[i]=CabTypes.sliderDetails(Integer.parseInt(setCustomerLoc.types.get(i).typeId)).cabId;

                        TextView inflatedView = (TextView) getLayoutInflater().inflate(R.layout.category_name, null);

                        inflatedView.setText(setCustomerLoc.types.get(i).type);

                        inflatedView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1));

                        llCategoryNames.addView(inflatedView);


                    }


                    Common.Log.i("? - setCustomerLoc.types.size() : "+setCustomerLoc.types.size());

                    Common.Log.i("? - drawable.length : "+stateListDrawables.length);

//                    SimplePhasedAdapter simplePhasedAdapter = new SimplePhasedAdapter(resources, drawable);

                    SimplePhasedAdapter simplePhasedAdapter = new SimplePhasedAdapter(resources, stateListDrawables);

                    psbHorizontal.setAdapter(simplePhasedAdapter);

                    Common.Log.i("? - simplePhasedAdapter.getCount() : "+simplePhasedAdapter.getCount());

                    psbHorizontal.setFirstDraw(true);

                    btnOrder.setVisibility(View.VISIBLE);

                    psbHorizontal.setVisibility(View.VISIBLE);

                    relSeekbarLayout.setVisibility(View.VISIBLE);

                    llDragViewMain.setVisibility(View.VISIBLE);

                    fareDetailLayout.setVisibility(View.VISIBLE);


                    psbHorizontal.setOnItemClickListener(new PhasedOnItemClickListener()
                    {
                        @Override
                        public void onItemClick(int position)
                        {


                            Dialog dialog = new Dialog(GoogleMapDrawerActivity.this);

                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                            dialog.setContentView(R.layout.fare_estimate_popup);

                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

                            RelativeLayout faredetailLayout = (RelativeLayout) dialog.findViewById(R.id.faredetail_layout);
                            faredetailLayout.setVisibility(View.VISIBLE);
                            faredetailLayout.setPadding(0,0,0,0);

                            TextView tvCabCapacity = (TextView) dialog.findViewById(R.id.tv_cab_capacity);
                            TextView tvSarPerKm = (TextView) dialog.findViewById(R.id.tv_sar_per_km);
                            TextView tvMinFare = (TextView) dialog.findViewById(R.id.tv_min_fare);
                            TextView tvBaseFare = (TextView) dialog.findViewById(R.id.tv_base_fare);
                            TextView tvPricePerMin = (TextView) dialog.findViewById(R.id.tv_price_per_min);

                            setCabDetails(position, tvCabCapacity, tvSarPerKm, tvMinFare, tvBaseFare, tvPricePerMin);


                            dialog.show();

                        }

                    });


                    psbHorizontal.setListener(new PhasedListener() {
                        @Override
                        public void onPositionSelected(final int position) {


                            Common.Log.i("Inside Phased Seekbar Listener.");

                            setCabDetails(position, tvCabCapacity, tvSarPerKm, tvMinFare, tvBaseFare, tvPricePerMin);

                           // cabType = position;
                            PHASED_SEEKBAR_DEFAULT_INDEX = position;
                            cabTypeId=cabCatagoriesId[position];

                            resetCabsData();

                           // refreshCabsData(setCustomerLoc.drivers);

                        }
                    });


                    psbHorizontal.setPosition(PHASED_SEEKBAR_DEFAULT_INDEX);


                }
                else
                {
                    btnOrder.setVisibility(View.GONE);
                    psbHorizontal.setVisibility(View.GONE);
                    relSeekbarLayout.setVisibility(View.GONE);
                    llDragViewMain.setVisibility(View.GONE);

                }

                if (googleMap!=null)
                {
                    refreshCabsData(setCustomerLoc.drivers);
                }


                previousResponseSuccess=true;

                break;




            case Constants.BROADCAST_LOCATION:

                broadcastLocationVo = Common.getSpecificDataObject(apiResponseVo.data, BroadcastLocationVo.class);

                Common.Log.i("broadcastLocationVo.drivers.size() : "+broadcastLocationVo.drivers.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!confirmValue)
                        {

                            /*BroadcastLocationVo broadcastLocationVo = Common.getSpecificDataObject(apiResponseVo.data, BroadcastLocationVo.class);

                            Common.Log.i("broadcastLocationVo.drivers.size() : "+broadcastLocationVo.drivers.size());*/

                            if (googleMap!=null && markerBitmapImgs!=null)
                            {

                                    refreshCabsData(broadcastLocationVo.drivers);

                            }


                        }

                    }


                });

                break;


            case ServiceUrls.RequestNames.PLACE_ORDER:
                // java.lang.NullPointerException: Attempt to invoke virtual method 'void android.os.Handler.removeCallbacksAndMessages(java.lang.Object)' on a null object reference
               // at com.tatx.userapp.activities.GoogleMapDrawerActivity.onSocketMessageReceived(GoogleMapDrawerActivity.java:2313)
                //Make promo code null for next trip
                currentPoromocode=null;
                if (CancelOrderActivity.handlerCheckCustomerOnTripStatus!=null) {
                    CancelOrderActivity.handlerCheckCustomerOnTripStatus.removeCallbacksAndMessages(null);
                    CancelOrderActivity.instance().cancelTimer();
                }

                PlaceOrderVo placeOrderVo = Common.getSpecificDataObject(apiResponseVo.data, PlaceOrderVo.class);

                if (placeOrderVo.orderStatus)
                {

                    OnTrip onTrip = new OnTrip(Integer.parseInt(placeOrderVo.tripid),Integer.parseInt(placeOrderVo.orderid),Integer.parseInt(placeOrderVo.vcId),placeOrderVo.cancellationCharges,placeOrderVo.pickLatitude,placeOrderVo.pickLongitude,placeOrderVo.dropLatitude,placeOrderVo.dropLongitude,placeOrderVo.vehicleNo,placeOrderVo.make,placeOrderVo.model,placeOrderVo.profile,String.valueOf(placeOrderVo.rating),placeOrderVo.drivername,placeOrderVo.mobile,placeOrderVo.carImage,placeOrderVo.color,placeOrderVo.paymentType);

                    Common.getDefaultSP(this).edit().putLong(Constants.ORDER_CANCEL_TIME, System.currentTimeMillis()).commit();
                    Intent AddLocationMapActivityIntent = new Intent(this, OnTripMapActivity.class);
                    AddLocationMapActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    AddLocationMapActivityIntent.putExtra("action", 2);
                    AddLocationMapActivityIntent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS, Common.getCompleteAddressString(this,placeOrderVo.pickLatitude,placeOrderVo.pickLongitude).getCompleteAddress());
                    AddLocationMapActivityIntent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS,Common.getCompleteAddressString(this,placeOrderVo.dropLatitude,placeOrderVo.dropLongitude).getCompleteAddress());
                    AddLocationMapActivityIntent.putExtra(Constants.KEY_1, onTrip);

                    startActivity(AddLocationMapActivityIntent);




                }
                else
                {

                    Intent intent = new Intent(this, GoogleMapDrawerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }

                Common.customToast(GoogleMapDrawerActivity.this, placeOrderVo.message, Common.TOAST_TIME);


                break;


            case ServiceUrls.RequestNames.ORDER_INITIATED:

                OrderInitiatedVo orderInitiatedVo = Common.getSpecificDataObject(apiResponseVo.data, OrderInitiatedVo.class);

                Common.Log.i("orderInitiatedVo.toString() : "+orderInitiatedVo.toString());

                coifirmatonlinearLayout.setVisibility(View.GONE);

                Intent intent = new Intent(this, CancelOrderActivity.class);
                /*
                intent.putExtra("sourceAdd", sourceText.getText().toString());
                intent.putExtra("destinationAdd", destinationText.getText().toString());
                */
                intent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS,sourceText.getText().toString().trim());

                intent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS,destinationText.getText().toString().trim());


                intent.putExtra("source_lat", latitude);
                intent.putExtra("source_long", longitude);
                intent.putExtra("destination_lat", destLat);
                intent.putExtra("destination_long", destLong);
                intent.putExtra(Constants.KEY_1,orderInitiatedVo);

                startActivity(intent);


                break;



        }




    }



    private StateListDrawableWithBitmap getDrawableFromUrl(final String carImageUrl)
    {


        stateListDrawableWithBitmap = new StateListDrawableWithBitmap();

        categoryCarDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.vip);


        Common.Log.i("?????-carImageUrl : "+carImageUrl);

        final boolean[] bitmapFetched = {false};
/*


        final ProgressDialog categoryWaitProgressDialog = new ProgressDialog(this);

        categoryWaitProgressDialog.setMessage("Please dlfkjiejflesdj");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                categoryWaitProgressDialog.show();
            }
        });
//        categoryWaitProgressDialog.show();

*/
        Common.Log.i("????? - After categoryWaitProgressDialog.show()");








        do
        {

                        Common.Log.i("?????-carImageUrl : "+carImageUrl);


//                    Picasso.with(GoogleMapDrawerActivity.this).load(carImageUrl).networkPolicy(NetworkPolicy.NO_STORE).into(new Target()
                    Picasso.with(GoogleMapDrawerActivity.this).load(carImageUrl).networkPolicy(NetworkPolicy.NO_CACHE).into(new Target()
                    {

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                        {

                            Common.Log.i("? - onBitmapLoaded");

                            categoryCarDrawable = new BitmapDrawable(getResources(), bitmap);

                            bitmapImg  = bitmap;

                            bitmapFetched[0] = true;


                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                            Common.Log.i("? - onBitmapFailed");

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                            Common.Log.i("? - onPrepareLoad");




                        }


                    });




/*

            try
            {
                URL url = new URL(carImageUrl);

                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                categoryCarDrawable = new BitmapDrawable(getResources(), bitmap);

                bitmapImg  =bitmap;

                bitmapFetched[0] = true;

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

*/
        }while (!bitmapFetched[0]);


//        categoryWaitProgressDialog.dismiss();

        Common.Log.i("????? - After categoryWaitProgressDialog.dismiss()");




        StateListDrawable stateListDrawable = new StateListDrawable();

        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, categoryCarDrawable);

        stateListDrawable.addState(new int[] {android.R.attr.state_focused}, categoryCarDrawable);

        stateListDrawable.addState(new int[] {android.R.attr.state_selected}, categoryCarDrawable);

        stateListDrawable.addState(new int[] { }, getResources().getDrawable(R.drawable.circle));


        stateListDrawableWithBitmap.setStateListDrawable(stateListDrawable);

        stateListDrawableWithBitmap.setBitmap(bitmapImg);

        return stateListDrawableWithBitmap;



    }


    private Bitmap getBitmapFromUrl(String carImageUrl)
    {

       // markerCategoryCarDrawable = (Bitmap) getResources().getDrawable(R.drawable.vip);


        final boolean[] bitmapFetched = {false};

        do
        {


            Picasso.with(this).load(carImageUrl).into(new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                   // Common.getResizedBitmap(bitmap,(bitmap.getWidth()/100)*75,(bitmap.getHeight()/100)*75);
                    int newWidth=(int)(bitmap.getWidth()*DensityType.getEnumField(getResources().getDisplayMetrics().density).getDensityValue())/4;
                    int newHeight=(int)(bitmap.getHeight()*DensityType.getEnumField(getResources().getDisplayMetrics().density).getDensityValue())/4;

                    Common.Log.i("? - onBitmapLoaded"+getResources().getDisplayMetrics().density+"  " +DensityType.getEnumField(getResources().getDisplayMetrics().density).getDensityValue());
                    Common.Log.i("? - onBitmapLoaded"+bitmap.getWidth()+"  " +bitmap.getHeight()+"\n new height and width "+newWidth+" height "+newHeight);

                    bitmapImg  = Common.getResizedBitmap(bitmap,newWidth,newHeight);
                  //  bitmapImg=bitmap;
                    bitmapFetched[0] = true;


                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                    Common.Log.i("? - onBitmapFailed");

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                    Common.Log.i("? - onPrepareLoad");




                }


            });





        }while (!bitmapFetched[0]);

        return bitmapImg;



    }




    private void setCabDetails(int position, TextView tvCabCapacity, TextView tvSarPerKm, TextView tvMinFare, TextView tvBaseFare, TextView tvPricePerMin)
    {

//      Type type = broadcastLocationVo.types.get(PHASED_SEEKBAR_DEFAULT_INDEX);

        Type type = setCustomerLoc.types.get(position);


        tvCabCapacity.setText( type.capacity + " "+Common.getStringResourceText(R.string.people));

//      tvSarPerKm.setText( type.pricePerKm + " per KM");

        tvSarPerKm.setText("SAR " + type.pricePerKm);

        tvMinFare.setText("SAR " + type.minfare);

        tvBaseFare.setText("SAR " + type.baseFare);

        tvPricePerMin.setText("SAR " + type.pricePerMinute);


    }

    List<Driver> previousDriversList = new ArrayList<Driver>();

    HashMap<Integer,Marker> markersHashMap = new HashMap<Integer,Marker>();


    private void refreshCabsData(List<Driver> currentDriversList)
    {
        Common.Log.i("refreshCabsData - currentDriversList.toString()"+new Gson().toJson(currentDriversList));

        Common.Log.i("refreshCabsData - previousDriversList.toString()"+new Gson().toJson(previousDriversList));


        Collection<Driver> updateDriversList =  CollectionUtils.retainAll(previousDriversList, currentDriversList);

        Collection<Driver> deleteDriversList = CollectionUtils.removeAll(previousDriversList, updateDriversList);

        Collection<Driver> addDriversList = CollectionUtils.removeAll(currentDriversList, updateDriversList);

        Common.Log.i("refreshCabsData - updateDriversList.toString()"+updateDriversList.toString());

        Common.Log.i("refreshCabsData - deleteDriversList.toString()"+deleteDriversList.toString());

        Common.Log.i("refreshCabsData - addDriversList.toString()"+addDriversList.toString());

        previousDriversList.clear();

        previousDriversList.addAll(currentDriversList);

        minTimeToReach = DEFAULT_MIN_TIME_TO_REACH;

        refreshMarkers(Constants.UPDATE,updateDriversList);
        refreshMarkers(Constants.DELETE,deleteDriversList);
        refreshMarkers(Constants.ADD,addDriversList);

        String estimateTimeText = null;
        if (sourcePressed)
        {
            estimateTimeText = minTimeToReach == DEFAULT_MIN_TIME_TO_REACH ? NO_DRIVER : minTimeToReach + "\nMin";
        }
        else
        {
            estimateTimeText = "D";
        }


        tvEstimateTime.setText(estimateTimeText);


/*

        if (minTimeToReach == DEFAULT_MIN_TIME_TO_REACH)
        {
            tvMin.setVisibility(View.GONE);
        }
        else
        {
            tvMin.setVisibility(View.VISIBLE);

        }
*/


//        tvTime.setText(estimateTimeText.replace("\"\\nMin\"",""));


        tvTime.setText(estimateTimeText);


    }


    public void setMinTimeToReach(int reachTime)
    {
        Common.Log.i("reachTime : "+reachTime);

        if(minTimeToReach > reachTime)
        {
            minTimeToReach = reachTime;
        }
        Common.Log.i("minTimeToReach : "+minTimeToReach);


    }

    int minTimeToReach = DEFAULT_MIN_TIME_TO_REACH;

    private void refreshMarkers(String operation, Collection<Driver> collection)
    {


        for (Driver driver:collection)
        {

            Common.Log.i("driver.typeId : anil "+driver.typeId +" driver "+driver.latitude);
            Common.Log.i("cabTypeId :  "+cabTypeId+"driver.typeId :  "+driver.typeId +" \n"+collection.toString());


            if(driver.typeId != cabTypeId)
            {
                continue;
            }

            switch (operation)
            {

                case Constants.UPDATE:

                    setMinTimeToReach(driver.time);
                    Log.d("UPDTAE_Markers","1-> "+markersHashMap.toString()+"\n"+markersHashMap.get(driver.driverId)+"\n"+driver.driverId);
                    Marker markerToUpdate = markersHashMap.get(driver.driverId);

                    LatLng previousLatLng = markerToUpdate.getPosition();

                    LatLng currentLatLng = new LatLng(driver.latitude, driver.longitude);

                    moveMarker(markerToUpdate, previousLatLng, currentLatLng);
                    Log.d("addMarkers","1-> "+driver.latitude+","+driver.longitude);
                    break;


                case Constants.DELETE:
                    if (markersHashMap!=null&&markersHashMap.size()!=0) { //all other customer  app getting crashed after accepting request to one user

                        markersHashMap.get(driver.driverId).remove();

                        markersHashMap.remove(driver.driverId);
                    }

                    break;


                case Constants.ADD:

                    setMinTimeToReach(driver.time);

                    Marker markerToAdd = googleMap.addMarker(new MarkerOptions().position(new LatLng(driver.latitude, driver.longitude)).icon(BitmapDescriptorFactory.fromBitmap( markerBitmapImgs[PHASED_SEEKBAR_DEFAULT_INDEX])));

                    markersHashMap.put(driver.driverId,markerToAdd);


                    Log.d("addMarkers","3-> "+driver.latitude+","+driver.longitude);
                    break;


            }



        }



    }

    private void moveMarker(Marker marker, LatLng previousLatLng, LatLng currentLatLng)
    {

        Location previousLocation = new Location("");
        previousLocation.setLatitude(previousLatLng.latitude);
        previousLocation.setLongitude(previousLatLng.longitude);

        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatLng.latitude);
        currentLocation.setLongitude(currentLatLng.longitude);

        float rotationValue = previousLocation.bearingTo(currentLocation);
if (previousLocation.distanceTo(currentLocation)>10) {
    rotateMarker(marker, rotationValue);

    animateMarker(googleMap, marker, currentLatLng, false);
}


    }


    @Override
    public void onInternetConnected()
    {
//        Common.customToast(this, "GMDA - Internet Connected.");

        super.onInternetConnected();

        Common.Log.i("Inside GMDA - Internet Connected.");

        sendSocketRequestToSetCustomerLocationService();

    }

    @Override
    public void onInternetDisconnected()
    {

        super.onInternetDisconnected();
//        Common.customToast(this, "GMDA - " + Constants.NO_INTERNET_CONNECTION_MESSAGE);

        Common.Log.i("Inside GMDA - "+Constants.NO_INTERNET_CONNECTION_MESSAGE);

        resetCabsData();



    }

    private void resetCabsData()
    {
        googleMap.clear();

        previousDriversList.clear();

        tvEstimateTime.setText(NO_DRIVER);

        if (sourcePressed)
        {
            tvTime.setText(NO_DRIVER);
        }


//        tvMin.setVisibility(View.GONE);


    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {

        if (apiResponseVo.code != Constants.SUCCESS)
        {
            switch (apiResponseVo.requestname)
            {
                case ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY:

                    switch (apiResponseVo.code)
                    {

                        case Constants._201 :
                            Common.customToast(this, Common.getStringResourceText(R.string.promo_code_already_used));
                            break;

                        case Constants._202 :

                            Common.customToast(this, Common.getStringResourceText(R.string.invalid_promo_code));

                            break;




                    }

                    break;




                default:
                //    Common.customToast(this, apiResponseVo.status);
                    break;

            }





            return;
        }


        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.GET_CUSTOMER_PROFILE:

                GetCustomerProfileVo getCustomerProfileVo = Common.getSpecificDataObject(apiResponseVo.data, GetCustomerProfileVo.class);

                username.setText(getCustomerProfileVo.firstName);
                userlname.setText(getCustomerProfileVo.lastName);

                Common.setCircleImageBackgroundFromUrl(GoogleMapDrawerActivity.this,profilepic,getCustomerProfileVo.profilePic);
//                HashMap<String, String> params = new HashMap<>();
//                params.put(ServiceUrls.RequestParams.TOKEN,Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.REG_ID,null));
//                params.put(ServiceUrls.RequestParams.DEVICE_ID,Common.getDeviceId(this));
//                params.put(ServiceUrls.RequestParams.DEVICE,Constants.ANDROID);
//
//                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.UPDATE_DEVICE_TOKEN,params);
//if (getCustomerProfileVo.profilePic!=null&&getCustomerProfileVo.profilePic.length()>0)


                setNotificationIconCountText(getCustomerProfileVo.unreadNotificationsCount);

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_SAVED_LOCATIONS,null);

                break;

            case ServiceUrls.RequestNames.GET_LOYALITY_POINT:

                GetLoyalityPointVo getLoyalityPointVo = Common.getSpecificDataObject(apiResponseVo.data, GetLoyalityPointVo.class);

                creditpoints.setText(String.valueOf(getLoyalityPointVo.points));

                switch (getLoyalityPointVo.loyality)
                {

                    case Constants.LoyalityTypes.BRONZE:
                         loyaltiicon.setBackgroundResource(R.drawable.loyalty_bronz);
                        break;

                    case Constants.LoyalityTypes.SILVER:
                        loyaltiicon.setBackgroundResource(R.drawable.loyalty_silver);
                        break;

                    case Constants.LoyalityTypes.GOLD:
                        loyaltiicon.setBackgroundResource(R.drawable.loyalty_gold);
                        break;

                    case Constants.LoyalityTypes.DIAMOND:
                        loyaltiicon.setBackgroundResource(R.drawable.loyalty_diamond);
                        break;
                    case Constants.LoyalityTypes.LOYALTY_ZERO:
                        loyaltiicon.setBackgroundResource(R.drawable.ic_launcher);
                        break;

                }


            case ServiceUrls.RequestNames.ACCOUNTS_CUSTOMER:


//                AccountsCustomerVo accountsCustomerVo = Common.getSpecificDataObject(apiResponseVo.data, AccountsCustomerVo.class);
                accountsCustomerVo = Common.getSpecificDataObject(apiResponseVo.data, AccountsCustomerVo.class);

                setPaymentTypeId(accountsCustomerVo.paymentTypeId);


                switch (requestId)
                {



                    case R.id.order_button:

                        orderButtonAccountsCustomerApiResponseFunctionality(accountsCustomerVo);


                        break;




                    case R.id.online:

                        Common.Log.i("? - accountsCustomerVo.cards.size() : "+ accountsCustomerVo.cards.size());

                        if(accountsCustomerVo.cards.size()!= 0)
                        {
                            if (accountsCustomerVo.credits>=0)
                            {
                                callUpdatePaymetAPI(String.valueOf(PaymentType.CREDIT_CARD.getId()));
                            }
                            else
                            {
                                Common.customToast(this,Common.getStringResourceText(R.string.as_of_now_this_feature_is_not_available));

 //                               Common.showNegativeTatxBalanceErrorDialog(GoogleMapDrawerActivity.this,String.valueOf(accountsCustomerVo.credits));


                            }

                        }
                        else
                        {
                            showAddCreditCardConfirmationDialog(Common.getStringResourceText(R.string.no_cards_were_added), Common.getStringResourceText(R.string.please_add_card_to_process_do_you_want_to_add_credit_card), AccountActivity.class);

                        }

                        selectPaymentTypeDialog.cancel();

                        break;



                    case R.id.credits:

                        if(accountsCustomerVo.credits > 1)
                        {

                            callUpdatePaymetAPI(String.valueOf(PaymentType.TATX_BALANCE.getId()));

                        }
                        else
                        {
                            showAddCreditCardConfirmationDialog(Common.getStringResourceText(R.string.insufficient_tatx_balance), Common.getStringResourceText(R.string.please_add_tatx_balance_to_process_do_you_want_to_add_tatx_balance), AccountActivity.class);
                        }

                        selectPaymentTypeDialog.cancel();

                        break;




                }

                break;


            case ServiceUrls.RequestNames.UPDATE_PAYMENT_TYPE:

                UpdatePaymentTypeVo updatePaymentTypeVo = Common.getSpecificDataObject(apiResponseVo.data, UpdatePaymentTypeVo.class);


                String paymentType = updatePaymentTypeVo.payment;

                setPaymentTypeId(Integer.parseInt(paymentType));

                Common.Log.i("Common.getPaymentTypeById(Integer.parseInt(paymentType)).name() : "+Common.getPaymentTypeById(Integer.parseInt(paymentType)).name());

                btncash.setText(Common.getPaymentTypeById(Integer.parseInt(paymentType)).getString());

                editor.putInt(ServiceUrls.RequestParams.PAYMENT_TYPE, Integer.parseInt(paymentType));

                editor.commit();

                Drawable [] img={getResources().getDrawable(R.drawable.card), getResources().getDrawable(R.drawable.logo), getResources().getDrawable(R.drawable.cash)};

                switch (Integer.parseInt(paymentType))
                {
                    case 2:
                        img[0].setBounds(0, 0, 90, 50);
                        btncash.setCompoundDrawables(img[0], null, null, null);
                        btncash.setText(Common.getStringResourceText(R.string.credit_card));
                        break;

                    case 3:
                        btncash.setText(getResources().getString(R.string.my_balance));
                        img[1].setBounds(0, 0, 50, 50);
                        btncash.setCompoundDrawables(img[1], null, null, null);
                        break;


                    default:
                        btncash.setText(Common.getStringResourceText(R.string.cash));
                        img[2].setBounds(0, 0, 90, 50);
                        btncash.setCompoundDrawables(img[2], null, null, null);
                        break;

                }


                break;

            case ServiceUrls.RequestNames.USER_LOGOUT:

                UserLogoutVo userLogoutVo = Common.getSpecificDataObject(apiResponseVo.data, UserLogoutVo.class);

                Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, false).commit();

                Common.getDefaultSP(this).edit().putInt(Constants.SharedPreferencesKeys.USERID, 0).commit();

                //Common.getDefaultSP(this).edit().putString(Constants.SharedPreferencesKeys.REG_ID, "").commit();

                Common.Log.i("Language Goo"+Locale.getDefault().getLanguage());
                Common.customToast(this, userLogoutVo.status);

                startActivity(new Intent(this,SplashScreen.class));

                finish();



                break;



            case ServiceUrls.RequestNames.GET_FARE:

                GetFareVo getFareVo = Common.getSpecificDataObject(apiResponseVo.data, GetFareVo.class);

                Intent intent = new Intent(this, FareEstimateActivity.class);

                intent.putExtra(Constants.KEY_1,getFareVo);

                intent.putExtra(Constants.IntentKeys.SOURCE_ADDRESS,tvSourceSelected.getText().toString());

                intent.putExtra(Constants.IntentKeys.DESTINATION_ADDRESS,tvDestinationSelected.getText().toString());

                startActivity(intent);

                break;
            case ServiceUrls.RequestNames.UPDATE_DEVICE_TOKEN:

                UpdateDeviceTokenVo updateDeviceTokenVo = Common.getSpecificDataObject(apiResponseVo.data,UpdateDeviceTokenVo.class);

                Common.Log.i("updateDeviceTokenVo.message : "+updateDeviceTokenVo.message);



                break;


/*

            case ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY:

//                Common.customToast(this, apiResponseVo.status);
                Common.customToast(this, Common.getStringResourceText(R.string.promo_code_validated_successfully));

                break;

*/

            case ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY:


                Common.customToast(this, apiResponseVo.status);

                PromoValidityVo promoValidityVo = Common.getSpecificDataObject(apiResponseVo.data,PromoValidityVo.class);

                btnPromoCode.setText(promoValidityVo.promoCode);
                break;


            case ServiceUrls.RequestNames.GET_SAVED_LOCATIONS:

                getSavedLocationVo = Common.getSpecificDataObject(apiResponseVo.data, GetSavedLocationVo.class);

                Common.Log.i("updateDeviceTokenVo.message : " + getSavedLocationVo.toString());
                favLocationArrayList.addAll(getSavedLocationVo.favLocations);


                for (RecentLocation recent:getSavedLocationVo.recentLocations){
                    FavLocation favLocation=new FavLocation();
                    favLocation.latitude=recent.pickLatitude;
                    favLocation.longitude=recent.pickLongitude;
                    favLocation.name=recent.pickupLocation;

                    favLocationArrayList.add(favLocation);
                }


                break;
            case ServiceUrls.RequestNames.GET_COUNTRY_WISE_AIRPORT_DETAILS:
                airportByCountryVo = Common.getSpecificDataObject(apiResponseVo.data, AirportByCountryVo.class);
                //airPortFunctionality();
                break;



        }




    }

    private void orderButtonAccountsCustomerApiResponseFunctionality(final AccountsCustomerVo accountsCustomerVo) {

        if (accountsCustomerVo.credits < 0) {

            customerAlertDialog = new CustomerAlertDialog(this, "Your TATX balance is due " + accountsCustomerVo.currencyCode + " " + accountsCustomerVo.credits + "\n" + "to process further orders.", new DialogClickListener() {
                @Override
                public void onCancelClick() {
                    customerAlertDialog.dismiss();
                }

                @Override
                public void onOkClick() {
                    if (accountsCustomerVo.cards.size() == 0) {
//                   Common.setPreviousActivity(GoogleMapDrawerActivity.this);
//                   startActivity(new Intent(GoogleMapDrawerActivity.this,AddCreditCardActivity.class));

                    } else {
//                   Common.setPreviousActivity(GoogleMapDrawerActivity.this);
//                   startActivity(new Intent(GoogleMapDrawerActivity.this,AddCreditBalanceActivity.class));

                    }


                    customerAlertDialog.dismiss();
                }
            });
            customerAlertDialog.setCancelable(false);
            customerAlertDialog.show();

            confirmationLayout(false);


            return;
        }

        Drawable[] img = {getResources().getDrawable(R.drawable.card), getResources().getDrawable(R.drawable.logo), getResources().getDrawable(R.drawable.cash)};


        if ((accountsCustomerVo.paymentTypeId == PaymentType.CREDIT_CARD.getId() && accountsCustomerVo.cards.size() == 0) || (accountsCustomerVo.paymentTypeId == PaymentType.TATX_BALANCE.getId() && accountsCustomerVo.credits < 1) || (accountsCustomerVo.credits < 0 && accountsCustomerVo.paymentTypeId != PaymentType.CASH.getId())) {
//                paymentId = PaymentType.CASH.getId();

            callUpdatePaymetAPI(String.valueOf(PaymentType.CASH.getId()));

            return;


        }


        switch (accountsCustomerVo.paymentTypeId) {
            case 2:
                img[0].setBounds(0, 0, 90, 50);
                btncash.setCompoundDrawables(img[0], null, null, null);
                btncash.setText(Common.getStringResourceText(R.string.credit_card));
                break;

            case 3:
                btncash.setText(getResources().getString(R.string.my_balance));
                img[1].setBounds(0, 0, 50, 50);
                btncash.setCompoundDrawables(img[1], null, null, null);
                break;


            default:
                btncash.setText(Common.getStringResourceText(R.string.cash));
                img[2].setBounds(0, 0, 90, 50);
                btncash.setCompoundDrawables(img[2], null, null, null);
                break;

        }
        confirmValue = true;
     //   confirmationLayout(true);
        Common.showContentView(this, true);


    }


    private void callUpdatePaymetAPI(String paymentType)
    {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put(ServiceUrls.RequestParams.PAYMENT_TYPE, paymentType);

        new RetrofitRequester(GoogleMapDrawerActivity.this).sendStringRequest(ServiceUrls.RequestNames.UPDATE_PAYMENT_TYPE,params);

    }

    private void showAddCreditCardConfirmationDialog(String titleText, String messageText, final Class<?> activityToStart)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(GoogleMapDrawerActivity.this);

//        builder.setTitle("No Cards were Added");
        builder.setTitle(titleText);

//        builder.setMessage("Please Add Card to process.\n\nDo You Want to Add Credit Card ?");
        builder.setMessage(messageText);

        builder.setPositiveButton(Common.getStringResourceText(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

//                startActivity(new Intent(AccountActivity.this,AddCreditCardActivity.class));
                startActivity(new Intent(GoogleMapDrawerActivity.this,activityToStart));

                onBackPressed();

            }



        });


        builder.setNegativeButton(Common.getStringResourceText(R.string.no), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });



        builder.create().show();


    }

    // To animate view slide out from top to bottom
    public void slideToBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(false);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    // To animate view slide out from bottom to top
    public void slideToTop(View view,int height){
        Log.d("slidertop",String.valueOf(height));
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-height);
        animate.setDuration(500);
        animate.setFillAfter(false);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
        Log.d("slidertop",String.valueOf(height));

    }

    public static void animateViewFromBottomToTop(final View view){

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {

                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                final int TRANSLATION_Y = view.getHeight();
                view.setTranslationY(TRANSLATION_Y);
                view.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                view.animate()
                        .translationYBy(-TRANSLATION_Y)
                        .setDuration(100)
                        .setStartDelay(10)
                        .setListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationStart(final Animator animation) {

                                view.setVisibility(View.VISIBLE);
                            }
                        })
                        .start();
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.clear_source:
                   tvSourceSelected.setText("");
                  // mRecyclerView.setAdapter(null);
                   //suggetionSetAdapter(0);
                Log.d("suggetionSetAdapter", "suggetionSetAdapter 3486");

                //

                removeSourceMapMarker();
                //

                break;
            case R.id.clear_destination:
                   tvDestinationSelected.setText("");
                   //suggetionSetAdapter(0);
                Log.d("suggetionSetAdapter", "suggetionSetAdapter 3491");
                destinationLatLngSelected = null;

                //

                removeDestinationMapMarker();

                //

                break;
        }
        return false;
    }



    private void removeSourceMapMarker()
    {

        if (sourceMapMarker != null)
        {
            sourceMapMarker.remove();
        }

    }



    private void removeDestinationMapMarker()
    {


        if (destinationMapMarker != null)
        {
            destinationMapMarker.remove();
        }


    }





    public void selectDestinationByMapMove(CameraPosition cameraPosition) {

        destinationLatLngSelected=new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);
        addressDestination = Common.getCompleteAddressString(GoogleMapDrawerActivity.this, destinationLatLngSelected.latitude, destinationLatLngSelected.longitude).getCompleteAddress();
        tvDestinationSelected.setText(addressDestination);


        addDestinationMapMarker();


        // tvDestinationSelected.performCompletion();


    }

    private void setViewsVisibilityStatus(int status, View... views)
    {


        Common.Log.i("views.length : "+views.length);

        for (View view : views)
        {

            view.setVisibility(status);

        }


    }

    private void logUser()
    {

        Common.Log.i("? - String.valueOf(Common.getUserIdFromSP(this)) : "+String.valueOf(Common.getUserIdFromSP(this)));
        Crashlytics.setUserIdentifier(String.valueOf(Common.getUserIdFromSP(this)));

        Common.Log.i("? - Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.EMAIL_ID,\"\") : "+Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.EMAIL_ID,""));
        Crashlytics.setUserEmail(Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.EMAIL_ID,""));

        Common.Log.i("? - Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.FIRST_NAME,\"\") : "+Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.FIRST_NAME,""));
        Common.Log.i("? - Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.LAST_NAME,\"\") : "+Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.LAST_NAME,""));
        Crashlytics.setUserName(Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.FIRST_NAME,"")+" "+Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.LAST_NAME,""));

        Common.Log.i("? - Common.getDeviceId(this) : "+Common.getDeviceId(this));
        Crashlytics.setString(ServiceUrls.RequestParams.DEVICE_ID,Common.getDeviceId(this));




    }



    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public static GoogleMapDrawerActivity getInstatnce() {
        return instance;
    }

    @Override
    public void onUnReadNotificationCountChage(int count)
    {

        setNotificationIconCountText(count);

    }

    public void mRecyclerViewSourceClickListner(LatLng latLng) {

        latitude = latLng.latitude;
        longitude = latLng.longitude;
        // suggetionOnBackPressed();
        GeoDetails geoDetails = Common.getCompleteAddressString(GoogleMapDrawerActivity.this, latLng.latitude, latLng.longitude);
        Log.d("geoDetails",geoDetails.toString()+latitude+" ,"+longitude+" OR "+latLng.latitude+"  "+ latLng.longitude);
        sourceAddress =geoDetails.getAddress();
       // tvSourceSelected.setText(place.getAddress().toString());

        addSourceMapMarker();

        llSuggtionView.setVisibility(View.GONE);
        zoomCamera(latLng.latitude, latLng.longitude);
        sendSocketRequestToSetCustomerLocationService();
    }

    public void mRecyclerViewDestinationClickListner(LatLng latLng) {

        destinationLatLngSelected = latLng;
        //tvDestinationSelected.setText(place.getAddress().toString());

        llSuggtionViewDestination.setVisibility(View.GONE);
        addDestinationMapMarker();


        //suggetionOnBackPressed();
        zoomCamera(destinationLatLngSelected.latitude, destinationLatLngSelected.longitude);

    }


    public class MyBroadcastReceiver extends BroadcastReceiver {
        int code=0;
        @Override
        public void onReceive(Context context, Intent intent) {

            Common.Log.i("intent.getStringExtra(\"from\") : "+intent.getStringExtra("from"));

            Bundle bundle=intent.getExtras();

            FavLocation favLocation = (FavLocation) bundle.getSerializable(Constants.FAVOURITE_LOCATION);

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(favLocation.latitude, favLocation.longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            selectDestinationByMapMove(googleMap.getCameraPosition());
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), Constants.CUSTOM_FONT_PATH);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @OnClick(R.id.btn_promo_code)
    void usePromoCode()
    {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.add_promo_manually);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        final EditText cetEnterPromoCode = (EditText) dialog.findViewById(R.id.cet_enter_promo_code);

        TextView tvValidateButton = (TextView) dialog.findViewById(R.id.tv_validate_button);

        TextView tvCancelButton = (TextView) dialog.findViewById(R.id.tv_cancel_button);
        final RelativeLayout removePromo = (RelativeLayout) dialog.findViewById(R.id.clear_source);


        if (!btnPromoCode.getText().toString().equalsIgnoreCase(Common.getStringResourceText(R.string.add_promo_code)))
        {
            cetEnterPromoCode.setText(btnPromoCode.getText().toString());
            //removePromo.setVisibility(View.VISIBLE);

        }

        tvValidateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            if (cetEnterPromoCode.getText().toString().trim().length()<1){
                btnPromoCode.setText(Common.getStringResourceText(R.string.add_promo_code));
                dialog.dismiss();
                return;
            }
                Common.hideSoftKeyboardFromDialog(dialog,GoogleMapDrawerActivity.this);

                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put(ServiceUrls.RequestParams.PCODE,cetEnterPromoCode.getText().toString().trim());

                new RetrofitRequester(GoogleMapDrawerActivity.this).sendStringRequest(ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY,hashMap);

                dialog.dismiss();



            }
        });


        tvCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        removePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cetEnterPromoCode.setText("");
                dialog.dismiss();
                btnPromoCode.setText(Common.getStringResourceText(R.string.add_promo_code));
            }
        });



    }


    private void showOfferDialog(String offerImg)
    {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.offer_popup_home);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView offerImage=(ImageView) dialog.findViewById(R.id.offer_image);
        ImageView cancel=(ImageView) dialog.findViewById(R.id.cancel);

        Picasso.with(this).load(offerImg).into(offerImage);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();



    }

    private void showAirportDialog(String city,String sourceName)
    {


        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.airport_popup);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView tvSourceSelected=(TextView) dialog.findViewById(R.id.tv_source_selected);
        TextView welcome=(TextView) dialog.findViewById(R.id.welcome);
        final RelativeLayout rootLayout = (RelativeLayout)dialog.findViewById(R.id.root_layout);
        tvSourceSelected.setText(sourceName);
        welcome.setText(city);


        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.dismiss();
                return true;
            }
        });

        dialog.show();



    }



private void allAirportDataApiCall(String countryName){
    HashMap<String, String> params = new HashMap();
    params.put(ServiceUrls.RequestParams.COUNTRY_NAME, countryName);

    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_COUNTRY_WISE_AIRPORT_DETAILS, params);
}

    private void airPortFunctionality(){
        GeoDetails geoDetails=new GeoDetails(null,null,null,null);
        airPortFunctionality(geoDetails);
    }


    private void airPortFunctionality(GeoDetails geoDetails){

        boolean inAirPortBoundary = false;
        if (countryNameGeo==null&&geoDetails.getCountry()!=null) {
            countryNameGeo = geoDetails.getCountry();
            allAirportDataApiCall(geoDetails.getCountry());
        }
        if (!countryNameGeo.equalsIgnoreCase(geoDetails.getCountry())){
            allAirportDataApiCall(geoDetails.getCountry());
        }
        if (airportByCountryVo != null && airportByCountryVo.airportDetails.size() > 0) {
            for (AirportDetail airportDetail : airportByCountryVo.airportDetails) {

                if (airportDetail.latNE==0.0|| airportDetail.lngNE==0.0 || airportDetail.latSW==0.0 || airportDetail.lngSW==0.0) {
                    continue;
                }
                LatLngBounds sourceLatLngBounds = new LatLngBounds(new LatLng(airportDetail.latSW, airportDetail.lngSW), new LatLng(airportDetail.latNE, airportDetail.lngNE));

                inAirPortBoundary = sourceLatLngBounds.contains(new LatLng(latitude,longitude));
                Location location=new Location("");
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                Location location1=new Location("");
                location1.setLatitude(airportDetail.lat);
                location1.setLongitude(airportDetail.lng);
                float radius = location.distanceTo(location1);

                Log.d("airport","airport 0 "+ inAirPortBoundary+"\n previousLat" +previousLat+"\n previousLng "+previousLng+"\n airportDetail.lat "+airportDetail.lat+"\n airportDetail.lng "+airportDetail.lng);
                if (inAirPortBoundary || Math.abs(radius)<300) {
                    Log.d("airport","airport 1 "+ inAirPortBoundary+"\n previousLat" +previousLat+"\n previousLng "+previousLng+"\n airportDetail.lat "+airportDetail.lat+"\n airportDetail.lng "+airportDetail.lng);
                 //   if ((airportDetail.lat!=previousLat&& airportDetail.lng!=previousLng) || (previousLat==0.0&&previousLng==0.0)) {
                        Log.d("airport","airport 2 "+ inAirPortBoundary+"\n previousLat" +previousLat+"\n previousLng "+previousLng+"\n airportDetail.lat "+airportDetail.lat+"\n airportDetail.lng "+airportDetail.lng);
                 //       previousLat=airportDetail.lat;
                 //       previousLng=airportDetail.lng;
                       // GeoDetails addresses = Common.getCompleteAddressString(this, airportDetail.lat, airportDetail.lng);
                        showAirportDialog("Welcome to "+airportDetail.city,airportDetail.name);
                      //  Log.d("addresses",addresses.getCityName());
                        isAirPortBoundary=true;
                        Log.d("airport","airport 3 "+ inAirPortBoundary+"\n previousLat" +previousLat+"\n previousLng "+previousLng+"\n airportDetail.lat "+airportDetail.lat+"\n airportDetail.lng "+airportDetail.lng);
                  //  }

                    break;
                }
            }

            if (!inAirPortBoundary){
                previousLat=0.0;
                previousLng=0.0;
                isAirPortBoundary=false;
            }


        }
    }

    private void updateCameraBearing(GoogleMap googleMap, float bearing)
    {



        Common.Log.i("? - googleMap : "+googleMap+" bearing "+bearing);

        if (googleMap == null) return;

        CameraPosition camPos = CameraPosition.builder(googleMap.getCameraPosition() /*Current Camera*/).bearing(bearing).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));

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

    @OnClick(R.id.tv_normal) void setMapViewNormal(){
        tvNormal.setBackgroundResource(R.drawable.button_bg_red_zero_radius);
        tvSatellite.setBackgroundResource(R.drawable.white_view);
        tvNormal.setTextColor(getResources().getColor(R.color.white));
        tvSatellite.setTextColor(getResources().getColor(R.color.primary_text));

        if (googleMap!=null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    @OnClick(R.id.tv_satellite) void setMapViewSatellite(){
        tvNormal.setBackgroundResource(R.drawable.white_view);
        tvSatellite.setBackgroundResource(R.drawable.button_bg_red_zero_radius);
        tvNormal.setTextColor(getResources().getColor(R.color.primary_text));
        tvSatellite.setTextColor(getResources().getColor(R.color.white));

        if (googleMap!=null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }
}

