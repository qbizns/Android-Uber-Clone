package com.tatx.partnerapp.menuactivity;

import android.os.Bundle;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.GetDriverRatingVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 20-05-2016.
 */
public class Rating extends BaseActivity implements RetrofitResponseListener{
    @BindView(R.id.current_rating) TextView current_rating;
    @BindView(R.id.lifetimetrips) TextView lifetimetrips;
    @BindView(R.id.ratedtrips) TextView ratedtrips;
    @BindView(R.id.stars) TextView stars;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);

        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.rating));

        initializedAll();

    }

    private void initializedAll() {

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_DRIVER_RATING,null);


    }



    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.GET_DRIVER_RATING:

               GetDriverRatingVo getDriverRatingVo = Common.getSpecificDataObject(apiResponseVo.data, GetDriverRatingVo.class);

                current_rating.setText(getDriverRatingVo.avgrating);
                lifetimetrips.setText(getDriverRatingVo.totoaltrips);
                ratedtrips.setText(getDriverRatingVo.ratingtrips);
                stars.setText(getDriverRatingVo.fullrating);
                break;
        }
    }
}
