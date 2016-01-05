package org.linphone.newphone.photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.linphone.R;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CutPicActivity extends Activity {
	private CirclView mCirclView;
	private Button mComplete;
	//照片存储路径
	private String filepath = null;
	private String picname = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_cutpic);
		filepath = getIntent().getStringExtra("bmpPath");
		picname = getIntent().getStringExtra("bmpName");
		
		initView();
		addListener();
		new Handler().postDelayed(new Runnable(){
			
		
			public void run()	{
				//获取图片路径
				String bitmapPath = getIntent().getStringExtra("fullPath");
				if (bitmapPath == null || bitmapPath.isEmpty()) {
					bitmapPath = filepath + picname;
				}
				//从图片路径解码获得图片
				Bitmap bm = BitmapFactory.decodeFile(bitmapPath);
				//获取图片的旋转角度（鉴于有些手机拍照后会把照片旋转）
				int degree = readPictureDegree(bitmapPath);
				//把图片放到设置的剪裁界面
				mCirclView.setBitmap(bm, degree);
			}
		}, 30);//30毫秒用于等待解码图片，要延长时间可以自己设置
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path)	{
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation)	{
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
                break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			
			}
		} catch (IOException e)	{
			e.printStackTrace();
		}
		return degree;
	}
	
	
	
    /**
     * 控件的监听事件
     */
	private void addListener()	{
		mComplete.setOnClickListener(new OnClickListener()	{
			
			@Override
			public void onClick(View v)	{
				Bitmap bm = mCirclView.getClipImage();
				if (null != bm)	{
					storeImageToSDCARD(bm, picname, filepath);
				}
				
				setResult(123);
				finish();
			}
		});
	}

	/**
	 * storeImageToSDCARD 将bitmap存放到sdcard中
	 * 
	 */
	public void storeImageToSDCARD(Bitmap colorImage, String ImageName, String path) {
		File file = new File(path);
		if (!file.exists())	{
			file.mkdir();
		}
		File imagefile = new File(file, ImageName);
		try {
			imagefile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagefile);
			colorImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 实例化控件
	 */
	private void initView()	{
		mCirclView = (CirclView)findViewById(R.id.circl);
		mComplete = (Button) findViewById(R.id.cut_pic_btn);
		
	}

}
