package org.linphone.newphone.photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.linphone.R;
import org.linphone.newphone.photo.NativeImageLoader.NativeImageCallBack;
import org.linphone.newphone.photo.PhotoImageView.OnMeasureListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoAdapter extends BaseAdapter {
	private Point mPoint = new Point(0,0); //用来封装ImageView的宽和高的对象
	/**
	 * 用来存储图片的选中情况
	 */
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	private GridView mGridView;
	private List<String> list;
	protected LayoutInflater mInflater;

	
	public PhotoAdapter(Context context, List<String> list,GridView mGridView) {
		this.list = list;
        this.mGridView = mGridView;
        mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()	{
		return list.size();
	}

	@Override
	public Object getItem(int position)	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position){
		
		return position;
	}

	public void onRefresh(List<String> list)	{
		this.list = list;
		notifyDataSetInvalidated();
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		final ViewHolder viewHolder;
		String path = list.get(position);
		
		if (convertView == null)	{
			convertView = mInflater.inflate(R.layout.photo_home_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (PhotoImageView) convertView.findViewById(R.id.child_image);
			
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener()	{
				
				@Override
				public void onMeasureSize(int width, int height)	{
					mPoint.set(width, height);
					
				}
			});
			convertView.setTag(viewHolder);
	
		}	else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView.setImageResource(R.drawable.photo_picbg);
		}
		viewHolder.mImageView.setTag(path);
		viewHolder.mImageView.setOnClickListener(onClick);
		
		//利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack()	{

			@Override
			public void onImageLoader(Bitmap bitmap, String path)	{
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if (bitmap != null && mImageView != null)	{
					mImageView.setImageBitmap(bitmap);
				}
			}
			
		});
		if (bitmap != null)	{
			viewHolder.mImageView.setImageBitmap(bitmap);
		
		} else {
			viewHolder.mImageView.setImageResource(R.drawable.photo_picbg);
		}
		
		return convertView;
	}
	
	/**
	 * 获取选中的Item的position
	 * 
	 * @return
	 */
	public List<Integer> getSelectItems() {
		List<Integer> list = new ArrayList<Integer>();
		for ( Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator();  it.hasNext();){
			Map.Entry<Integer, Boolean> entry = it.next();
			if (entry.getValue())	{
				list.add(entry.getKey());
			}
		}
		return list;
	}
	
	public static class ViewHolder{
		public PhotoImageView mImageView;
	}
	
	public OnClickListener onClick = null;
	
}
