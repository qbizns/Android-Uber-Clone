package com.tatx.partnerapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.ViewAllReferrals;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.GetShareAndEarnDetailsVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by user on 20-05-2016.
 */
public class InviteAFriend extends BaseActivity implements RetrofitResponseListener
{

    @BindView(R.id.tv_referrall_code) TextView tvReferrallCode;

    @BindView(R.id.tv_share_code_get_amount) TextView tvShareCodeGetAmount;



    private GetShareAndEarnDetailsVo getShareAndEarnDetailsVo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friend);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.invite));
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_SHARE_AND_EARN_DETAILS, null);

    }


    @OnClick(R.id.ll_view_all_referrals)
    void viewAllReferrals()
    {
        Intent intent = new Intent(this, ViewAllReferrals.class);

        intent.putExtra(Constants.IntentKeys.SHARE_AND_EARN_VO, getShareAndEarnDetailsVo);

        startActivity(intent);

    }


    @Optional
    @OnClick({R.id.ll_copy, R.id.ll_referral_code})
    void copyReferralCode(View view)
    {
        Common.copy(tvReferrallCode.getText().toString(),this);
    }




    @OnClick(R.id.ll_share)
    void shareReferralCode()
    {

        String shareText = Common.getStringResourceText(R.string.download_now) + " \n" + Constants.PLAY_STORE_URL;

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Common.getStringResourceText(R.string.share_and_earn));

        if (getShareAndEarnDetailsVo.referAmount>0) {

           shareText = Common.getStringResourceText(R.string.download_now_and_use_referral_code) + tvReferrallCode.getText().toString() + Common.getStringResourceText(R.string._to_get) + " " + getShareAndEarnDetailsVo.referAmount + " " + getShareAndEarnDetailsVo.currencyCode + " \n" + Constants.PLAY_STORE_URL + "\n\n" + ServiceUrls.CURRENT_ENVIRONMENT.getApiUrl() + "/registerDriver?reC=" + Common.base64Encode(tvReferrallCode.getText().toString());

        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(sharingIntent, Common.getStringResourceText(R.string.share_via)));


    }






    private void setData(GetShareAndEarnDetailsVo getShareAndEarnDetailsVo)
    {
        tvReferrallCode.setText(getShareAndEarnDetailsVo.referralCode.toUpperCase());

        tvShareCodeGetAmount.setText(getShareAndEarnDetailsVo.textShare);

       /* if (getShareAndEarnDetailsVo.referAmount>0)
        {

            tvShareCodeGetAmount.setText(getShareAndEarnDetailsVo.textShare);

        }*/


    }



    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        Log.d("requestId", String.valueOf(requestId));
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.GET_SHARE_AND_EARN_DETAILS:
                getShareAndEarnDetailsVo = Common.getSpecificDataObject(apiResponseVo.data, GetShareAndEarnDetailsVo.class);
                setData(getShareAndEarnDetailsVo);

                break;

        }
        }
}
