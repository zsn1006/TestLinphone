package org.linphone.newphone.adapter;

import java.util.ArrayList;

import org.linphone.R;
import org.linphone.newphone.bean.ContactSort;
import org.linphone.newphone.view.MyListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LinkmanAdapter1 extends BaseAdapter {

	private Context mContext;
	private ArrayList<ContactSort> mList;

	public LinkmanAdapter1(Context c, ArrayList<ContactSort> list) {
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

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_in_linkman_item1, null);
			holder = new ViewHolder();
			holder.mTvTitle = (TextView) convertView.findViewById(R.id.mTvTitle);
			holder.mListView = (MyListView) convertView.findViewById(R.id.mListView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < mList.size()) {

			holder.position = position;
			String title = mList.get(position).getTitle();
			holder.mTvTitle.setText(title);

			LinkmanAdapter2 adapter = new LinkmanAdapter2(mContext, mList.get(position).getContacts());
			holder.mListView.setAdapter(adapter);
			holder.mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					ViewHolder holder = (ViewHolder) parent.getTag();
					if (holder != null) {
						
					}
				}
			});

			holder.mListView.setTag(holder);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView mTvTitle;
		MyListView mListView;
		int position;
	}

}
