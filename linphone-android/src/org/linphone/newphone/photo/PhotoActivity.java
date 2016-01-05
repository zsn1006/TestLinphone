package org.linphone.newphone.photo;
import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.linphone.R;
import org.linphone.newphone.photo.FolderPop.CallbackPopWin;
import org.linphone.newphone.tools.Constants;


public class PhotoActivity extends Activity{
	private GridView mGridView;
	private List<String> allList;
	private PhotoAdapter adapter;
	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
	private List<FolderBean> folderList = new ArrayList<FolderBean>();
	private Boolean mLoadAll = true;

	private ImageButton topBack = null;
	private TextView buttonTitle = null;
	private RelativeLayout mLayout = null;
	private Boolean mFolderFlag = false;
	private FolderPop mPop = null;
	private int mFolderPosition = 0;
	private Boolean mIsUserIcon = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_home_activity);

		mGridView = (GridView) findViewById(R.id.child_grid);
		allList = new ArrayList<String>();
		mIsUserIcon = getIntent().getBooleanExtra("isUserIcon", true);

		getImages();
		onInitControl();
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法运行在子线程中
	 * 
	 */
	private void getImages()	{
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		new Thread(new Runnable(){
			
			@Override
			public void run()	{
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = PhotoActivity.this.getContentResolver();
				
				//只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null, 
						MediaStore.Images.Media.MIME_TYPE + "=? or "
						+ MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{
						"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
						
				while (mCursor.moveToNext()){
					//获取图片的路径
					String path = mCursor.getString(mCursor
					
							.getColumnIndex(MediaStore.Images.Media.DATA));
					if (mLoadAll) {
					
						mLoadAll = false;
						allList.add(path);
						mGruopMap.put("aaa", allList);
				
					} else {
						allList.add(path);
					}
					
					//获取该图片的父路径名
					String parentName = new File(path).getParentFile().getName();
					//根据父路径讲图片放入到mGroupMap中
					if (!mGruopMap.containsKey(parentName))	{
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(parentName, chileList);
				
				
					} else {
						mGruopMap.get(parentName).add(path);
					}
				}
				
				mCursor.close();
				//通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
			}
		}).start();
	}
	
	private final static int SCAN_OK = 1;

	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg)	{
			super.handleMessage(msg);
			switch (msg.what)	{
			case SCAN_OK:
				mFolderFlag = true;
				folderList = subGroupOfImage(mGruopMap);
				adapter = new PhotoAdapter(PhotoActivity.this, allList, mGridView);
				adapter.onClick = onClick;
				mGridView.setAdapter(adapter);
				break;

			}
		}
		
	};
	
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v)	{
			String path = (String) v.getTag();
			if (path != null)	{
				//另存一张新图片,避免裁剪坏原图
				//path = onResaveAnotherPic(path);
				if (path != null)	{
					Intent data = null;
					data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("name", path);
					data.putExtras(bundle);
					setResult(9527, data);
					finish();
				}
			}
			
		}

		
	};
	
	/**
	 * 组装分组界面GridView的数据源，因为我们扫描手机的时候讲图片信息放在HashMap中，所有需要遍历HashMap讲数据组装成List
	 * 
	 * @param mGroupMap
	 * @return
	 */
	private List<FolderBean> subGroupOfImage(HashMap<String, List<String>> gruopMap) {

		List<FolderBean> list = new ArrayList<FolderBean>();
		if (gruopMap.size() == 0) {
	
			return list;
		}
		
		Iterator<Map.Entry<String, List<String>>> it = gruopMap.entrySet().iterator();
		while (it.hasNext())	{
			Map.Entry<String, List<String>> entry = it.next();
			FolderBean mImageBean = new FolderBean();
			String key = entry.getKey();
			List<String> value = entry.getValue();
			
			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			mImageBean.setTopImagePath(value.get(0));   //获取该组顶部第一张图片
			
			list.add(mImageBean);
		}
		
		for (int i = 0; i < list.size() - 1; i++)	{
			for (int j = 0; j < list.size() - 1 - i; j++)	{
				FolderBean bean0 = list.get(j);
				FolderBean bean1 = list.get(j + 1);
				String key0 = bean0.getFolderName().toLowerCase();
				String key1 = bean1.getFolderName().toLowerCase();
				if (key0.compareTo(key1) > 0)	{
					list.set(j, bean1);
					list.set(j + 1, bean0);
				}
			}
		}
		return list;
	}

	
	private void onInitControl()	{
		topBack = (ImageButton)findViewById(R.id.topBack);
		buttonTitle = (TextView)findViewById(R.id.buttonTitle);
		mLayout = (RelativeLayout)findViewById(R.id.mLayout);
		
		topBack.setOnClickListener(new OnClickListener()	{
			
			@Override
			public void onClick(View v)	{
				finish();
				
			}
		});
		
		buttonTitle.setOnClickListener(new OnClickListener()	{
			
			@Override
			public void onClick(View v)	{
				if (mFolderFlag)	{
					callPopWin call = new callPopWin();
					mPop = new FolderPop(PhotoActivity.this, folderList, mLayout, call,
							mFolderPosition);
				}
				
			}
		});
		
	}

	private  class callPopWin implements CallbackPopWin{

		@Override
		public void setResult(String result)	{
			if (result != null && result.length() > 0)	{
				mFolderPosition = Integer.valueOf(result);
				
				List<String> list = mGruopMap.get(folderList.get(mFolderPosition).getFolderName());
				adapter.onRefresh(list);
			}
			
		}
		
	}
	
	private String onResaveAnotherPic(String oldPath) {

		try {
			SharedPreferences sp = getSharedPreferences("sp_user", Activity.MODE_PRIVATE);
			String name = null;
			try {
				name = sp.getString("userId", "100000") + "_head.jpg";
			} catch (Exception e1) {
				e1.printStackTrace();
				name = "100000_head.jpg";
			}
			if (!mIsUserIcon) {
				File f = new File(oldPath);
				name = f.getName();
			}
			name = Constants.appRootAddr + "img/"+ name;
			File file = new File(name);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			try {
				if (!file.exists() || file.length() == 0) {
					file.createNewFile();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			RandomAccessFile rfile = new RandomAccessFile(file, "rw");
			FileInputStream fis = new FileInputStream(new File(oldPath));
			byte[] buffer = new byte[10240];
			int len;
			while ((len = fis.read(buffer)) != -1) {
				rfile.write(buffer, 0, len);
			}
			fis.close();
			rfile.close();
			return name;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
