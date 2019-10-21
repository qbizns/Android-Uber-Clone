package com.tatx.partnerapp.menuactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.AllOnlineHoursListAdapter;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.DayDetail;
import com.tatx.partnerapp.pojos.DayDetailsVo;
import com.tatx.partnerapp.pojos.DaylyDetail;
import com.tatx.partnerapp.pojos.TimeConvert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Home on 12-12-2016.
 */
public class TodayHoursDetails extends BaseActivity implements RetrofitResponseListener{

    private DayDetailsVo dayDetailsVo;
    @BindView(R.id.recycler_view_online_hours)
    RecyclerView recyclerViewOnlineHours;
    @BindView (R.id.tv_today_date)
    TextView tvDate;
    @BindView (R.id.tv_total_hours)
    TextView tvTotalHours;

    public ArrayList<DaylyDetail> daylyDetailList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_hours_details);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.online_hours));

        initilaiedAll();
    }

    private void initilaiedAll() {

        String todayDate = getCurrentdate();

        String date = getIntent().getStringExtra(Constants.IntentKeys.DATE);
        long totalTimeLong = getIntent().getLongExtra(Constants.IntentKeys.TOTAL_HOURS,0);
        TimeConvert tTime = Common.calculateTime(totalTimeLong);
        if (date.equalsIgnoreCase(todayDate)){
            daylyDetailList= (ArrayList<DaylyDetail>) getIntent().getSerializableExtra(Constants.IntentKeys.TODAY_VO);
           // String totalH = getIntent().getStringExtra(Constants.IntentKeys.TOTAL_HOURS);
            Log.d("daylyDetailList",daylyDetailList.toString());
            setListOnAdapter(daylyDetailList);
        }

        else {
            getOnlieHoursRequest(date);
        }
        tvDate.setText(date);
        tvTotalHours.setText(tTime.getHours()+":"+tTime.getMinutes()+":"+tTime.getSeconds());
    }


        public String getCurrentdate() {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 0);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            month=month+1;
            String date = year + "-" + month + "-" + day;

            return date;

    }

    private void getOnlieHoursRequest(String date){
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(ServiceUrls.ApiRequestParams.DATE, date);
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_ONLINE_HOURS_BY_DATE, hashMap);
    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        Log.d("requestId", String.valueOf(requestId));
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.GET_ONLINE_HOURS_BY_DATE:
                List<DaylyDetail> daylyDetailList=new ArrayList<>();
                dayDetailsVo = Common.getSpecificDataObject(apiResponseVo.data, DayDetailsVo.class);
                List<DayDetail> dayaDeatils = dayDetailsVo.currentDayDetails;
                for (DayDetail dayDetail:dayaDeatils){
                    DaylyDetail daylyDetail=new DaylyDetail();
                    daylyDetail.day=dayDetail.startTime+" - "+dayDetail.endTime;
                    daylyDetail.seconds=dayDetail.seconds;

                    daylyDetailList.add(daylyDetail);
                }

                setListOnAdapter(daylyDetailList);
                break;
        }
    }

    public void setListOnAdapter(final List<DaylyDetail> daylyDetails) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerViewOnlineHours.setLayoutManager(gridLayoutManager);
        if (daylyDetails != null &&daylyDetails.size()>0) {
            //recordnotfnd.setVisibility(View.GONE);
            AllOnlineHoursListAdapter allOnlineHoursListAdapter = new AllOnlineHoursListAdapter(this, daylyDetails);
            recyclerViewOnlineHours.setAdapter(allOnlineHoursListAdapter);
            allOnlineHoursListAdapter.setOnItemClickListener(new AllOnlineHoursListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                    Intent intent=new Intent(TodayHoursDetails.this, TodayHoursDetails.class);
//                    startActivity(intent);
                }
            });
            allOnlineHoursListAdapter.notifyDataSetChanged();

        } else if (daylyDetails == null) {

            // recordnotfnd.setVisibility(View.VISIBLE);
        }


    }
}
