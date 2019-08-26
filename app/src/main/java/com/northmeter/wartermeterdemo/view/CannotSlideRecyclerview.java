package com.northmeter.wartermeterdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author zz
 * @time 2016/09/07 10:14
 * @des 不能滑动的recyclerview
 */
public class CannotSlideRecyclerview extends RecyclerView{

    public CannotSlideRecyclerview(Context context) {
        super(context);
    }

    public CannotSlideRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CannotSlideRecyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }
}
