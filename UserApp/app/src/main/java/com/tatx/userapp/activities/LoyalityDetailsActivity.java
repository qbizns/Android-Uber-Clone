package com.tatx.userapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Home on 18-05-2016.
 */
public class LoyalityDetailsActivity extends BaseActivity implements View.OnClickListener{
@BindView(R.id.tv_loyalty_points) TextView tvLoyaltyPoints;
@BindView(R.id.need_to_reach) TextView needToReach;
@BindView(R.id.loyaltiicon) ImageView loyaltiicon;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyality_details);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.loyalti_points));
        initialaziedAll();
    }

    public void initialaziedAll() {

        String loyalityPoints = getIntent().getStringExtra(Constants.IntentKeys.LOYALITY_POINT);
        String loyalityText = getIntent().getStringExtra(Constants.IntentKeys.LOYALITY_TEXT);
        int loyalityIcon = getIntent().getIntExtra(Constants.IntentKeys.LOYALITY_TYPE, R.drawable.ic_launcher);
        tvLoyaltyPoints.setText(loyalityPoints);
        needToReach.setText(loyalityText);
        loyaltiicon.setBackgroundResource(loyalityIcon);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }


}
