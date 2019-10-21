package com.tatx.userapp.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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

/**
 * Created by Home on 17-05-2016.
 */
public class FeedbackActivity extends BaseActivity implements RetrofitResponseListener{

    @BindView(R.id.feedbackform) EditText feedbackform;
    @BindView(R.id.submit) Button submit;
    @BindView(R.id.hide_keyboard_layout) LinearLayout hideKeyboardView;
    private String tripId="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.feedback));


        initilaizedAll();
    }

    public void initilaizedAll() {

        tripId=getIntent().getStringExtra(Constants.IntentKeys.TRIP_ID);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideSoftKeyboard(FeedbackActivity.this);
                if (feedbackform.length()>4){
                    sendfeedbackApi(feedbackform.getText().toString().trim(),tripId);
                }else {
                    Common.customToast(FeedbackActivity.this,Common.getStringResourceText(R.string.please_enter_minimum_5_character));
                }

            }
        });
        hideKeyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Common.hideSoftKeyboard(FeedbackActivity.this);
                return true;
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



    public void sendfeedbackApi(String message,String trip_id) {

        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(ServiceUrls.RequestParams.MESSAGE, message);
        hashMap.put(ServiceUrls.RequestParams.TYPE, Constants.CONTACT_US);
        hashMap.put(ServiceUrls.RequestParams.TRIP_ID, trip_id);
        hashMap.put(ServiceUrls.RequestParams.ROLE, Constants.CUSTOMER);

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.FEEDBACK, hashMap);

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
