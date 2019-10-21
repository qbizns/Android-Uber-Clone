package com.tatx.partnerapp.customviews;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Home on 19-09-2016.
 */
public class TripCanceledAlertDialog extends Dialog {

    private Context context;
    @BindView(R.id.rocket_image)
    ImageView rocket_image;
    private AnimationDrawable rocketAnimation;
    private DialogClickListener dialogClickListener;


    public TripCanceledAlertDialog(Context context,DialogClickListener dialogClickListener) {
        super(context);
        this.dialogClickListener=dialogClickListener;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.trip_canceled_popup);
        ButterKnife.bind(this);

        rocket_image.setBackgroundResource(R.drawable.rocket_thrust);

        rocketAnimation = (AnimationDrawable) rocket_image.getBackground();
        rocketAnimation.start();
        init();


    }

    @OnClick(R.id.ll_cancel_order)
    void okClicked(){
        dialogClickListener.onOkClick();
    }
    public void init(){
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.drawable.canel_trip_dialog_background);


    }

    public interface DialogClickListener{
         void onOkClick();
    }


}
