package com.tatx.userapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.CircleImageView;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.Push;
import com.tatx.userapp.pojos.UpdateReadStatusVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Home on 24-09-2016.
 */
public class LearnMoreNotifiActivity extends BaseActivity implements RetrofitResponseListener {
    @BindView(R.id.tv_description)
    TextView descriptionTv;
    @BindView(R.id.notify_text)
    TextView titleTv;
    @BindView(R.id.notify_img)
    CircleImageView notifyImg;
    private Push push;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_about_notification);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.learn_more));
        push =(Push)getIntent().getSerializableExtra("push_data");
        Picasso.with(this).load(push.image).into(notifyImg);
        titleTv.setText(push.messageTitle);
        descriptionTv.setText(push.message);

        HashMap<String, String> requestParams = new HashMap<>();

        requestParams.put(ServiceUrls.RequestParams.NOTIFICATION_ID, String.valueOf(push.id));


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.UPDATE_READ_STATUS,requestParams);

    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {


        if(apiResponseVo.code != Constants.SUCCESS)
        {

            switch (apiResponseVo.requestname)
            {
                default:
                    Common.customToast(this,apiResponseVo.status);
                    break;
            }


            return;


        }



        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.UPDATE_READ_STATUS:

                UpdateReadStatusVo updateReadStatusVo = Common.getSpecificDataObject(apiResponseVo.data, UpdateReadStatusVo.class);

                Common.sendUnReadNotificationCountBrodCast(this,updateReadStatusVo.unreadNotificationsCount);

                break;


        }






    }



}
