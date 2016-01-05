package org.linphone.newphone.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CirclView extends SurfaceView implements  android.view.SurfaceHolder.Callback,
					GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener,
					ScaleGestureDetector.OnScaleGestureListener
{
	

	

	Context ctx;
	private ScaleGestureDetector mScaleGestureDetector = null;
	float scale = 1.0f;
	private GestureDetectorCompat mDetector;
	Bitmap backBitmap;
	ThreadDraw myThread;
	float mScale = 1.0f;
	Bitmap cacheBitmap = null;

	float xCircl, yCircl = 0;
	float radius = 200;
	
	float xBack = 0f, yBack = 0f;
	
	public CirclView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		SurfaceHolder holder = this.getHolder();
		holder.addCallback(this);
		mDetector = new GestureDetectorCompat(context, this);
		mDetector.setOnDoubleTapListener(this);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)	{
		super.onSizeChanged(w, h, oldw, oldh);
		if (getRight() - getLeft() > 0 && getBottom() - getTop() > 0) {
			cacheBitmap = Bitmap.createBitmap(getRight() - getLeft(), getBottom() - getTop(),
					Config.ARGB_8888);
		}
	}
	
	@Override
	public void draw(Canvas canvas)	{
		super.draw(canvas);
		if (backBitmap == null) {
			return;
		}
		xBack = (getRight() - getLeft() - backBitmap.getWidth()*mScale*scale) / 2;
		yBack = (getBottom() - getTop() - backBitmap.getHeight()*mScale*scale)/ 2;
		canvas.drawARGB(255, 255, 255, 255);
		Paint paint=new Paint();

		 Matrix mMatrix = new Matrix();  
		 
		 mMatrix.setScale(mScale*scale, mScale*scale);
		 mMatrix.postTranslate(xBack, yBack);
		 
		 canvas.drawBitmap(backBitmap, mMatrix, paint);
		//canvas.drawBitmap(backBitmap, null, new Rect(0,0,getRight()-getLeft(),getBottom()-getTop()), paint);
		
		drawMask(cacheBitmap);
		paint.setAlpha(128);
		canvas.drawBitmap(cacheBitmap, null, new Rect(0, 0, getRight() - getLeft(), getBottom() - getTop()), paint);
		
	}
	
	
	
	 void drawMask(Bitmap bm)	{
		Canvas canvas = new Canvas(bm);
		canvas.drawARGB(255, 0, 0, 0);
		//创建画笔
		Paint mPaint = new Paint();
		////设置画笔style为画实心的
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setARGB(0, 0, 0, 0);
		//显示切图圆圈
		mPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC));
		canvas.drawCircle(xCircl, yCircl, radius, mPaint);	
	}

	public void setBitmap(Bitmap bm, int degree){
		if (bm == null || bm.getWidth() == 0 || bm.getHeight() == 0)
		
			return;
		float Maxwidth = getRight() - getLeft();
		float Maxhight = getBottom() - getTop();
		if (bm.getWidth() > Maxwidth || bm.getHeight() > Maxhight)	{
			float mx  = Maxwidth / bm.getWidth()*1.5f;
			float my  = Maxwidth / bm.getHeight()*1.5f;
			float sc  = Math.min(mx, my);
			Matrix mMatrix = new Matrix();
			mMatrix.setScale(sc, sc);
			mMatrix.postRotate(degree);
			Bitmap scaleBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), mMatrix, false);
			bm = scaleBitmap;
		
		} else if (bm.getWidth() < Maxwidth / 2 || bm.getHeight() < Maxhight / 2) {
			float mx = 2f;
			float my = 2f;
			float sc = Math.min(mx, my);
			Matrix mMatrix = new Matrix();
			mMatrix.setScale(sc, sc);
			Bitmap scaleBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), mMatrix, false);
			bm = scaleBitmap;
		}	
		backBitmap = bm;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)	{
		int count = event.getPointerCount();
		if (count == 2)	{
			mScaleGestureDetector.onTouchEvent(event);
		}
		else if (count == 1) 
		{
			float x = event.getRawX() - getLeft();
			float y = event.getRawY() - getTop();
			float dis = (x - xCircl)*(x - xCircl)+(y - yCircl)*(y - yCircl);
			
			if (dis < radius *radius) {
				mDetector.onTouchEvent(event);
			}
			else {
				//moveType = 0;
			}
		}
		return true;
		//return super.onTouchEvent(event);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)	{
		myThread = new ThreadDraw(holder);//创建一个绘图线程
		myThread.isRun = true;
		myThread.start();
		xCircl = (getRight() - getLeft()) / 2;
		yCircl = (getBottom() - getTop()) / 2;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
	 
	 int height) {
		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)	{
		myThread.isRun = false;
	}
	
	//线程内部类
	class ThreadDraw extends Thread
	{
		private SurfaceHolder holder;
		public boolean isRun;
		public ThreadDraw(SurfaceHolder holder)
		{
			this.holder = holder;
			isRun = true;
		}
		
		@Override
		public void run()
		{
			while (isRun)
			{
				Canvas c = null;
				try
				{
					synchronized (holder)
					{
						c = holder.lockCanvas();
						CirclView.this.draw(c);
						Thread.sleep(1);  //睡眠时间为1秒
					}
					}
					catch (Exception e){
					e.printStackTrace();
				}
				finally
				{
					if (c != null)
					{
						holder.unlockCanvasAndPost(c); //结束锁定画图，并提交改变。
					}
				}
			}
		}
	}
	
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)	{
		return false;
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent e){
		return false;
	}
	
	@Override
	public boolean onDoubleTapEvent(MotionEvent e){
		return false;
	}
	
	@Override
	public boolean onDown(MotionEvent e){
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e){
		
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e){
		return false;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY){
		Log.e("tag", "on scroll: x="+distanceX+" y="+distanceY);
		
		xCircl -= distanceX;
		yCircl -= distanceY;
		
		return true;
	}
	
	@Override
	public void onLongPress(MotionEvent e){
		
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.e("tag", "on Fling: x="+velocityX+" y="+velocityY);
		return false;
	}
	
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		 Log.e("tag", "scale:"+detector.getScaleFactor());
		 //缩放比例
		 scale = detector.getScaleFactor();
		 return false;
	}
	
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector)	{
		return true;
	}
	
	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		mScale *= detector.getScaleFactor();
		scale = 1.0f;
	}
	
	public void setRadius(float r) {
		radius = r;
	}
	
	public Bitmap getClipImage()	{
		int w = (int)(radius*2);
		Bitmap clipBitmap = Bitmap.createBitmap(w, w,
		 Config.ARGB_8888);
		Matrix mMatrix = new Matrix();
		mMatrix.setScale(mScale*scale,  mScale*scale);
		Bitmap scaleBitmap = Bitmap.createBitmap(backBitmap, 0, 0, backBitmap.getWidth(),
				backBitmap.getHeight(), mMatrix, false);
		Canvas canvas = new Canvas(clipBitmap);
		int x = (int)(xCircl - radius - xBack);
		int y = (int)(yCircl - radius - yBack);
		canvas.drawBitmap(scaleBitmap, new Rect(x, y, x+w, y+w), new Rect(0, 0, w, w), new Paint());
		return clipBitmap;
		
	}


}
