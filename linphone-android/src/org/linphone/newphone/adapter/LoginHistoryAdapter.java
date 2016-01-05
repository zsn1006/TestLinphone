package org.linphone.newphone.adapter;

import java.util.ArrayList;

import org.linphone.R;
import org.linphone.newphone.New_Popwin_LoginHistory;
import org.linphone.newphone.bean.UserInfo;
import org.linphone.newphone.db.LoginRecordsDao;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginHistoryAdapter extends BaseAdapter {

	private ArrayList<UserInfo> list;
	private Context mContext;
	private Handler mHandler;
	private int mListCnt = 0;

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

	public LoginHistoryAdapter(Context context, ArrayList<UserInfo> list, Handler handler) {
		this.list = list;
		mContext = context;
		mHandler = handler;
		mListCnt = (list == null ? 0 : list.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_login_popwin_item, null);
			viewHolder.mIconUser = (ImageView) convertView.findViewById(R.id.mIconUser);
			viewHolder.mIconDelete = (ImageView) convertView.findViewById(R.id.mIconDelete);
			viewHolder.mTvAccount = (TextView) convertView.findViewById(R.id.mTvAccount);
			viewHolder.mTvNickname = (TextView) convertView.findViewById(R.id.mTvNickname);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mIconUser.setImageResource(R.drawable.new_usericon_default);
		}
		
		UserInfo call = list.get(position);
		if (call != null) {
			viewHolder.mPosition = position;
			viewHolder.mTvNickname.setText(call.getName());
			viewHolder.mTvAccount.setText(call.getAccount());
			
			String icon = call.getIcon();
			
			viewHolder.mIconDelete.setTag(viewHolder);
			viewHolder.mIconDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ViewHolder holder = (ViewHolder)arg0.getTag();
					if (holder != null) {
						list.remove(holder.mPosition);
						notifyDataSetChanged();
						UserInfo call = list.get(holder.mPosition);
						LoginRecordsDao.getInstance().deleteById(call.getId());
						
						if (mListCnt > New_Popwin_LoginHistory.mListCount && list.size() == New_Popwin_LoginHistory.mListCount) {
							Message msg = new Message();
							msg.what = New_Popwin_LoginHistory.mListCount;
							msg.obj = "set";
							mHandler.sendMessage(msg);
						}
						if (mListCnt > 0 && list.size() == 0) {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = "dismiss";
							mHandler.sendMessage(msg);
						}
					}
				}
			});
		}
		return convertView;
	}

	public static class ViewHolder {

		public TextView mTvNickname;
		public TextView mTvAccount;
		public ImageView mIconUser;
		public ImageView mIconDelete;
		public int mPosition;
	}

}
