package com.tatx.partnerapp.menuactivity;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Home on 05-12-2016.
 */
public class ViewsTvIv {

    private  ImageView imageView;
    private TextView textView;
    private int id;
    private int drawableBlackId;
    private int drawableRedId;

    public ViewsTvIv(ImageView imageView, TextView textView, int id, int drawableBlackId, int drawableRedId) {
        this.imageView = imageView;
        this.textView = textView;
        this.id = id;
        this.drawableBlackId = drawableBlackId;
        this.drawableRedId = drawableRedId;
    }



    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public int getId() {
        return id;
    }

    public int getDrawableBlackId() {
        return drawableBlackId;
    }

    public int getDrawableRedId() {
        return drawableRedId;
    }










}

