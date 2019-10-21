package com.tatx.partnerapp.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.tatx.partnerapp.commonutills.Constants;

/**
 * Created by Venkateswarlu SKP on 30-08-2016.
 */
public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {

        Typeface externalFont=Typeface.createFromAsset(getContext().getAssets(), Constants.CUSTOM_FONT_PATH);
        setTypeface(externalFont);
        setAllCaps(true);

    }


}
