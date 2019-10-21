package com.tatx.partnerapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.menuactivity.OnlineHoursActivity;
import com.tatx.partnerapp.menuactivity.TodayHoursDetails;
import com.tatx.partnerapp.pojos.CurrentDayDetail;
import com.tatx.partnerapp.pojos.DaylyDetail;
import com.tatx.partnerapp.pojos.OnlineHoursVo;
import com.tatx.partnerapp.pojos.TimeConvert;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TodayHoursFragment extends Fragment {

    @BindView(R.id.tv_online_hours)
    TextView tvOnlineHours;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    private String date;
    public ArrayList<DaylyDetail> daylyDetailList=null;
    private OnlineHoursVo onlineHoursVo;
    private TimeConvert todayT;

    public TodayHoursFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_today_hours, container, false);
        ButterKnife.bind(this,view);
        OnlineHoursActivity onlineHoursActivity =(OnlineHoursActivity)getActivity();
        onlineHoursVo = onlineHoursActivity.getOnlineHoursVo();
        date = onlineHoursActivity.getTodayDates();
        Log.d("customViewIconText","ddfdf onlineHoursActivity"+ onlineHoursActivity +"\ndfgdfg "+onlineHoursVo);
        //   Log.d("dfgdfg",dfgdfg.toString());
        todayT=Common.calculateTime(onlineHoursVo.totalSeconds);
        setData(todayT);

        List<CurrentDayDetail> currentDayDetails = onlineHoursVo.currentDayDetails;
        daylyDetailList=new ArrayList<>();
        for (CurrentDayDetail currentDayDetail:currentDayDetails){

            DaylyDetail daylyDetail=new DaylyDetail();
            daylyDetail.day=currentDayDetail.startTime+" - "+currentDayDetail.endTime;
            daylyDetail.seconds=currentDayDetail.seconds;

            daylyDetailList.add(daylyDetail);
        }
        return view;
    }

    private void setData(TimeConvert timeConvert){

        tvOnlineHours.setText(String.valueOf(timeConvert.getHours())+"H");
        tvMinute.setText(String.valueOf(timeConvert.getMinutes()));
        tvSecond.setText(String.valueOf(timeConvert.getSeconds()));
    }


    @OnClick(R.id.image_view_clock) void setImageViewClock() {
        Log.d("daylyDetailList 11",daylyDetailList.toString());
        Intent intent = new Intent(getActivity(), TodayHoursDetails.class);
        intent.putExtra(Constants.IntentKeys.TODAY_VO, daylyDetailList);
        intent.putExtra(Constants.IntentKeys.DATE,date);
        intent.putExtra(Constants.IntentKeys.TOTAL_HOURS, onlineHoursVo.totalSeconds);
        startActivity(intent);
    }

}
