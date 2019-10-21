package com.tatx.userapp.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.tatx.userapp.R;
import com.tatx.userapp.commonutills.Common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by user on 30-06-2016.
 */
public class LongClickableRelativeLayout extends RelativeLayout
{

    public LongClickableRelativeLayout(Context context)
    {
        super(context);
        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());
        Common.Log.i("1");


    }

    public LongClickableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());
        Common.Log.i("2");
        init(context, attrs);

    }

    public LongClickableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {

        super(context, attrs, defStyleAttr);
        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());
        Common.Log.i("3");

        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs)
    {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LongClickableStyleable);

        for (int i = 0; i < typedArray.getIndexCount(); i++)
        {

            int attr = typedArray.getIndex(i);

            switch (attr)
            {
                case R.styleable.LongClickableStyleable_onKeyLongPress:

                    if(context.isRestricted())
                    {

                        throw new IllegalStateException("The "+getClass().getCanonicalName()+":onKeyLongPress attribute cannot be used within a restricted context");

                    }

                    final String methodName = typedArray.getString(attr);

                    Common.Log.i("methodName : "+methodName);

                    if(methodName != null)
                    {

                        setOnLongClickListener(new OnLongClickListener()
                        {
                            @Override
                            public boolean onLongClick(View v)
                            {



                                try
                                {
                                    Method method = getContext().getClass().getMethod(methodName, View.class);

                                    method.invoke(getContext(),LongClickableRelativeLayout.this);

                                    return true;

                                }
                                catch (NoSuchMethodException e)
                                {
                                    e.printStackTrace();

                                    int id = getId();

                                    String idText = id == NO_ID ? "" : " with id '" + getContext().getResources().getResourceEntryName(id) + "'";

                                    throw new IllegalStateException("Could not find a method " + methodName + "(View) in the activity " + getContext().getClass() + " for onKeyLongPress handler on view " + LongClickableRelativeLayout.this.getClass() + idText, e);



                                }
                                catch (InvocationTargetException e)
                                {

                                    e.printStackTrace();

                                    throw new IllegalStateException("Could not execute method of the activity", e);

                                }
                                catch (IllegalAccessException e)
                                {
                                    e.printStackTrace();

                                    throw new IllegalStateException("Could not execute non public method of the activity", e);

                                }



                            }

                        });


                    }



                    break;

            }


        }


        typedArray.recycle();

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LongClickableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {

        super(context, attrs, defStyleAttr, defStyleRes);
        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());
        Common.Log.i("4");
        init(context, attrs);



    }



}
