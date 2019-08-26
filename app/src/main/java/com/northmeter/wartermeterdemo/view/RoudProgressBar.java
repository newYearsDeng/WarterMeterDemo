package com.northmeter.wartermeterdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.northmeter.wartermeterdemo.R;

/**
 * created by lht on 2016/7/11 15:33
 */
public class RoudProgressBar extends View{
    private Paint mPaint,mPaint1;
    private float textSize;
    private float roundWidth;
    private int progress;
    private int max=100;

    public RoudProgressBar(Context context) {
        this(context,null);
    }

    public RoudProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoudProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        roundWidth=typedArray.getDimension(R.styleable.RoundProgressBar_roundWidth,25);
        textSize=typedArray.getDimension(R.styleable.RoundProgressBar_textSize,20);
        mPaint=new Paint();
        typedArray.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画最外层的圆
        int centre=getWidth()/2;//圆心坐标
        int radius= (int) (centre-roundWidth/2);

        mPaint.setColor(Color.parseColor("#99000000"));
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(centre,centre,radius,mPaint);
        //画字
        mPaint.setStrokeWidth(0);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        float textWidth = mPaint.measureText("跳过");
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float heitht=fontMetrics.bottom-fontMetrics.top;
        float baseline= (getHeight()-(getHeight()-heitht)/2-fontMetrics.bottom);
        canvas.drawText("跳过",centre-textWidth/2,baseline,mPaint);

        //画圆弧的进度
        mPaint1=new Paint();
        mPaint1.setStrokeWidth(4);//设置圆环的宽度
        mPaint1.setColor(Color.BLUE);//设置进度条的颜色
        mPaint1.setAntiAlias(true);
        RectF oval=new RectF(centre-radius,centre-radius,centre+radius,centre+radius);
        mPaint1.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 0, 360 * progress / max, false, mPaint1);//根据进度画圆弧
    }

    public synchronized int getMax(){
        return max;
    }
    /**
     * 设置最大进度
     */
    public synchronized void setMax(int max){
        this.max=max;
    }
    /**
     * 设置进度，此为线程安全控件，由于考虑到多线程的问题，需要同步
     * 刷新界面需要调用postInvalidate（） 能在非UI线程中刷新
     */
    public synchronized void setProgress(int progress){
        if(progress>max){
            progress=max;
        }
        if(progress<=max){
            this.progress=progress;
            postInvalidate();
        }
    }
}
