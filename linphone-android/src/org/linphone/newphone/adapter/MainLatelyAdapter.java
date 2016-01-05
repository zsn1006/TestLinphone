package org.linphone.newphone.adapter;

import java.util.ArrayList;

import org.linphone.R;
import org.linphone.newphone.bean.CallRecords;
import org.linphone.newphone.bean.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainLatelyAdapter extends BaseAdapter {

	private ArrayList<CallRecords> list;
	private Context mContext;

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public MainLatelyAdapter(Context context, ArrayList<CallRecords> list) {
		this.list = list;
		mContext = context;
	}

	public void onRefresh(ArrayList<CallRecords> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_in_main_item, null);
			viewHolder.mIconUser = (ImageView) convertView.findViewById(R.id.mIconUser);
			viewHolder.mIconPhone = (ImageView) convertView.findViewById(R.id.mIconPhone);
			viewHolder.mIconVideo = (ImageView) convertView.findViewById(R.id.mIconVideo);
			viewHolder.mDirection = (ImageView) convertView.findViewById(R.id.mDirection);
			viewHolder.mTvName = (TextView) convertView.findViewById(R.id.mTvName);
			viewHolder.mTvTime = (TextView) convertView.findViewById(R.id.mTvTime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mIconUser.setImageResource(R.drawable.new_usericon_default);
		}

		CallRecords call = list.get(position);
		if (call != null) {
			viewHolder.mPosition = position;
			Contact contact = call.getContact();
			if (contact != null) {
				String name = contact.getName();
				String icon = contact.getIcon();
				if (name != null && !name.equals("null") && name.length() > 0) {
					viewHolder.mTvName.setText(name);
				} else {
					viewHolder.mTvName.setText(call.getAccount());
				}
			} else {
				viewHolder.mTvName.setText(call.getAccount());
			}
			viewHolder.mTvTime.setText(call.getStartTime());

			int type = call.getCallType();
			if (type == 1) {
				viewHolder.mIconPhone.setVisibility(View.GONE);
				viewHolder.mIconVideo.setVisibility(View.VISIBLE);
			} else if (type == 2) {
				viewHolder.mIconPhone.setVisibility(View.VISIBLE);
				viewHolder.mIconVideo.setVisibility(View.GONE);
			} else {
				viewHolder.mIconPhone.setVisibility(View.GONE);
				viewHolder.mIconVideo.setVisibility(View.GONE);
			}
			
			int direction = call.getIncoming();
			if (direction == 1) {
				viewHolder.mDirection.setImageResource(R.drawable.icon_incoming);
			} else {
				viewHolder.mDirection.setImageResource(R.drawable.icon_outgoing);
			}
		}
		return convertView;
	}

	public static class ViewHolder {

		public TextView mTvName;
		public TextView mTvTime;
		public ImageView mIconUser;
		public ImageView mIconPhone;
		public ImageView mIconVideo;
		public ImageView mDirection;
		public int mPosition;
	}

}
