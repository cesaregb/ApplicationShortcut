package com.il.easyclick.android.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.il.easyclick.R;

public class LuncherPatternView extends View {
	private LuncherPatternListener onItemPressedListener;

	public interface LuncherPatternListener {
		public void fireApplication(String currentSelection);

		public void registerSelection(String currentSelection);
	}

	private String currentSelection;
	Handler lunchAppTimer = new Handler();

	View thisContainer = null;
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			onItemPressedListener.fireApplication(currentSelection);
			currentSelection = "";
			cellProps.setLastPressed(0);
			if (thisContainer != null) {
				thisContainer.invalidate();
			}
		}
	};

	final int MIN_WIDTH = 200;
	final int MIN_HEIGHT = 200;
	final int DEFAULT_COLOR = Color.WHITE;
	final int STROKE_WIDTH = 2;

	private Paint background;
	private Paint cellLeft;
	private Paint cellTop;
	private Paint cellRight;
	private Paint cellBottom;
	private CellProps cellProps;

	Bitmap btnLeft;
	Bitmap btnUp;
	Bitmap btnRight;
	Bitmap btnDown;
	Bitmap btnSelected;

	public static class CellProps {
		private int size = 200;
		private List<CellAreas> cellAreasArray = new ArrayList<CellAreas>();
		private int lastPressed = 0;

		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public List<CellAreas> getCellAreasArray() {
			return cellAreasArray;
		}
		public void setCellAreasArray(List<CellAreas> cellAreasArray) {
			this.cellAreasArray = cellAreasArray;
		}
		public int getLastPressed() {
			return lastPressed;
		}
		public void setLastPressed(int lastPressed) {
			this.lastPressed = lastPressed;
		}
	}

	public static class CellAreas {
		private int cell;
		private int left;
		private int top;
		private int right;
		private int bottom;
		CellAreas(int cell, int left, int top, int right, int bottom) {
			this.cell = cell;
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
		public int getLeft() {
			return left;
		}
		public void setLeft(int left) {
			this.left = left;
		}
		public int getTop() {
			return top;
		}
		public void setTop(int top) {
			this.top = top;
		}
		public int getRight() {
			return right;
		}
		public void setRight(int right) {
			this.right = right;
		}
		public int getBottom() {
			return bottom;
		}
		public void setBottom(int bottom) {
			this.bottom = bottom;
		}
		public int getCell() {
			return cell;
		}
		public void setCell(int cell) {
			this.cell = cell;
		}
	}

	private boolean mInputEnabled = true;

	public LuncherPatternView(Context context) {
		super(context);
		init();
	}

	public LuncherPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LuncherPatternView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();

	}

	private void init() {
		currentSelection = "";
		try {
			onItemPressedListener = (LuncherPatternListener) getContext();
		} catch (ClassCastException e) {
			throw new ClassCastException(getContext().toString()
					+ " must implement OnItemPressedListener");
		}
		btnLeft = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.btn_left);
		btnUp = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.btn_up);
		btnDown = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.btn_down);
		btnRight = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.btn_right);
		btnSelected = BitmapFactory.decodeResource(getContext()
				.getResources(), R.drawable.btn_selected);

		cellProps = new CellProps();

		background = new Paint();
		background.setColor(Color.parseColor("#122320"));

		cellLeft = new Paint();
		cellTop = new Paint();
		cellRight = new Paint();
		cellBottom = new Paint();
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

	private int measure(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.UNSPECIFIED) {
			result = 200;
		} else {
			result = specSize;
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		thisContainer = this;
		canvas.drawRect(2, 2, getWidth() - 2, getHeight() - 2, background);
		canvas.save();
		drawDots(canvas);
	}

	private void drawDots(Canvas canvas) {
		int cellSize = cellProps.getSize();
		int halfSpaceCellSize = (cellSize / 2);
		int cellSpaceSize = cellSize + halfSpaceCellSize;

		int left = getWidth() / 2 - cellSpaceSize;
		int top = getHeight() / 2 - halfSpaceCellSize;
		int right = getWidth() / 2 - halfSpaceCellSize;
		int bottom = getHeight() / 2 + halfSpaceCellSize;
		if (cellProps.getLastPressed() == 1) {
			canvas.drawBitmap(btnSelected, left, top, cellLeft);
		} else {
			canvas.drawBitmap(btnLeft, left, top, cellLeft);
		}

		cellProps.getCellAreasArray().add(
				new CellAreas(1, left, top, right, bottom));

		left = getWidth() / 2 - halfSpaceCellSize;
		top = getHeight() / 2 - cellSpaceSize;
		right = getWidth() / 2 + halfSpaceCellSize;
		bottom = getHeight() / 2 - halfSpaceCellSize;
		if (cellProps.getLastPressed() == 2) {
			canvas.drawBitmap(btnSelected, left, top, cellTop);
		} else {
			canvas.drawBitmap(btnUp, left, top, cellTop);
		}
		cellProps.getCellAreasArray().add(
				new CellAreas(2, left, top, right, bottom));

		left = getWidth() / 2 + halfSpaceCellSize;
		top = getHeight() / 2 - halfSpaceCellSize;
		right = getWidth() / 2 + cellSpaceSize;
		bottom = getHeight() / 2 + halfSpaceCellSize;
		if (cellProps.getLastPressed() == 3) {
			canvas.drawBitmap(btnSelected, left, top, cellRight);
		} else {
			canvas.drawBitmap(btnRight, left, top, cellRight);
		}
		cellProps.getCellAreasArray().add(
				new CellAreas(3, left, top, right, bottom));

		left = getWidth() / 2 - halfSpaceCellSize;
		top = getHeight() / 2 + halfSpaceCellSize;
		right = getWidth() / 2 + halfSpaceCellSize;
		bottom = getHeight() / 2 + cellSpaceSize;
		if (cellProps.getLastPressed() == 4) {
			canvas.drawBitmap(btnSelected, left, top, cellBottom);
		} else {
			canvas.drawBitmap(btnDown, left, top, cellBottom);
		}
		cellProps.getCellAreasArray().add(
				new CellAreas(4, left, top, right, bottom));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mInputEnabled || !isEnabled()) {
			return false;
		}

		final float x = event.getX();
		final float y = event.getY();

		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_MOVE) {
			if (checkGrid(x, y)) {
				onItemPressed(cellProps.getLastPressed());
			}

		} else if (action == MotionEvent.ACTION_HOVER_EXIT) {
			return super.onTouchEvent(event);
		} else if (action == MotionEvent.ACTION_UP) {
			return super.onTouchEvent(event);
		}
		return true;
		// return super.onTouchEvent(event);
	}

	public boolean checkGrid(float x, float y) {
		List<CellAreas> _list = cellProps.getCellAreasArray();
		for (CellAreas ca : _list) {
			if (ca.getLeft() < x && ca.getRight() > x && ca.getTop() < y
					&& ca.getBottom() > y
					&& ca.getCell() != cellProps.getLastPressed()) {
				cellProps.setLastPressed(ca.getCell());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onHoverEvent(MotionEvent event) {
		return onTouchEvent(event);
	}

	public void onItemPressed(int itemPressed) {
		currentSelection += String.valueOf(itemPressed);
		com.il.easyclick.helpers.UserInputHelpers.pressEffect(
				(Vibrator) getContext().getSystemService(
						Context.VIBRATOR_SERVICE), (AudioManager) getContext()
						.getSystemService(Context.AUDIO_SERVICE));

		onItemPressedListener.registerSelection(currentSelection);

		lunchAppTimer.removeCallbacks(runnable);
		lunchAppTimer.postDelayed(runnable, 2000);
		this.invalidate();
	}

}