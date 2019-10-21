package com.tatx.partnerapp.menuactivity;

import android.widget.TextView;

/**
 * Created by Home on 05-12-2016.
 */
public class ViewsTvIv1 {
    private int drawableBlackId;
    private int drawableRedId;
    private int id;
    private String tabName;
    private TextView textView;

    public ViewsTvIv1( int id, int drawableBlackId, int drawableRedId,String tabName, TextView textView) {
        this.drawableBlackId = drawableBlackId;
        this.drawableRedId = drawableRedId;
        this.id = id;
        this.tabName = tabName;
        this.textView = textView;
    }




    public int getDrawableRedId() {
        return drawableRedId;
    }

    public int getDrawableBlackId() {
        return drawableBlackId;
    }

    public int getId() {
        return id;
    }

    public String getTabName() {
        return tabName;
    }

    public TextView getTextView() {
        return textView;
    }














}

