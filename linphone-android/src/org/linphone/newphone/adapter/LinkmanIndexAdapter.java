package org.linphone.newphone.adapter;

import java.util.ArrayList;

import org.linphone.R;
import org.linphone.newphone.bean.ContactSort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class LinkmanIndexAdapter extends BaseAdapter {

	private Context mContext;
	private int mPosition = 0;
	private ArrayList<ContactSort> mList;

	public LinkmanIndexAdapter(Context c, ArrayList<ContactSort> list) {
		mContext = c;
		mList = list;
		notifyDataSetInvalidated();
	}

	@Override
	public int getCount() {
		return (mList == null) ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void onSetPosition(int position) {
		mPosition = position;
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_index_item, null);
			holder = new ViewHolder();
			holder.mTvIndex = (TextView) convertView.findViewById(R.id.mTvIndex);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < mList.size()) {

			String title = mList.get(position).getTitle();
			holder.mTvIndex.setText(title);

			if (position == mPosition) {
				holder.mTvIndex.setBackgroundColor(mContext.getResources().getColor(R.color.index1));
			} else {
				holder.mTvIndex.setBackgroundColor(mContext.getResources().getColor(R.color.index0));
			}
		}

		return convertView;
	}

	private class ViewHolder {
		TextView mTvIndex;
	}

}
