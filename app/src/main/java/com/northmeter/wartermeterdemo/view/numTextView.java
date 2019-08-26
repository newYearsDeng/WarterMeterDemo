package com.northmeter.wartermeterdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;

/**
 * Created by lht on 2016/5/9.
 */
public class numTextView extends TextView {
    private String text;
    private int height = 100;
    private int width = 100;
    private int color = Color.BLACK;
    private Paint p;
    private String number;
    private Paint.FontMetrics fm;
    private float textCenterX;
    private float textBaselineY;
    private float textSize;
    public numTextView(Context context) {
        super(context);
        init();
    }
    public numTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.numText, 0, 0);
        height = t.getDimensionPixelSize(R.styleable.numText_numText_height, height);
        width = t.getDimensionPixelSize(R.styleable.numText_numText_width, width);
        color = t.getDimensionPixelSize(R.styleable.numText_numText_color, color);
        number = t.getString(R.styleable.numText_numText_num);
        textSize = t.getDimensionPixelSize(R.styleable.numText_numText_textSize, 100);
        text = number;
        t.recycle();
        init();
    }
    private void init() {
        p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }
    public void setWidth(int displayWidth){
        width=displayWidth;
        postInvalidate();

 }
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = width;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = width;
        }

        return result;
    }


    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = height;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = height;
        }
        return result;
    }

    public static boolean isInt(String s) {
        try {
            int i = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void start() {
        if (isInt(text)) {
            com.nineoldandroids.animation.ValueAnimator valueAnimator = com.nineoldandroids.animation.ValueAnimator.ofInt(0, (int) Integer.parseInt(text));
            double time = Math.random();
            valueAnimator.setDuration((int) (time * 1000));
            valueAnimator.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator valueAnimator) {
                    setText(valueAnimator.getAnimatedValue().toString());
                    invalidate();
                }
            });
            valueAnimator.start();
        }
    }

    private void setTextLocation() {
        fm = p.getFontMetrics();
        float textWidth = p.measureText(text);
       // float textCenterVerticalBaselineY = getHeight() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
        float textCenterVerticalBaselineY = height/2 - fm.descent + (fm.descent-fm.ascent)/2;
        textCenterX = textWidth / 2;
        textBaselineY = textCenterVerticalBaselineY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setTextLocation();
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);
        canvas.drawText(text, textCenterX, textBaselineY, p);

    }
}
