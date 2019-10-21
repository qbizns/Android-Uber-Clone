package com.tatx.userapp.abstractclasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.commonutills.Common;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Venkateswarlu SKP on 09-09-2016.
 */


public abstract class BaseActivity extends AppCompatActivity
{


    @Nullable
    @BindView(R.id.tv_title_text) TextView tvTitleText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);




    }


    @Optional
    @OnClick(R.id.ll_title_back)
    protected void onTitleBackPressed()
    {
        Common.Log.i("? - Title Back Clicked.");
        finish();
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
