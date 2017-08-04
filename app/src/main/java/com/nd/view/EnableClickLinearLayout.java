package com.nd.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Danny on 2017/3/20.
 */
public class EnableClickLinearLayout extends LinearLayout {

    private long mFirstTime = 0;

    public EnableClickLinearLayout(Context context) {
        this(context, null);
    }

    public EnableClickLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnableClickLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public EnableClickLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event == null) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mFirstTime == 0) {
                    mFirstTime = System.currentTimeMillis();
                }
                break;
            case MotionEvent.ACTION_UP:
                //long nowTime = System.currentTimeMillis();
                if (mFirstTime != 0) {
                    super.callOnClick();
                    Log.d("tdl", "tdl call onclick");
                    mFirstTime = 0;
                }
                break;
            default:
                break;
        }
        return true;
    }
}
