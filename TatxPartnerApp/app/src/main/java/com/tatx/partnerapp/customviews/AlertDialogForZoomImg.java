package com.tatx.partnerapp.customviews;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Home on 19-09-2016.
 */
public class AlertDialogForZoomImg extends Dialog {

    private final Context context;
    @BindView(R.id.image_view_zoom)
    ImageView imageViewZoom;
    String imageURL;
    DialogClickListener dialogClickListener;
    private ProgressDialog progressDialog;

    public AlertDialogForZoomImg(Context context, String imageURL, DialogClickListener dialogClickListener) {
        super(context);
        this.context=context;
        this.dialogClickListener=dialogClickListener;
        this.imageURL=imageURL;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zoom_img_popup);
        ButterKnife.bind(this);

        /*Picasso.with(context).load(imageURL).memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imageViewZoom);*/
        getBitmapFromUrl(imageURL);
        init();


    }

    @OnClick(R.id.close_popup)
    void okClicked(){
        dialogClickListener.onOkClick();
    }

    public void init(){
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

    }

    public interface DialogClickListener{
         void onOkClick();
    }


    private void getBitmapFromUrl(String carImageUrl)
    {

        // markerCategoryCarDrawable = (Bitmap) getResources().getDrawable(R.drawable.vip);
        progressDialog = Common.showProgressDialog(context);


        final boolean[] bitmapFetched = {false};

        do
        {


            Picasso.with(context).load(carImageUrl).into(new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    Common.Log.i("? - onBitmapLoaded"+bitmap.getWidth()+"  " +bitmap.getHeight());
                    // Common.getResizedBitmap(bitmap,(bitmap.getWidth()/100)*75,(bitmap.getHeight()/100)*75);

                    imageViewZoom.setImageBitmap(bitmap);

                    //  bitmapImg=bitmap;
                    bitmapFetched[0] = true;


                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                    Common.Log.i("? - onBitmapFailed");

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                    Common.Log.i("? - onPrepareLoad");




                }


            });





        }while (!bitmapFetched[0]);


        if (progressDialog!=null) {
            progressDialog.cancel();
        }


    }
}
