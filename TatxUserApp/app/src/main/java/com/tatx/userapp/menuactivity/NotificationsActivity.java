package com.tatx.userapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.activities.LearnMoreNotifiActivity;
import com.tatx.userapp.adapter.NotificationsListAdapter;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.Push;
import com.tatx.userapp.pojos.PushNotificationData;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Home on 17-05-2016.
 */
public class NotificationsActivity extends BaseActivity implements RetrofitResponseListener {

    NotificationsListAdapter notificationsListAdapter;
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.recordnotfndnotify) TextView recordnotfnd;
    @BindView(R.id.saved_location_recycler_view_notify) RecyclerView savedLocationRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.notifications));
        initilaizedAll();
    }

    public void initilaizedAll() {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(ServiceUrls.RequestParams.ROLE, "customer");
        hashMap.put(ServiceUrls.RequestParams.TYPE, "push");

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.PUSH_NOTIFICATION,hashMap);

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

    public void setListOnAdapter(final List<Push> push) {
        gridLayoutManager = new GridLayoutManager(this, 1);
        savedLocationRecyclerView.setLayoutManager(gridLayoutManager);
        if (push.size() != 0) {
            recordnotfnd.setVisibility(View.GONE);
            notificationsListAdapter = new NotificationsListAdapter(this, push);
            savedLocationRecyclerView.setAdapter(notificationsListAdapter);
            notificationsListAdapter.setOnItemClickListener(new NotificationsListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                    Push pushData = push.get(position);
                    Intent intent=new Intent(NotificationsActivity.this, LearnMoreNotifiActivity.class);
                    intent.putExtra("push_data",  pushData);
                    startActivity(intent);


                }
            });
            notificationsListAdapter.notifyDataSetChanged();
        } else if (push == null||push.size() == 0) {
            recordnotfnd.setVisibility(View.VISIBLE);
        }
    }





    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {

        if (apiResponseVo.code != Constants.SUCCESS)
        {

            Common.customToast(this, apiResponseVo.status);

            switch(apiResponseVo.requestname)
            {
                case ServiceUrls.RequestNames.PUSH_NOTIFICATION:
                    finish();
                    break;
            }




            return;


        }



        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.PUSH_NOTIFICATION:
                PushNotificationData pushNotificationData = Common.getSpecificDataObject(apiResponseVo.data, PushNotificationData.class);
                Log.d("list",pushNotificationData.push.toString());
                setListOnAdapter(pushNotificationData.push);
                break;
        }



    }

}

