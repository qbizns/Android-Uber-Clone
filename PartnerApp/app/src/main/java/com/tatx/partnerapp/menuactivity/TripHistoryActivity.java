package com.tatx.partnerapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.TripHistoryListAdapter;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.Order;
import com.tatx.partnerapp.pojos.Trip;
import com.tatx.partnerapp.pojos.TripDriverVo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Home on 17-05-2016.
 */
public class TripHistoryActivity extends BaseActivity implements RetrofitResponseListener{

    TripHistoryListAdapter tripHistoryListAdapter;
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.recordnotfnd) TextView recordnotfnd;
    @BindView(R.id.saved_location_recycler_view_triphistory) RecyclerView savedLocationRecyclerView;

    List<Trip> historyList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.trip_history));
        initilaizedAll();
    }
    public void initilaizedAll() {

        new RetrofitRequester(TripHistoryActivity.this).sendStringRequest(ServiceUrls.RequestNames.TRIP_DRIVER,null);

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
        gridLayoutManager = new GridLayoutManager(this, 1);
        savedLocationRecyclerView.setLayoutManager(gridLayoutManager);
        if (historyList.size() != 0) {
            recordnotfnd.setVisibility(View.GONE);
            tripHistoryListAdapter = new TripHistoryListAdapter(this, historyList);
            savedLocationRecyclerView.setAdapter(tripHistoryListAdapter);
            tripHistoryListAdapter.setOnItemClickListener(new TripHistoryListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position)
                {

                    Trip trip = historyList.get(position);

                    Intent intent =new Intent(getApplicationContext(),TripDetailsActivity.class);

                    intent.putExtra(Constants.KEY_1,trip);

                    startActivity(intent);

                }

            });
            tripHistoryListAdapter.notifyDataSetChanged();

        } else if (historyList == null) {

            recordnotfnd.setVisibility(View.VISIBLE);
        }


    }




    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.TRIP_DRIVER:

                TripDriverVo tripDriverVo = Common.getSpecificDataObject(apiResponseVo.data, TripDriverVo.class);

                List<Order> orderList = tripDriverVo.orders;

                Common.Log.i("TripHistory - orderList.size() : "+orderList.size());

                for (Order order:orderList)
                {

                    historyList.addAll(order.trip);


                }


                Common.Log.i("TripHistory - historyList.size() : "+historyList.size());

                Toast.makeText(this, apiResponseVo.status, Toast.LENGTH_SHORT).show();
                setListOnAdapter(historyList);
                break;
        }
    }
}
