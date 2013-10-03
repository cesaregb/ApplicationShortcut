package com.il.appshortcut.android.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.il.appshortcut.R;

public class ApplicationListItemView extends View {
	final int MIN_WIDTH = 100;
	final int MIN_HEIGHT = 100;
	final int RECT_SIZE = 200;
	final int TOP_MARGIN = 5;
	final int DEFAULT_COLOR = Color.WHITE;
	final int STROKE_WIDTH = 2;
	
	Bitmap backgroundImage;
	private Paint bgPaint;
	private Paint appText;
	
	private String text;
	
	public ApplicationListItemView (Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
		init();
	}

	public ApplicationListItemView (Context context) {
		super(context);
		init();
	}

	public ApplicationListItemView (Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		backgroundImage = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cuadrito);
		bgPaint = new Paint();
		appText =  new Paint();
		appText.setTextSize(20);
		
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
		canvas.drawText(this.text, 5, RECT_SIZE - 50, appText);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
