package com.tatx.partnerapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.AllOnlineHoursListAdapter;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.pojos.CurrentDayDetail;
import com.tatx.partnerapp.pojos.DaylyDetail;
import com.tatx.partnerapp.pojos.OnlineHoursVo;
import com.tatx.partnerapp.pojos.TimeConvert;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Home on 12-12-2016.
 */
public class AllOnlineHoursListActivity extends BaseActivity{

    @BindView (R.id.recycler_view_online_hours) RecyclerView recyclerViewOnlineHours;
    @BindView (R.id.tv_today_date)
    TextView tvDates;
    @BindView (R.id.tv_total_hours)
    TextView tvTotalHours;
    private ArrayList<DaylyDetail> daylyDetailList=new ArrayList<>();
    private OnlineHoursVo onlineHoursVo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onlineHoursVo = (OnlineHoursVo)getIntent().getSerializableExtra(Constants.IntentKeys.ON_LINE_HOURS_VO);

        setContentView(R.layout.activity_online_hours_list);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.online_hours));

        initilaiedAll();

    }

    private void initilaiedAll() {

        String dates = getIntent().getStringExtra(Constants.IntentKeys.DATE);
        tvDates.setText(dates);
        setListOnAdapter(onlineHoursVo.dailyDetails);
        TimeConvert totalTime = Common.calculateTime(onlineHoursVo.allTime);
        tvTotalHours.setText(totalTime.getHours()+":"+totalTime.getMinutes()+":"+totalTime.getSeconds());
        List<CurrentDayDetail> currentDayDetails = onlineHoursVo.currentDayDetails;

        for (CurrentDayDetail currentDayDetail:currentDayDetails){

            DaylyDetail daylyDetail=new DaylyDetail();
            daylyDetail.day=currentDayDetail.startTime+" - "+currentDayDetail.endTime;
            daylyDetail.seconds=currentDayDetail.seconds;

            daylyDetailList.add(daylyDetail);
        }
    }

    public void setListOnAdapter(final List<DaylyDetail> daylyDetails) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerViewOnlineHours.setLayoutManager(gridLayoutManager);
        if (daylyDetails.size() != 0) {
            //recordnotfnd.setVisibility(View.GONE);
            AllOnlineHoursListAdapter allOnlineHoursListAdapter = new AllOnlineHoursListAdapter(this, daylyDetails);
            recyclerViewOnlineHours.setAdapter(allOnlineHoursListAdapter);
           allOnlineHoursListAdapter.setOnItemClickListener(new AllOnlineHoursListAdapter.OnItemClickListener() {
               @Override
               public void onItemClick(View view, int position) {
                   Intent intent=new Intent(AllOnlineHoursListActivity.this, TodayHoursDetails.class);
                   intent.putExtra(Constants.IntentKeys.DATE,daylyDetails.get(position).day);
                   intent.putExtra(Constants.IntentKeys.TODAY_VO, daylyDetailList);
                   intent.putExtra(Constants.IntentKeys.TOTAL_HOURS, daylyDetails.get(position).seconds);
                   startActivity(intent);
               }
           });
            allOnlineHoursListAdapter.notifyDataSetChanged();

        } else if (daylyDetails == null) {

           // recordnotfnd.setVisibility(View.VISIBLE);
        }


    }
}
