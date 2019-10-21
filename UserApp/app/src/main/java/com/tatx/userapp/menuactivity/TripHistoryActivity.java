package com.tatx.userapp.menuactivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.adapter.TripHistoryListAdapter;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.dataset.RestResponse;
import com.tatx.userapp.pojos.Order;
import com.tatx.userapp.pojos.Trip;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.TripHistoryVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Home on 17-05-2016.
 */

public class TripHistoryActivity extends BaseActivity implements RetrofitResponseListener {

    TripHistoryListAdapter tripHistoryListAdapter;
    GridLayoutManager gridLayoutManager;
    private Dialog progressDialog;
    List<Trip> historyList = new ArrayList<>();

    @BindView(R.id.recordnotfnd)
    TextView recordnotfnd;
    @BindView(R.id.saved_location_recycler_view_triphistory)
    RecyclerView savedLocationRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trip_history);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.trip_history));

        getTripHistoryApi();

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void setListOnAdapter(final List<Trip> historyList) {

        Common.Log.i("? historyList : " + historyList);
        Common.Log.i("? historyList.size() : " + historyList.size());

        gridLayoutManager = new GridLayoutManager(this, 1);
        savedLocationRecyclerView.setLayoutManager(gridLayoutManager);
        if (historyList.size() != 0) {
            recordnotfnd.setVisibility(View.GONE);
            tripHistoryListAdapter = new TripHistoryListAdapter(this, historyList);
            savedLocationRecyclerView.setAdapter(tripHistoryListAdapter);
            tripHistoryListAdapter.setOnItemClickListener(new TripHistoryListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    // view.findViewById(R.id.mapImage);

                    Trip data = historyList.get(position);
                    Intent i = new Intent(getApplicationContext(), TripDetailsActivity.class);
                     bundle.putSerializable("triphistory", data);
                    i.putExtra("bundle", bundle);
                    startActivity(i);
                }
            });
            tripHistoryListAdapter.notifyDataSetChanged();

        } else if (historyList == null || historyList.size() == 0) {

            recordnotfnd.setVisibility(View.VISIBLE);
        }


    }




    public void getTripHistoryApi() {
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.TRIP_CUSTOMER, null);
    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS) {

            Common.customToast(this, apiResponseVo.status);

            return;


        }


        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.TRIP_CUSTOMER:
                Common.customToast(this, apiResponseVo.status);
                TripHistoryVo tripHistoryVo = Common.getSpecificDataObject(apiResponseVo.data, TripHistoryVo.class);
                // tripHistoryVo.orders
                for (Order order : tripHistoryVo.orders) {
                    for (Trip trip : order.trip) {
                        historyList.add(trip);
                    }
                }
                setListOnAdapter(historyList);
                break;
        }
    }
}
