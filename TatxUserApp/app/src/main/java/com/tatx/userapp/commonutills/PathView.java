package com.tatx.userapp.commonutills;

/**
 * Created by Home on 23-11-2016.
 */

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tatx.userapp.R;

public class PathView extends View
{
    Path path;
    Paint paint;
    float length;

    public PathView(Context context)
    {
        super(context);
        init();
    }

    public PathView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
        path.moveTo(((int)getScreenWidth()/2), getResources().getDimension(R.dimen._98sdp));
        path.lineTo(((int)getScreenWidth()/2), (getScreenHeight()/2)-60);
//        path.lineTo(200, 500);
//        path.lineTo(200, 300);
//        path.lineTo(350, 300);

        // Measure the path
        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};

        ObjectAnimator animator = ObjectAnimator.ofFloat(PathView.this, "phase", 1.0f,0.0f);
        animator.setDuration(1000);
        animator.start();
    }

    //is called by animtor object
    public void setPhase(float phase)
    {
        Log.d("pathview","setPhase called with:" + String.valueOf(phase));
        paint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();//will calll onDraw
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset)
    {
        return new DashPathEffect(new float[] { pathLength, pathLength },
                Math.max(phase * pathLength, offset));
    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path, paint);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}