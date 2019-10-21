package com.tatx.userapp.commonutills;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Home on 23-11-2016.
 */
public class Circle   extends View {
    private Paint myPaint;
    private Paint myPaint2;
    private Paint myFramePaint;
    private RectF bigOval;
    public TextView value;
    private RectF bigOval2;
    private float myStart;
    private float mySweep;
    private float SWEEP_INC = 8;
    private float SWEEP_INC2 = 5;
    // Use this flag to control the direction of the arc's movement
    private boolean addToCircle = true;

    public Circle(Context context) {
        super(context);
        init();
    }

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);



        init();
    }

    public void init() {
        myPaint = new Paint();

        myPaint.setAntiAlias(true);

        myPaint.setStyle(Paint.Style.STROKE);

        myPaint.setColor(Color.WHITE);

        myPaint.setStrokeWidth(2);
        myPaint.setTextAlign(Paint.Align.CENTER);

//        bigOval = new RectF(280, 5, 400, 125);
        bigOval = new RectF((getScreenWidth()/2)-60, (getScreenHeight()/2)-60, (getScreenWidth()/2)+60, (getScreenHeight()/2)+60);
//        bigOval = new RectF();

        myFramePaint = new Paint();
        myFramePaint.setAntiAlias(true);
        myFramePaint.setColor(Color.TRANSPARENT);




    }

    private void drawArcs(Canvas canvas, RectF oval, boolean useCenter,
                          Paint paint) {
        canvas.drawRect(oval, myFramePaint);
//        canvas.drawArc(oval, myStart, mySweep, false, paint);
        canvas.drawArc(oval, myStart, mySweep, false, paint);

    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    public void setIncrement(float newIncrement) {
        SWEEP_INC = newIncrement;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawArcs(canvas, bigOval, true, myPaint);
       // value = (TextView) findViewById(R.id.value);
        //drawArcs(canvas, bigOval, true, myPaint);
        myStart = -90;
        // If the arc is currently getting bigger, decrease the value of
        // mySweep
        if (addToCircle) {
            mySweep -= SWEEP_INC;
        }
        // If the arc is currently getting smaller, increase the value of
        // mySweep
        else {
            mySweep += SWEEP_INC;
        }
        // If the animation has reached the end, reverse it

        invalidate();
    }
}
