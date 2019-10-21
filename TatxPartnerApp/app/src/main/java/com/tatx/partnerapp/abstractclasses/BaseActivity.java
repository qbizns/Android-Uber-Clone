package com.tatx.partnerapp.abstractclasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Home on 23-08-2016.
 */
public abstract class BaseActivity extends AppCompatActivity
{

    @Nullable
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;

    /*@Override
    protected void attachBaseContext(Context newBase)
    {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        super.onCreate(savedInstanceState);

//        ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "SKP").acquire();





    }

    @Optional
    @OnClick(R.id.ll_title_back)
    protected void onTitleBackPressed()
    {
        Common.Log.i("? - Title Back Clicked.");
        finish();
    }

    @Override
    protected void onStart()
    {

        super.onStart();

//        getWindow().getDecorView().setKeepScreenOn(true);


    }

    public void setTitleText(String titleText)
    {
        tvTitleText.setText(titleText);
    }


    @Override
    protected void onResume()
    {

        if (!isDisableHideKeyBord())
        {
            Common.setupUI(this,findViewById(android.R.id.content));
        }

        super.onResume();
    }



    public void setDisableHideKeyBoard(boolean disableHideKeyBord) {
        this.disableHideKeyBord = disableHideKeyBord;
    }

    public boolean isDisableHideKeyBord() {
        return disableHideKeyBord;
    }

    boolean disableHideKeyBord;




}
