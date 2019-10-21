package com.tatx.partnerapp.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.andexert.library.RippleView;

/**
 * Created by Venkateswarlu SKP on 24-08-2016.
 */
public class CustomRippleView extends RippleView
{
    private boolean performClickEvent;

    public CustomRippleView(Context context)
    {
        super(context);
        init();
    }

    public CustomRippleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CustomRippleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }



    private void init()
    {

        setRippleType(RippleType.RECTANGLE);


        setOnRippleCompleteListener(new OnRippleCompleteListener()
        {
            @Override
            public void onComplete(RippleView rippleView)
            {
                if (performClickEvent)
                {
                    getChildAt(0).performClick();
                }

            }
        });


    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        getChildAt(0).setClickable(false);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
                performClickEvent = true;
                break;

            case MotionEvent.ACTION_DOWN:
                performClickEvent = false;
                break;

        }

        return super.onTouchEvent(event);
    }
}
