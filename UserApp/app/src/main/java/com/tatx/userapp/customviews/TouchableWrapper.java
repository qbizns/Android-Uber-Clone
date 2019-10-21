package com.tatx.userapp.customviews;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.tatx.userapp.interfaces.GoogleMapOnTouchListener;

public class TouchableWrapper extends FrameLayout {

    private final Context context;

    public TouchableWrapper(Context context)
    {
        super(context);

        this.context = context;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        ((GoogleMapOnTouchListener)context).onGoogleMapTouch(event);


        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
//                Common.Log.i("Map Touch Action Down.");
                break;

            case MotionEvent.ACTION_UP:
//                Common.Log.i("Map Touch Action Up.");
                break;
        }
        return super.dispatchTouchEvent(event);
//        return true;
    }
}