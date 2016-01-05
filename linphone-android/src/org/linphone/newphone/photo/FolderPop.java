package org.linphone.newphone.photo;

import java.io.IOException;
import java.util.List;

import org.linphone.R;

import android.content.Context;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;


public class FolderPop {
	private PopupWindow mPopWin = null;
	private FolderAdapter mAdapter = null;
	private ListView mPopList = null;
	private CallbackPopWin mCallback = null;

	/**
	 *文件目录列表构造函数
	 * @param list
	 *            数据列表
	 * @param view
	 *            父视图
	 * @param callback
	 *            返回接口
	 * 
	 */
	public FolderPop(Context context, List<FolderBean> list, View view, 	CallbackPopWin callback, int position)	{
		if (view != null)	{
			this.mCallback = callback;
			iniPopupWindow(context, list, view, position);
		}
	}
	


	/**
	 * pop初始化
	 * 
	 *  Please give the method description.
	 *  
	 *  ##@author ty - 2014年9月17日
	 *  
	 *  @param value
	 *            description
	 * @return void
	 * @throws IOException
	 *             if an input/output error occurs
	 *  
	 */
	private void iniPopupWindow(Context context, List<FolderBean> list, View view, int position)	{
		// 列表窗体处理
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.photo_folder_list, null);
		mPopList = (ListView) layout.findViewById(R.id.mListView);
		mPopWin = new PopupWindow(layout);
		mPopWin.setFocusable(true);
		
		mAdapter = new FolderAdapter(context, list, mPopList, position);
		mPopList.setAdapter(mAdapter);
		
		/*** 用于back键退出，但是会和点旁边消失 */
		layout.setFocusableInTouchMode(true);
		layout.setOnKeyListener(new OnKeyListener()	{
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)	{
				if (keyCode == KeyEvent.KEYCODE_BACK)	{
					if (mPopWin != null)	{
						mPopWin.dismiss();
						mPopWin = null;
					}
					return true;
				}
				return false;
			}
		});
		
		mPopList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,	long arg3)	{
				mCallback.setResult(String.valueOf(arg2));
				mPopWin.dismiss();
				
			}
			
		});
		
		//控制popupwindow的宽度和高度自适应
		mPopList.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopWin.setWidth(LayoutParams.MATCH_PARENT);
		if (list.size() <= 4)	{
			mPopWin.setHeight(LayoutParams.WRAP_CONTENT);
	
		} else {
			mPopWin.setHeight((mPopList.getMeasuredHeight()) * 4);
		}
		
		int popupWidth = mPopWin.getWidth();
		int popupHeight = mPopWin.getHeight();
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		mPopWin.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
		
	}
	
	
	
	
	/**
	 * 回调方法
	 * 
	 *  @author ty
	 * 
	 */
	public interface CallbackPopWin	{
		public void setResult(String result);
	}
}
