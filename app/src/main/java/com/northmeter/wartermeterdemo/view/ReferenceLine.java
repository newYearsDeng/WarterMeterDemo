package com.northmeter.wartermeterdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.northmeter.wartermeterdemo.utils.CarmeraUtils;

/**
 * created by lht on 2016/5/10
 */
public class ReferenceLine extends View {

	private Paint mLinePaintRec;
	private float top, left, right, bottom;
	private boolean isDraw;
	public ReferenceLine(Context context) {
		this(context, null);

	}

	public ReferenceLine(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public ReferenceLine(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {

		//读数标识矩形框
		mLinePaintRec = new Paint();
		mLinePaintRec.setAntiAlias(true);
		mLinePaintRec.setColor(Color.GREEN);
		mLinePaintRec.setStyle(Paint.Style.STROKE);
		mLinePaintRec.setStrokeWidth(5);
	}


	/**
	 * 3* @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if (isDraw) {
			canvas.drawRect(left,
					top,
					right,
					bottom, mLinePaintRec);
		}
	}

	public synchronized void setRect(float top1, float left1, float right1, float bottom1) {
		isDraw = true;
		top = top1;
		left = left1;
		right = right1;
		bottom = bottom1;
		postInvalidate();

	}
}
