package com.tatx.userapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tatx.userapp.R;

import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.activities.CreditBalTransHistory;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.ShareAndEarnVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 01-05-2016.
 */
public class ShareAndEarn extends BaseActivity implements View.OnClickListener,RetrofitResponseListener {

    @BindView(R.id.share) Button share;
    @BindView(R.id.copy) Button copy;
    @BindView (R.id.shareurcode) TextView shareCode;
    @BindView(R.id.share_amount) TextView shareAmount;
    private String shareText;

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shareandearn);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.shareearn));

        initialiseAll();

    }

    public void initialiseAll()
    {

        share.setOnClickListener(this);

        copy.setOnClickListener(this);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.SHARE,null);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.share:
                 Share();
                break;
            case R.id.copy:
//                Common.copy(shareCode,this);
                Common.copy(shareText,this);
                break;
            case R.id.sar_history:
                Intent history = new Intent(this, CreditBalTransHistory.class);
                startActivity(history);
                break;

        }
    }

    private void Share()
    {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Common.getStringResourceText(R.string.share_and_earn));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(sharingIntent, Common.getStringResourceText(R.string.share_via)));
    }



    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);

            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.SHARE:

                ShareAndEarnVo shareAndEarnVo = Common.getSpecificDataObject(apiResponseVo.data, ShareAndEarnVo.class);
                shareText = Common.getStringResourceText(R.string.download_now)+"\n\n"+Constants.PLAY_STORE_URL;
                shareCode.setText(shareAndEarnVo.promoCode);
                if (shareAndEarnVo.shareAmount>0){
                    shareAmount.setVisibility(View.VISIBLE);
                    shareAmount.setText(shareAndEarnVo.textShare);
                    shareText = Common.getStringResourceText(R.string.download_now_and_use_promo_code)+shareAndEarnVo.promoCode.toUpperCase()+Common.getStringResourceText(R.string._to_get )+" "+shareAndEarnVo.currencyCode+" "+shareAndEarnVo.shareAmount+" "+Common.getStringResourceText(R.string.rides_free)+"\n"+Constants.PLAY_STORE_URL;
                }

                Common.Log.i("updateDeviceTokenVo.message : " + shareAndEarnVo.toString());
                Common.Log.i("shareText : "+shareText);

                break;


        }
    }
}
