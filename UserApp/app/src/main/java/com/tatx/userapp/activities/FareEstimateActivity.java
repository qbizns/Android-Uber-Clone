package com.tatx.userapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.pojos.GetFareVo;

import butterknife.ButterKnife;

/**
 * Created by user on 24-05-2016.
 */
public class FareEstimateActivity extends BaseActivity {
    private TextView source;
    private TextView destination;
    private TextView amountTv;
    TextView etcredit;
    TextView vehiclemodelfare_estimate;
    TextView faresar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fare_estimate);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.farestimate));
        initializedAll();


        GetFareVo getFareVo = (GetFareVo) getIntent().getSerializableExtra(Constants.KEY_1);


        source.setText(getIntent().getStringExtra(Constants.IntentKeys.SOURCE_ADDRESS));

        destination.setText(getIntent().getStringExtra(Constants.IntentKeys.DESTINATION_ADDRESS));

        faresar.setText(getFareVo.fare.get(0).currency);

        amountTv.setText(String.valueOf(getFareVo.fare.get(0).min)+" - "+String.valueOf(getFareVo.fare.get(0).max));

        //vehiclemodelfare_estimate.setText(CabTypes.sliderDetails(getFareVo.fare.get(0).typeId).cabType);



    }
    public void initializedAll(){
        source=(TextView)findViewById(R.id.tv_source_address_to_show);
        destination=(TextView)findViewById(R.id.tv_destination_address_to_show);
        faresar=(TextView)findViewById(R.id.faresar);
        amountTv=(TextView)findViewById(R.id.amount);
        etcredit=(TextView) findViewById(R.id.etcredit);
        vehiclemodelfare_estimate=(TextView) findViewById(R.id.vehiclemodelfare_estimate);


    }




}
