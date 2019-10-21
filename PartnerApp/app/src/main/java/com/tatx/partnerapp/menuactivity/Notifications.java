package com.tatx.partnerapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.activities.LearnMoreNotifiActivity;
import com.tatx.partnerapp.adapter.NotificationsListAdapter;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.NotificationsVo;
import com.tatx.partnerapp.pojos.Push;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Home on 17-05-2016.
 */
public class Notifications extends BaseActivity implements RetrofitResponseListener{

    NotificationsListAdapter notificationsListAdapter;
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.recordnotfndnotify) TextView recordnotfnd;
    @BindView(R.id.saved_location_recycler_view_notify) RecyclerView savedLocationRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.notify));
        initilaizedAll();
    }

    public void initilaizedAll() {
        HashMap<String,String>  hashMap=new HashMap();
        hashMap.put(ServiceUrls.ResponseParams.ROLE, Constants.DRIVER);
        hashMap.put(ServiceUrls.ResponseParams.TYPE, Constants.PUSH);

        new RetrofitRequester(Notifications.this).sendStringRequest(ServiceUrls.RequestNames.PUSH_NOTIFICATION,hashMap);

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

    public void setListOnAdapter(final List<Push> notificationsList) {
        gridLayoutManager = new GridLayoutManager(this, 1);
        savedLocationRecyclerView.setLayoutManager(gridLayoutManager);
        if (notificationsList.size() != 0) {
            recordnotfnd.setVisibility(View.GONE);
            notificationsListAdapter = new NotificationsListAdapter(this, notificationsList);
            savedLocationRecyclerView.setAdapter(notificationsListAdapter);
            notificationsListAdapter.setOnItemClickListener(new NotificationsListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Push pushData = notificationsList.get(position);
                    Intent intent=new Intent(Notifications.this, LearnMoreNotifiActivity.class);
                    intent.putExtra("push_data", pushData);
                    startActivity(intent);
                }
            });
            notificationsListAdapter.notifyDataSetChanged();

        } else if (notificationsList == null) {

            recordnotfnd.setVisibility(View.VISIBLE);
        }


    }

  /*  public void commonResponse(Object o) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        String response = ((RestResponse) o).getCode();
        Log.d("params", "response:" + response + o.toString());
        if (response.equalsIgnoreCase("200")) {
            switch (((RestResponse) o).getRequestname()) {
                case Constants.offers:
                    Object data = ((RestResponse) o).getData();
                    try {
                        Gson gson = new Gson();
                        String data1 = gson.toJson(data);
                        JSONObject jsonObject = new JSONObject(data1);
                        JSONArray jsonArray = jsonObject.getJSONArray("offers");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            NotificationsPojo notificationsPojo=new NotificationsPojo();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            notificationsPojo.setId(jsonObject1.getString("id"));
                            notificationsPojo.setMessage_type(jsonObject1.getString("message_type"));
                            notificationsPojo.setMessage(jsonObject1.getString("message"));
                            notificationsPojo.setSend_to_roles(jsonObject1.getString("send_to_roles"));
                            notificationsList.add(notificationsPojo);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Toast.makeText(this, ((RestResponse)o).getData().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, ((RestResponse) o).getStatus().toString(), Toast.LENGTH_SHORT).show();
                    setListOnAdapter(notificationsList);
                    break;

            }
        } else {
            setListOnAdapter(notificationsList);
            Common.customToast(this, ((RestResponse) o).getStatus(), Common.TOAST_TIME);
        }
        Log.d("notificationsList", notificationsList.toString());
    }

*/




    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {

        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.PUSH_NOTIFICATION:
                NotificationsVo notificationsVo=Common.getSpecificDataObject(apiResponseVo.data,NotificationsVo.class);
                setListOnAdapter(notificationsVo.push);
                Common.customToast(this, apiResponseVo.status);


                break;
        }
    }
}
