package com.tatx.partnerapp.abstractclasses;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Constants;

public abstract class DemoBase extends BaseActivity {

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        mTfRegular = Typeface.createFromAsset(getAssets(), Constants.CUSTOM_FONT_PATH);

        mTfLight = Typeface.createFromAsset(getAssets(), Constants.CUSTOM_FONT_PATH);


    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}