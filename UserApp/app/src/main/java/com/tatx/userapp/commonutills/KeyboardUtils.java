package com.tatx.userapp.commonutills;

/**
 * Created by Home on 08-11-2016.
 *
 *
 */

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.HashMap;
public class KeyboardUtils
    implements ViewTreeObserver.OnGlobalLayoutListener
    {
        @Override
        public void onGlobalLayout()
        {
            Rect r = new Rect();
            //r will be populated with the coordinates of your view that area still visible.
            mRootView.getWindowVisibleDisplayFrame(r);

            int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
            float dp = heightDiff/ mScreenDensity;

            if(mCallback != null)
                mCallback.onToggleSoftKeyboard(dp > 200);
        }

        public interface SoftKeyboardToggleListener
        {
            void onToggleSoftKeyboard(boolean isVisible);
        }

        private SoftKeyboardToggleListener mCallback;
        private View mRootView;
        private float mScreenDensity = 1;
        private static HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();



    public static void addKeyboardToggleListener(Activity act, SoftKeyboardToggleListener listener)
    {
        removeKeyboardToggleListener(listener);

        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener)
    {
        if(sListenerMap.containsKey(listener))
        {
            KeyboardUtils k = sListenerMap.get(listener);
            k.removeListener();

            sListenerMap.remove(listener);
        }
    }

    public static void removeAllKeyboardToggleListeners()
    {
        for(SoftKeyboardToggleListener l : sListenerMap.keySet())
            sListenerMap.get(l).removeListener();

        sListenerMap.clear();
    }

    private void removeListener()
    {
        mCallback = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    private KeyboardUtils(Activity act, SoftKeyboardToggleListener listener)
    {
        mCallback = listener;

        mRootView = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mScreenDensity = act.getResources().getDisplayMetrics().density;
    }



}
