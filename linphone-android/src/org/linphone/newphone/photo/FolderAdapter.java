package org.linphone.newphone.photo;

import java.util.List;

import org.linphone.R;
import org.linphone.newphone.photo.NativeImageLoader.NativeImageCallBack;
import org.linphone.newphone.photo.PhotoImageView.OnMeasureListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter{
	private List<FolderBean> list;
	private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
	private ListView mListView;
	protected LayoutInflater mInflater;
	private int mPosition = 0;

	@Override
	public int getCount()	{
		return list.size();
	}

	@Override
	public Object getItem(int position)	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)	{
		return position;
	}

	public FolderAdapter(Context context, List<FolderBean> list,	ListView listView, int position)	{
        this.list = list;
        this.mListView = listView;
        mPosition = position;
        mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)	{
		final ViewHolder viewHolder;
		FolderBean mImageBean = list.get(position);
		String path = mImageBean.getTopImagePath();
		if (convertView == null)	{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.photo_folder_item, null);
			viewHolder.mImageView = (PhotoImageView) convertView.findViewById(R.id.mImageView);
			viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.mTvTitle);
			viewHolder.mTvCount = (TextView) convertView.findViewById(R.id.mTvCount);
			viewHolder.mIvFlag = (ImageView) convertView.findViewById(R.id.mIvFlag);
			
			// 用来监听ImageView的宽和高
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
		String name = mImageBean.getFolderName();
		if (name.contentEquals("aaa"))	{
			name = "所有图片";
		}
		viewHolder.mTvTitle.setText(name);
		viewHolder.mTvCount.setText(Integer.toString(mImageBean.getImageCounts()) + " 张");
		// 给ImageView设置路径Tag,这是异步加载图片的小技巧
		viewHolder.mImageView.setTag(path);
		// 利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack()	{
			
			@Override
			public void onImageLoader(Bitmap bitmap, String path)	{
				ImageView mImageView = (ImageView) mListView.findViewWithTag(path);
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
		if (mPosition == position)	{
			viewHolder.mIvFlag.setVisibility(View.VISIBLE);
	
		} else {
			viewHolder.mIvFlag.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	

	public static class ViewHolder	{
		public PhotoImageView mImageView;
		public TextView mTvTitle;
		public TextView mTvCount;
		public ImageView mIvFlag;
	}

	
}
