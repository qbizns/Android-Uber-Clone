package com.tatx.partnerapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 01-06-2016.
 */
public class FeedbackFormActivity extends BaseActivity implements RetrofitResponseListener{

    @BindView(R.id.contactinfo) TextView contactinfo;
    @BindView(R.id.emailinfo) TextView emailinfo;
    @BindView(R.id.postboxinfo) TextView postboxinfo;
    @BindView(R.id.feedbackform) EditText feedbackform;
    @BindView(R.id.submit) Button submit;
    private String tripId="0";
    private String feedbackType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.feedbackform));

        tripId=getIntent().getStringExtra(Constants.IntentKeys.TRIP_ID);
        feedbackType=getIntent().getStringExtra(Constants.IntentKeys.FEEDBACK_TYPE);


    }



   @OnClick(R.id.submit) void setSubmit(){
       if (feedbackform.length()>5){

           sendfeedbackApi(feedbackform.getText().toString().trim(),tripId);
       }else
       {
           Common.customToast(this,Common.getStringResourceText(R.string.please_enter_minimum_5_characters));
       }
   }

    public void sendfeedbackApi(String message,String trip_id) {

        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(ServiceUrls.ApiRequestParams.MESSAGE, message);
        hashMap.put(ServiceUrls.ApiRequestParams.TYPE, feedbackType);
        hashMap.put(ServiceUrls.ApiRequestParams.TRIP_ID, trip_id);
        hashMap.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.FEEDBACK,hashMap);

    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {

        if (apiResponseVo.code != Constants.SUCCESS)
        {

            Common.customToast(this, apiResponseVo.status);
            return;

        }


        switch (apiResponseVo.requestname) {
            case ServiceUrls.RequestNames.FEEDBACK:

              Common.customToast(this, apiResponseVo.status);
                finish();


                break;
        }
    }
}