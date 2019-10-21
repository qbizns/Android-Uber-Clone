package com.tatx.userapp.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Home on 19-09-2016.
 */
public class CallMessageActivity extends BaseActivity {
    @BindView(R.id.call_layout)
    LinearLayout callLayout;
    @BindView(R.id.message_layout)
    LinearLayout messageLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms);
        ButterKnife.bind(this);
        //setTitleText(Common.getStringResourceText(R.string.popup));
    }

    @OnClick (R.id.call_layout)
    void setCallLayout(){
        try {
            Intent intent1 = new Intent(Intent.ACTION_CALL);
            intent1.setData(Uri.parse("tel:+" + String.valueOf(getIntent().getStringExtra(Constants.MOBILE))));
            startActivity(intent1);

            finish();

        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    @OnClick (R.id.message_layout)
    void setMessageLayout(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + String.valueOf(getIntent().getStringExtra(Constants.MOBILE))));
        intent.putExtra("sms_body", "");
        startActivity(intent);
        finish();

    }
}
