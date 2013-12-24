package com.il.easyclick.android.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.il.easyclick.R;

public class ActivityActionListItemView extends TextView {
	final int MIN_WIDTH = 200;
	final int MIN_HEIGHT = 50;
	final int RECT_SIZE = 100;
	final int TOP_MARGIN = 5;
	final int DEFAULT_COLOR = Color.WHITE;
	final int STROKE_WIDTH = 2;
	Bitmap backgroundImage;
	private Paint bgPaint;

	public ActivityActionListItemView(Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
		init();
	}

	public ActivityActionListItemView(Context context) {
		super(context);
		init();
	}

	public ActivityActionListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		Resources myResources = getResources();
		backgroundImage = BitmapFactory.decodeResource(myResources, R.drawable.btn_selected);
		bgPaint = new Paint();
		setMinimumWidth(MIN_WIDTH);
		setMinimumHeight(MIN_HEIGHT);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = measure(widthMeasureSpec);
		int measureHeight = measure(heightMeasureSpec);
		int d = Math.min(measureWidth, measureHeight);
		setMeasuredDimension(d, d);
	}
	
	private int measure(int measureSpec){
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.UNSPECIFIED){
			result = RECT_SIZE;
		}else{
			result = specSize;
		}
		return result;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(backgroundImage, 5, TOP_MARGIN, bgPaint);
		super.onDraw(canvas);
		canvas.restore();
	}

}
