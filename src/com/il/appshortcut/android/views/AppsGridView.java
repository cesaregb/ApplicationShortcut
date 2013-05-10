package com.il.appshortcut.android.views;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.il.appshortcut.events.AdapterView;
import com.il.appshortcut.events.DragSource;

public class AppsGridView extends GridView implements AdapterView.OnItemClickListener {

	private Bitmap mTexture;
    private Paint mPaint;
    private int mTextureWidth;
    private int mTextureHeight;
	
	public AppsGridView(Context context) {
		super(context);
	}
	public AppsGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public AppsGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	//view 
	@Override
    public boolean isOpaque() {
        return !mTexture.hasAlpha();
    }

    @Override
    protected void onFinishInflate() {
//        setOnItemClickListener(this);
    }
	
    
    @Override
    public void draw(Canvas canvas) {
        final Bitmap texture = mTexture;
        final Paint paint = mPaint;

        final int width = getWidth();
        final int height = getHeight();

        final int textureWidth = mTextureWidth;
        final int textureHeight = mTextureHeight;

        int x = 0;
        int y;

        while (x < width) {
            y = 0;
            while (y < height) {
                canvas.drawBitmap(texture, x, y, paint);
                y += textureHeight;
            }
            x += textureWidth;
        }

        super.draw(canvas);
    }
    
    @Override
    public void onItemClick(AdapterView parent, View v, int position, long id) {
//        ApplicationInfo app = (ApplicationInfo) parent.getItemAtPosition(position);
//        mLauncher.startActivitySafely(app.intent);
    }

}
