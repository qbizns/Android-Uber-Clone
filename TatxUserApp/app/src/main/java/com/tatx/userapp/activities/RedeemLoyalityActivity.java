package com.tatx.userapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Home on 18-05-2016.
 */
public class RedeemLoyalityActivity extends BaseActivity implements RetrofitResponseListener{


    private ProgressDialog progressDialog;

    @BindView(R.id.et_points_to_redeem) EditText etPointsToRedeem;
    @BindView(R.id.tv_title_text) TextView registration;
    @BindView(R.id.tv_redeem_points) TextView tv_redeem_points;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_loyality);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.redeem_loyalty));

    }



           @OnClick( R.id.tv_redeem_points) void setEtPointsToRedeem(){
               if (isValidData())
               {
                   callRedeemLoyalityAPI();
               }
           }



    private boolean isValidData()
    {


        if(TextUtils.isEmpty(etPointsToRedeem.getText()))
        {
            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_points_to_redeem));
            return false;
        }

        return true;
    }



    private void callRedeemLoyalityAPI()
    {


        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put(ServiceUrls.RequestParams.POINTS, etPointsToRedeem.getText().toString());

        new RetrofitRequester(RedeemLoyalityActivity.this).sendStringRequest(ServiceUrls.RequestNames.REDEEM_LOYALTY,hashMap);


    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {

        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.REDEEM_LOYALTY:

                Common.customToast(this, apiResponseVo.status);
                break;
        }

    }
}
