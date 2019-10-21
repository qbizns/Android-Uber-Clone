package com.tatx.partnerapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.menuactivity.AllOnlineHoursListActivity;
import com.tatx.partnerapp.menuactivity.OnlineHoursActivity;
import com.tatx.partnerapp.pojos.OnlineHoursVo;
import com.tatx.partnerapp.pojos.TimeConvert;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AllHoursFragment extends Fragment {
    @BindView(R.id.tv_online_hours)
    TextView tvOnlineHours;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.image_view_clock)
    ImageView imageViewClock;
    private OnlineHoursVo onlineHoursVo;
    private String dates;


    public AllHoursFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all_hours, container, false);
        ButterKnife.bind(this,view);
        OnlineHoursActivity onlineHoursActivity =(OnlineHoursActivity)getActivity();
        onlineHoursVo = onlineHoursActivity.getOnlineHoursVo();
        dates=onlineHoursActivity.getDates();

        Log.d("customViewIconText","ddfdf onlineHoursActivity"+ onlineHoursActivity +"\ndfgdfg "+onlineHoursVo);
        //   Log.d("dfgdfg",dfgdfg.toString());
        setData(Common.calculateTime(onlineHoursVo.allTime));
        return view;
    }



    private void setData(TimeConvert timeConvert){



        tvOnlineHours.setText(String.valueOf(timeConvert.getHours())+"H");
        tvMinute.setText(String.valueOf(timeConvert.getMinutes()));
        tvSecond.setText(String.valueOf(timeConvert.getSeconds()));
    }

    @OnClick(R.id.image_view_clock) void setImageViewClock(){
        Intent intent=new Intent(getActivity(), AllOnlineHoursListActivity.class);
        intent.putExtra(Constants.IntentKeys.ON_LINE_HOURS_VO,onlineHoursVo);
        intent.putExtra(Constants.IntentKeys.DATE,dates);
        startActivity(intent);
    }

}
