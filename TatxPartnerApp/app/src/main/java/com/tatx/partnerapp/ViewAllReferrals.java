package com.tatx.partnerapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.ViewAllReferralsListAdapter;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.customviews.CustomTextView;
import com.tatx.partnerapp.pojos.AllReferral;
import com.tatx.partnerapp.pojos.GetShareAndEarnDetailsVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllReferrals extends BaseActivity
{

    @BindView(R.id.rv_referral) RecyclerView rvReferral;

    @BindView(R.id.ctv_record_not_found) CustomTextView ctvRecordNotFound;

    @BindView(R.id.tv_total_all_referral_amount) TextView tvTotalAllReferralAmount;

    @BindView(R.id.ll_total_all_referral_amount) LinearLayout llTotalAllReferralAmount;


    private ArrayList<AllReferral> allReferral=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all_referrals);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.view_all_referrals));

        initilaiedAll();

    }

    private void initilaiedAll()
    {
        GetShareAndEarnDetailsVo getShareAndEarnDetailsVo=(GetShareAndEarnDetailsVo)getIntent().getSerializableExtra(Constants.IntentKeys.SHARE_AND_EARN_VO);

        Common.Log.i("? - getShareAndEarnDetailsVo : "+getShareAndEarnDetailsVo);

        setListOnAdapter(getShareAndEarnDetailsVo);


        tvTotalAllReferralAmount.setText(getShareAndEarnDetailsVo.totalAllReferralAmount+" "+getShareAndEarnDetailsVo.currencyCode);


    }


    public void setListOnAdapter(GetShareAndEarnDetailsVo getShareAndEarnDetailsVo)
    {

        rvReferral.setLayoutManager(new LinearLayoutManager(this));

        Common.Log.i("? - allReferrals : "+getShareAndEarnDetailsVo.allReferral);

        if (getShareAndEarnDetailsVo.allReferral.size() != 0)
        {
            ctvRecordNotFound.setVisibility(View.GONE);

            llTotalAllReferralAmount.setVisibility(View.VISIBLE);


            rvReferral.setAdapter(new ViewAllReferralsListAdapter(this, getShareAndEarnDetailsVo.allReferral, getShareAndEarnDetailsVo.currencyCode));

            Common.Log.i("? - inside if allReferrals : "+getShareAndEarnDetailsVo.allReferral);

        }
        else
        {

            ctvRecordNotFound.setVisibility(View.VISIBLE);

            llTotalAllReferralAmount.setVisibility(View.GONE);



            Common.Log.i("? - inside else allReferrals : "+getShareAndEarnDetailsVo.allReferral);

        }


    }
}
