package com.tatx.userapp.pojos;

import android.graphics.Bitmap;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by Venkateswarlu SKP on 18-01-2017.
 */
public class StateListDrawableWithBitmap {
    StateListDrawable stateListDrawable;
    Bitmap bitmap;


    public StateListDrawable getStateListDrawable() {
        return stateListDrawable;
    }

    public void setStateListDrawable(StateListDrawable stateListDrawable) {
        this.stateListDrawable = stateListDrawable;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


}
