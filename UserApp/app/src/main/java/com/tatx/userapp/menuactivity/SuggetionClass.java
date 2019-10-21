package com.tatx.userapp.menuactivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.customviews.NpaGridLayoutManager;
import com.tatx.userapp.googlemapadapters.PlacesAutoCompleteAdapter;
import com.tatx.userapp.pojos.FavLocation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Home on 19-12-2016.
 */
public class SuggetionClass extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks
       {

           @BindView(R.id.rv_google_search)
           RecyclerView mRecyclerView;
           @BindView(R.id.recyclerview_saved_locatons)  RecyclerView recyclerviewSavedLocatons;
           @BindView(R.id.enter_text)
           EditText enterText;

           private GoogleApiClient mGoogleApiClient;
           private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
           private static final LatLngBounds BOUNDS_JAMAICA = new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531), new LatLng(72.77492067739843, -9.998857788741589));


           LocationRequest mLocationRequest;
           private static final long INTERVAL = 1000 * 5;
           private static final long FASTEST_INTERVAL = 1000 * 2;
           private static final float MINIMUM_DISPLACEMENT_IN_METERS = 35;
           private NpaGridLayoutManager mLinearLayoutManager;
           private GridLayoutManager gridLayoutManager;

           ArrayList <FavLocation> favLocationArrayList=new ArrayList<>();


           protected void createLocationRequest() {
               mLocationRequest = new LocationRequest();
               mLocationRequest.setInterval(INTERVAL);
               mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
               mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
               mLocationRequest.setSmallestDisplacement(MINIMUM_DISPLACEMENT_IN_METERS);
           }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggtion_class);
        ButterKnife.bind(this);

        createLocationRequest();




        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        for (int i=0;i<15;i++){
            FavLocation favLoc = new FavLocation();
            favLoc.name="Anil"+i;
            favLocationArrayList.add(favLoc);

        }
        FavLocation favLoc = new FavLocation();
        favLoc.name="Zero Index Anil";
        favLocationArrayList.add(0,favLoc);


        Log.d("favLocationArrayList",favLocationArrayList.toString());

      /*  mAutoCompleteAdapter =  new PlacesAutoCompleteAdapter(this, R.layout.searchview_adapter,
                mGoogleApiClient, BOUNDS_JAMAICA, null);

          *//*
        * Adds auto complete adapter to both auto complete
        * text views.
        * *//*
        mLinearLayoutManager=new NpaGridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);


        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerviewSavedLocatons.setLayoutManager(gridLayoutManager);
        if (favLocationArrayList!=null){
        SavedLocationListAdapterFilterable savedLocationListAdapterFilterable = new SavedLocationListAdapterFilterable(this,favLocationArrayList , 1);
        recyclerviewSavedLocatons.setAdapter(savedLocationListAdapterFilterable);

    } else {
            recyclerviewSavedLocatons.setVisibility(View.GONE);
       }
        enterText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.d("onTextChanged",s.toString());
                ((SavedLocationListAdapterFilterable) recyclerviewSavedLocatons.getAdapter()).getFilter().filter(s);
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());

                } else if (!mGoogleApiClient.isConnected()) {
                    // Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e(Constants.PlacesTag, Constants.API_NOT_CONNECTED);

                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Common.hideSoftKeyboard(SuggetionClass.this);

                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);


                        if (enterText.isFocused()) {
                            if (item == null) {
                                if (enterText.getText().length() > 0) {
                                    Toast.makeText(getApplicationContext(), Common.getStringResourceText(R.string.no_result_for) + enterText.getText(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), Common.getStringResourceText(R.string.please_enter_address) + enterText.getText(), Toast.LENGTH_SHORT).show();
                                }

                                return;
                            }
                            final String placeId = String.valueOf(item.placeId);

                        *//*
                        Issue a request to the Places Geo Data API to retrieve a Place object with additional
                        details about the place.
                        *//*
                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);
                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (!places.getStatus().isSuccess()) {
                                        // Request did not complete successfully
                                        Log.d("Place query", "Place query did not complete. Error: " + places.getStatus().toString());
                                        places.release();
                                        return;
                                    }
                                    // Get the Place object from the buffer.
                                    final Place place = places.get(0);
                                    LatLng latLng = place.getLatLng();


                                    Log.d("Place query", "Place query complete.  " + places.getStatus().toString() + "Add: " + place.toString());
                                }
                            });
                        }
                    }
                })
        );
*/

        Common.setSuggetions(this,mGoogleApiClient,recyclerviewSavedLocatons,mRecyclerView,favLocationArrayList,enterText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
