package com.tatx.userapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.googlemapadapters.PlaceAutoCompleteAdapter;
import com.tatx.userapp.googlemapadapters.Util;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.FavLocation;
import com.tatx.userapp.pojos.GetSavedLocationVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddLocationsByMapActivity extends BaseActivity implements GoogleMap.OnCameraChangeListener,com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, RetrofitResponseListener {


    private static final String LOG = AddLocationsByMapActivity.class.getSimpleName();

    private GoogleMap googleMap = null;
    protected LatLng end;
    private static final String LOG_LOG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    double latitude = 17.4483000;
    double longitude = 78.3915000;

    ImageView view;
    private Location lastknownLocation = null;
    private String address = "";
    private int action=1;

    ProgressDialog progressDialog;

    @BindView(R.id.my_current_location)
    ImageButton myCurrentLocation;
    @BindView(R.id.save_button)
    Button saveButton;

    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayoutSearch;

    @BindView(R.id.startloc)
    AutoCompleteTextView startloc;

    @BindView(R.id.clear_source) RelativeLayout clearSource;

    private int count = 1;
    private FavLocation favLocationForUpdate;
    private PlaceAutoCompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_JAMAICA = new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531), new LatLng(72.77492067739843, -9.998857788741589));
    private boolean suggestionStatus=false;
    private GetSavedLocationVo getSavedLocationVo;
    private boolean onConnecedCall=false;


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addlocation_by_map);
        Log.d("name:: ", "Clicked..rl_update_location 3");
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.addlocation));
        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());

        progressDialog=ProgressDialog.show(this,"",Common.getStringResourceText(R.string.please_wait));
        if (!Util.isGooglePlayServicesAvailable(this)) {
            finish();
        }

        Log.d(LOG, String.valueOf(Util.isGooglePlayServicesAvailable(this)));

        createLocationRequest();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        displayGoogleLocationSettingPage(this);


        Log.d("name:: ", "Clicked..rl_update_location 4");
        initiLazedAll();


    }

    @OnClick(R.id.my_current_location)
    void setMyCurrentLocation() {
        if (lastknownLocation != null)
            myCurrentLocationCamera(lastknownLocation);
    }

    @OnClick(R.id.save_button)
    void saveLocationButton() {

            saveLocationAlert(action);

    }


    @Override
    protected void onStart() {
        super.onStart();


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

    }


    public void initiLazedAll() {

        Bundle bundle = getIntent().getExtras();

        Common.Log.i("bundle.toString() : " + bundle.toString());

        action = getIntent().getIntExtra(Constants.IntentKeys.REQUEST_ACTION, 1);
        Log.d("methoscalling", "initiLazedAll");
        if (action == 2) {
           // Log.d("methoscalling", "initiLazedAll" + action + getIntent().getSerializableExtra(Constants.IntentKeys.UPDATAE_LOCATION_KEY).toString());
            saveButton.setText(Common.getStringResourceText(R.string.update));
            favLocationForUpdate = (FavLocation) getIntent().getSerializableExtra(Constants.IntentKeys.UPDATAE_LOCATION_KEY);
        }
        getSavedLocationVo = (GetSavedLocationVo) getIntent().getSerializableExtra(Constants.IntentKeys.GET_SAVED_LOCATION_VO);
        Log.d("name:: ", "Clicked..rl_update_location 5");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maploc);


        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.maploc, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_JAMAICA, null);
          /*
        * Adds auto complete adapter to both auto complete
        * text views.
        * */
        startloc.setAdapter(mAdapter);
        //seekbar select cab


        startloc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.hideSoftKeyboard(AddLocationsByMapActivity.this);
                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                //clear.setVisibility(View.VISIBLE);
                if (item==null){
                    if (startloc.getText().length()>0) {
                        Toast.makeText(getApplicationContext(), "No result for " + startloc.getText(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Please enter address" + startloc.getText(), Toast.LENGTH_SHORT).show();
                    }

                    return;
                }
                final String placeId = String.valueOf(item.placeId);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            Log.d(LOG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);
                        LatLng latLng = place.getLatLng();
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;

                        myCurrentLocationCamera(latLng,place.getAddress().toString());



                        Log.d(LOG, "Place query complete.  " + places.getStatus().toString());
                    }
                });
            }
        });


        startloc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (startloc.getText().toString().length()>0)
                    clearSource.setVisibility(View.VISIBLE);
                else
                    clearSource.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.d("name:: ", "Clicked..rl_update_location 6");
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.v(LOG_LOG, connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {

        onConnecedCall=true;
        if (mGoogleApiClient != null) {
            startLocationUpdates();
        }

        Log.d("name:: ", "Clicked..rl_update_location 8"+lastknownLocation);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("name:: ", "Clicked..rl_update_location 9");
        googleMap = map;

        googleMap.getUiSettings().setRotateGesturesEnabled(false);

        if (googleMap != null) {
            googleMap.setOnCameraChangeListener(this);


        }

    }

    public void myCurrentLocationCamera(Location location) {
        if (location != null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            address = Common.getCompleteAddressString(AddLocationsByMapActivity.this, location.getLatitude(), location.getLongitude()).getCompleteAddress();
            startloc.setText(address);
        }
    }
    public void myCurrentLocationCamera(LatLng latLng,String address) {
        if (latLng != null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            startloc.setText(address);
            startloc.performCompletion();
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
                        if (lastknownLocation == null) {
                            Log.d(LOG, "OnresultAxtivity2" + lastknownLocation);
                            int count1 = 1;
                            while (count1 <= 5) {
                                Log.d(LOG, "OnresultAxtivity2");
                                lastknownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                                count1++;
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (lastknownLocation != null) {
                                    myCurrentLocationCamera(lastknownLocation);

                                    break;
                                }
                            }
                        }
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


    public void saveFavLocationsApi(String name, String latitude, String longitude) {

        HashMap<String, String> params = new HashMap();
        params.put(ServiceUrls.RequestParams.NAME, name);
        params.put(ServiceUrls.RequestParams.LATITUDE, latitude);
        params.put(ServiceUrls.RequestParams.LONGITUDE, longitude);
        params.put(ServiceUrls.RequestParams.STATUS, "0");
        params.put(ServiceUrls.RequestParams.ADDRESS, address);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.SAVE_FAV_LOCATIONS, params);


    }


    public void saveLocationAlert(final int action) {
        final EditText locationName;
        TextView save;
        TextView cancel;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.alert_save_location);
        locationName = (EditText) dialog.findViewById(R.id.enter_location);
        save = (TextView) dialog.findViewById(R.id.but_save);
        cancel = (TextView) dialog.findViewById(R.id.but_cancel);
        if (action==2){
            save.setText(Common.getStringResourceText(R.string.update));
            locationName.setText(favLocationForUpdate.name);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideSoftKeyboard(AddLocationsByMapActivity.this);
                dialog.cancel();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideSoftKeyboard(AddLocationsByMapActivity.this);
                if (getSavedLocationVo!=null){
                    if (getSavedLocationVo.favLocations.size()>0){
                        for (FavLocation favLocation: getSavedLocationVo.favLocations){
                            if (favLocation.name.equalsIgnoreCase(locationName.getText().toString().trim()) && action==1){
                                Common.customToast(AddLocationsByMapActivity.this,Common.getStringResourceText(R.string.location_name)+" "+favLocation.name+" "+Common.getStringResourceText(R.string.already_exist));
                           return;
                            }else if (favLocation.name.equalsIgnoreCase(locationName.getText().toString().trim()) && action==2){
                                if (locationName.getText().toString().trim().equalsIgnoreCase(favLocationForUpdate.name)){

                                }else {
                                    Common.customToast(AddLocationsByMapActivity.this, Common.getStringResourceText(R.string.location_name) + " " + favLocation.name + " " + Common.getStringResourceText(R.string.already_exist));
                                    return;
                                }
                            }
                        }
                    }
                }
                if (locationName.getText().toString().length() > 0) {
                    if (action==2){
                            HashMap<String, String> params = new HashMap();
                            params.put(ServiceUrls.RequestParams.ID, favLocationForUpdate.id);
                            params.put(ServiceUrls.RequestParams.NAME, locationName.getText().toString().trim());
                            params.put(ServiceUrls.RequestParams.LATITUDE,  String.valueOf(latitude));
                            params.put(ServiceUrls.RequestParams.LONGITUDE,String.valueOf(longitude));
                            params.put(ServiceUrls.RequestParams.STATUS, "0");
                            params.put(ServiceUrls.RequestParams.ADDRESS,startloc.getText().toString());

                            new RetrofitRequester(AddLocationsByMapActivity.this).sendStringRequest(ServiceUrls.RequestNames.UPDATE_FAV_LOCATIONS, params);
                    }else {

                        saveFavLocationsApi(locationName.getText().toString(), String.valueOf(latitude), String.valueOf(longitude));
                    }
                    dialog.cancel();
                } else {
                    Common.customToast(AddLocationsByMapActivity.this, Common.getStringResourceText(R.string.please_enter_location_name), Common.TOAST_TIME);
                }


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

            case ServiceUrls.RequestNames.SAVE_FAV_LOCATIONS:
                Common.customToast(this, apiResponseVo.status);
                Intent intent1 = new Intent();
                setResult(Activity.RESULT_OK, intent1);
                finish();

                break;
            case ServiceUrls.RequestNames.UPDATE_FAV_LOCATIONS:
                Common.customToast(this, apiResponseVo.status);
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

                break;
        }
    }

    protected void startLocationUpdates() {
        PendingResult<Status> fgfdg = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("methoscalling", location.toString());
        if (location != null)
            lastknownLocation = location;
        if (onConnecedCall){
            onConnecedCall=false;
            if (lastknownLocation != null && action == 1) {
                myCurrentLocationCamera(lastknownLocation);

            } else if (favLocationForUpdate != null && action == 2) {
                Location location1 = new Location("");
                location1.setLatitude(favLocationForUpdate.latitude);
                location1.setLongitude(favLocationForUpdate.longitude);
                myCurrentLocationCamera(location);
            }


        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        latitude = cameraPosition.target.latitude;
        longitude = cameraPosition.target.longitude;

        address = Common.getCompleteAddressString(AddLocationsByMapActivity.this, latitude, longitude).getCompleteAddress();
        startloc.setText(address);
        startloc.performCompletion();


    }

    @OnClick (R.id.clear_source) void clearSource(){
        startloc.setText("");
    }
}

