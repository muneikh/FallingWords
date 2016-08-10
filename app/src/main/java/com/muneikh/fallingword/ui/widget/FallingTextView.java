package com.muneikh.fallingword.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class FallingTextView extends TextView {

    private static final long THREE_SEC_IN_MILLIS = 3000;
    private Animation animation;

    public FallingTextView(Context context) {
        super(context);
        setup(context);
    }

    public FallingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public FallingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FallingTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context);
    }

    private void setup(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int deltaY = metrics.heightPixels - getHeight();
        animation = new TranslateAnimation(0, 0, 0, deltaY);
        animation.setDuration(THREE_SEC_IN_MILLIS);
    }

    public void setAnimationListener(Animation.AnimationListener listener) {
        animation.setAnimationListener(listener);
    }

    public void startAnimation() {
        startAnimation(animation);
    }
}
