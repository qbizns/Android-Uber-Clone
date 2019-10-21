package com.tatx.partnerapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.menuactivity.OnlineHoursActivity;
import com.tatx.partnerapp.pojos.OnlineHoursVo;
import com.tatx.partnerapp.pojos.TimeConvert;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AvgHoursFragment extends Fragment {

    @BindView(R.id.tv_online_hours)
    TextView tvOnlineHours;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    @BindView(R.id.tv_second)
    TextView tvSecond;

    public AvgHoursFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_avg_hours, container, false);
        ButterKnife.bind(this,view);
        OnlineHoursActivity onlineHoursActivity =(OnlineHoursActivity)getActivity();
        OnlineHoursVo onlineHoursVo = onlineHoursActivity.getOnlineHoursVo();
        Log.d("customViewIconText","ddfdf onlineHoursActivity"+ onlineHoursActivity +"\ndfgdfg "+onlineHoursVo);
        //   Log.d("dfgdfg",dfgdfg.toString());
        setData(Common.calculateTime(onlineHoursVo.dailyAvg));
        return view;
    }


    private void setData(TimeConvert timeConvert){



        tvOnlineHours.setText(String.valueOf(timeConvert.getHours())+"H");
        tvMinute.setText(String.valueOf(timeConvert.getMinutes()));
        tvSecond.setText(String.valueOf(timeConvert.getSeconds()));
    }

}
