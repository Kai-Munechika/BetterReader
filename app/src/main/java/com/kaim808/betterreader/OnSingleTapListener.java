package com.kaim808.betterreader;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by KaiM on 8/12/17.
 */

public class OnSingleTapListener implements View.OnTouchListener {
    private GestureDetector gestureDetector;

    public OnSingleTapListener(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            OnSingleTapListener.this.onSingleTap(e);
            return super.onSingleTapConfirmed(e);
        }
    }

    public void onSingleTap(MotionEvent e) {
        // To be overridden when implementing listener
    }
}
