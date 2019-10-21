package com.tatx.userapp.menuactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.activities.AddLocationsByMapActivity;
import com.tatx.userapp.adapter.LocationListAdapter;
import com.tatx.userapp.adapter.ResentLocationListAdapter;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.customviews.CustomerAlertDialog2;
import com.tatx.userapp.dataset.UserLocations;
import com.tatx.userapp.interfaces.DialogClickListener;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.FavLocation;
import com.tatx.userapp.pojos.GetSavedLocationVo;
import com.tatx.userapp.pojos.RecentLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Home on 02-05-2016.
 */
public class AddLocationActivity extends BaseActivity implements View.OnClickListener,RetrofitResponseListener {
    private static final int LOCATION_SAVED = 100;
    public static AddLocationActivity inst;
    LocationListAdapter locationListAdapter;
    GridLayoutManager gridLayoutManager;
    private List<UserLocations> locationlist=new ArrayList<>();
    private GetSavedLocationVo getSavedLocationVo;
    private CustomerAlertDialog2 customerAlertDialog2;

    @BindView(R.id.add_locations) ImageView addLocations;
    @BindView(R.id.recordnotfnd) TextView recordnotfnd;
    @BindView(R.id.recent_location_recycler_view) RecyclerView recentLocationRecycleView;
    @BindView(R.id.saved_location_recycler_view) RecyclerView savedLocationRecyclerView;
    @BindView(R.id.hide_keyboard_layout) RelativeLayout hideKeyboardView;

    public static AddLocationActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_location);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.locations));

        initialiseMap();

        initilaizedAll();


    }

    public void initilaizedAll() {
        Common.hideSoftKeyboard(this);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_SAVED_LOCATIONS,null);


        addLocations.setOnClickListener(this);
        hideKeyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Common.hideSoftKeyboard(AddLocationActivity.this);
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.add_locations:



                if (getSavedLocationVo==null || getSavedLocationVo.favLocations==null || getSavedLocationVo.favLocations.size()<2){

                    Bundle bundle1 = new Bundle();
                    Intent addlocation1 = new Intent(this, AddLocationsByMapActivity.class);
                    bundle1.putInt(Constants.IntentKeys.REQUEST_ACTION, 1);
                    bundle1.putSerializable(Constants.IntentKeys.GET_SAVED_LOCATION_VO, getSavedLocationVo);
                    addlocation1.putExtras(bundle1);
                    startActivityForResult(addlocation1, LOCATION_SAVED);
                }else {

                    customerAlertDialog2 = new CustomerAlertDialog2(this, Common.getStringResourceText(R.string.you_already_added_two_loc), Common.getStringResourceText(R.string.dismiss), new DialogClickListener() {
                        @Override
                        public void onCancelClick() {

                        }

                        @Override
                        public void onOkClick() {
                            customerAlertDialog2.dismiss();
                        }
                    });

                    customerAlertDialog2.show();
                }
                break;
        }
    }
    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        Log.d("Saved location", "OnresultAxtivity1");
        switch (requestCode) {
            case LOCATION_SAVED:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        locationlist.clear();
                        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_SAVED_LOCATIONS,null);

                        Log.d("Saved location", "OnresultAxtivity1");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                //        finish();

                        break;

                    default:
                        break;
                }
                break;


        }
    }



    public void setListOnAdapter(final List<FavLocation> favLocation) {
        gridLayoutManager = new GridLayoutManager(this, 1);
        savedLocationRecyclerView.setLayoutManager(gridLayoutManager);
        if (favLocation.size() != 0&& favLocation !=null) {
            recordnotfnd.setVisibility(View.GONE);
            savedLocationRecyclerView.setVisibility(View.VISIBLE);
            locationListAdapter = new LocationListAdapter(this, favLocation);
            savedLocationRecyclerView.setAdapter(locationListAdapter);
            locationListAdapter.setOnItemClickListener(new LocationListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    Log.d("name:: ", "Clicked..");
                    switch (view.getId()){
                        case R.id.rl_update_location:
                            //deleteLocationPopUp(view, favLocation.get(position),hideKeyboardView);
                            Log.d("name:: ", "Clicked..rl_update_location 1");
                            showPopup(view, favLocation.get(position));

                            break;
                        case R.id.linear_row_click:
                            Bundle bundle1=new Bundle();
                            Intent intentUpdate = new Intent();
                            intentUpdate.setAction(Constants.ACTION_MyUpdate);
                            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
                            bundle1.putSerializable(Constants.FAVOURITE_LOCATION,  favLocation.get(position));

                            intentUpdate.putExtras(bundle1);
                            sendBroadcast(intentUpdate);
                            finish();
                            Log.d("name:: ", "Clicked..linear_row_click");
                            break;
                    }
//send update

                }
            });
            locationListAdapter.setOnItemLongClickListener(new LocationListAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, int position) {
                    Log.d("name:: ", "Long Clicked..");

                }
            });
            locationListAdapter.notifyDataSetChanged();

        } else if (favLocation == null|| favLocation.size()==0) {

            recordnotfnd.setVisibility(View.VISIBLE);
            savedLocationRecyclerView.setVisibility(View.INVISIBLE);
        }

    }

    public void setListOnRecentLocationAdapter(final List<RecentLocation> recentLocation) {
        gridLayoutManager = new GridLayoutManager(this, 1);
        recentLocationRecycleView.setLayoutManager(gridLayoutManager);
        Log.d("name:: ", "Inside ListAdapter");
        if (recentLocation.size() != 0) {
            recordnotfnd.setVisibility(View.GONE);
            ResentLocationListAdapter resentLocationListAdapter = new ResentLocationListAdapter(this, recentLocation, 2);
            recentLocationRecycleView.setAdapter(resentLocationListAdapter);
            Log.d("name:: ", " In  Inside ListAdapter");
            resentLocationListAdapter.setOnItemClickListener(new ResentLocationListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                    RecentLocation data = recentLocation.get(position);
                    int pos = position;
                    switch (view.getId()) {
                        default:
                            break;
                    }
                }
            });
            resentLocationListAdapter.notifyDataSetChanged();

        } else if (recentLocation==null || recentLocation.size() == 0) {

            recordnotfnd.setVisibility(View.VISIBLE);
            recentLocationRecycleView.setVisibility(View.GONE);
        }


    }
/* Raju For map initilaization******************************/

    public void initialiseMap() {


    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        Log.d("apiResponseVo.code",""+apiResponseVo.code+" "+apiResponseVo.requestname);
        if (apiResponseVo.code==201&&apiResponseVo.requestname.equalsIgnoreCase(ServiceUrls.RequestNames.GET_SAVED_LOCATIONS)){
            setListOnAdapter(new ArrayList<FavLocation>());
            return;
        }
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);

            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.GET_SAVED_LOCATIONS:

                getSavedLocationVo = Common.getSpecificDataObject(apiResponseVo.data, GetSavedLocationVo.class);

                Common.Log.i("updateDeviceTokenVo.message : " + getSavedLocationVo.toString());
                setListOnAdapter(getSavedLocationVo.favLocations);
                setListOnRecentLocationAdapter(getSavedLocationVo.recentLocations);


                break;
            case ServiceUrls.RequestNames.DELETE_FAV_LOCATIONS:
                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_SAVED_LOCATIONS,null);
                Common.customToast(this, apiResponseVo.status);

                break;

        }
        }

    public void deleteLocationPopUp(View view, final FavLocation favLocation,View parentView) {
        Log.d("XYCO",""+view.getX()+ "  "+(int)view.getY()+ " parent "+parentView.getX()+ "  "+(int)parentView.getY());
        PopupMenu popup = new PopupMenu(AddLocationActivity.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_delete_location, popup.getMenu());
        MenuPopupHelper menuHelper = new MenuPopupHelper(this, (MenuBuilder) popup.getMenu(), view);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_location:
                        Bundle bundle=new Bundle();
                        Intent addlocation = new Intent(AddLocationActivity.this, AddLocationsByMapActivity.class);
                        bundle.putInt(Constants.IntentKeys.REQUEST_ACTION,2);
                        bundle.putSerializable(Constants.IntentKeys.UPDATAE_LOCATION_KEY,favLocation);
                        bundle.putSerializable(Constants.IntentKeys.GET_SAVED_LOCATION_VO,getSavedLocationVo);
                        addlocation.putExtras(bundle);
                        startActivityForResult(addlocation,LOCATION_SAVED);
                        Log.d("name:: ", "Clicked..rl_update_location 2");
                        break;
                    case R.id.delete_location:
                        HashMap<String, String> params = new HashMap<>();
                        params.put(ServiceUrls.RequestParams.FAV_ID, favLocation.id);

                        new RetrofitRequester(AddLocationActivity.this).sendStringRequest(ServiceUrls.RequestNames.DELETE_FAV_LOCATIONS, params);
                        // Toast.makeText(AddLocationActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();


                        break;
                }

                return true;
            }
        });

       // popup.show();//showing popup menu
    }

    public void showPopup(View v,final FavLocation favLocation) {
         PopupWindow popupWindow = null;

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_filter_layout, null);



        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss

            }
        });

        final PopupWindow finalPopupWindow = popupWindow;
        popupView.findViewById(R.id.delete_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalPopupWindow.dismiss();
                HashMap<String, String> params = new HashMap<>();
                params.put(ServiceUrls.RequestParams.FAV_ID, favLocation.id);

                new RetrofitRequester(AddLocationActivity.this).sendStringRequest(ServiceUrls.RequestNames.DELETE_FAV_LOCATIONS, params);

            }
        });
        popupView.findViewById(R.id.img_delete_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalPopupWindow.dismiss();
                HashMap<String, String> params = new HashMap<>();
                params.put(ServiceUrls.RequestParams.FAV_ID, favLocation.id);

                new RetrofitRequester(AddLocationActivity.this).sendStringRequest(ServiceUrls.RequestNames.DELETE_FAV_LOCATIONS, params);

            }
        });
        popupView.findViewById(R.id.edit_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalPopupWindow.dismiss();
                Bundle bundle=new Bundle();
                Intent addlocation = new Intent(AddLocationActivity.this, AddLocationsByMapActivity.class);
                bundle.putInt(Constants.IntentKeys.REQUEST_ACTION,2);
                bundle.putSerializable(Constants.IntentKeys.UPDATAE_LOCATION_KEY,favLocation);
                bundle.putSerializable(Constants.IntentKeys.GET_SAVED_LOCATION_VO,getSavedLocationVo);
                addlocation.putExtras(bundle);
                startActivityForResult(addlocation,LOCATION_SAVED);
            }
        });
        popupView.findViewById(R.id.img_edit_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalPopupWindow.dismiss();

                Bundle bundle=new Bundle();
                Intent addlocation = new Intent(AddLocationActivity.this, AddLocationsByMapActivity.class);
                bundle.putInt(Constants.IntentKeys.REQUEST_ACTION,2);
                bundle.putSerializable(Constants.IntentKeys.UPDATAE_LOCATION_KEY,favLocation);
                bundle.putSerializable(Constants.IntentKeys.GET_SAVED_LOCATION_VO,getSavedLocationVo);
                addlocation.putExtras(bundle);
                startActivityForResult(addlocation,LOCATION_SAVED);

            }
        });

        popupWindow.showAsDropDown(v, -290,-130);
    }


}
