package com.northmeter.wartermeterdemo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by lht on 2016/5/10.
 */
public class numViewGroup extends AutoLinearLayout{

    public numViewGroup(Context context) {
        super(context);
    }

    public numViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public numViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public numViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int prevChildRight = 0;
        int prevChildBottom = 0;
        for (int i = 0; i < count; i++) {
            final numTextView child = (numTextView)getChildAt(i);
            child.layout(prevChildRight, prevChildBottom,
                    prevChildRight + child.getMeasuredWidth(),
                    prevChildBottom + child.getMeasuredHeight());
            prevChildRight += child.getMeasuredWidth();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final numTextView child = (numTextView)getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void start(){
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final numTextView child = (numTextView)getChildAt(i);
            child.start();
        }
    }

}
