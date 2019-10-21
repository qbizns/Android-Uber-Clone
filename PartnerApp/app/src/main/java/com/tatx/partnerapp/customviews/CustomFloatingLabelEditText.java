package com.tatx.partnerapp.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import com.tatx.partnerapp.commonutills.Common;

/**
 * Created by Venkateswarlu SKP on 30-08-2016.
 */
public class CustomFloatingLabelEditText extends CustomEditText
{
//    private OnFloatingLabelEditTextClickListener listener;

    public CustomFloatingLabelEditText(Context context) {
        super(context);
        init();
    }

    public CustomFloatingLabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFloatingLabelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomFloatingLabelEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {


        setInputType(InputType.TYPE_NULL);

        setTextIsSelectable(true);




    }



    public void setOnFloatingLabelEditTextClickListener(final OnFloatingLabelEditTextClickListener listener, final View nextFocusableView)
    {

        setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {

                Common.Log.i("? - onFocusChange() Executed. - hasFocus : "+hasFocus);

                if(hasFocus)
                {

                    listener.onFloatingLabelEditTextClick(CustomFloatingLabelEditText.this);



                }


            }

        });


        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

                clearFocus();

                if (nextFocusableView != null && nextFocusableView.getId() != getId() ) {nextFocusableView.requestFocus();}


            }
        });



    }


    public interface OnFloatingLabelEditTextClickListener
    {
         void onFloatingLabelEditTextClick(CustomFloatingLabelEditText customFloatingLabelEditText);
    }

}
