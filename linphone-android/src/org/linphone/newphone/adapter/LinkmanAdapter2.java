package org.linphone.newphone.adapter;

import java.util.ArrayList;

import org.linphone.LinphoneManager;
import org.linphone.R;
import org.linphone.newphone.New_Activity_DetailedInformation;
import org.linphone.newphone.bean.Contact;
import org.linphone.ui.AddressText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LinkmanAdapter2 extends BaseAdapter {

	private Context mContext;
	private ArrayList<Contact> mList;
	private String GetPositionAccount;
	private String GetPositionNickName;

	public LinkmanAdapter2(Context c, ArrayList<Contact> list) {
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
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.new_in_linkman_item2, null);
			holder = new ViewHolder();
			holder.mTvNickname = (TextView) convertView.findViewById(R.id.mTvNickname);
			holder.mTvAccount = (TextView) convertView.findViewById(R.id.mTvAccount);
			holder.mIconUser = (ImageView) convertView.findViewById(R.id.mIconUser);
			holder.linkLayout = (RelativeLayout)convertView.findViewById(R.id.linkLayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < mList.size()) {
			String name = mList.get(position).getName();
		    String account = mList.get(position).getAccount();
			String icon = mList.get(position).getIcon();

			holder.mTvNickname.setText(name);
			holder.mTvAccount.setText(account);
			
			holder.position = position;
			holder.linkLayout.setTag(holder);
			holder.linkLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ViewHolder holder = (ViewHolder)arg0.getTag();
					if (holder != null) {
						AddressText at = new AddressText(mContext, null);
						at.setText(mList.get(holder.position).getAccount());
						LinphoneManager.getInstance().newOutgoingCall(at);
					}
				}
			});
			
			//Add 2015-12-22
			holder.linkLayout.setOnLongClickListener(new OnLongClickListener()
			{
				
				@Override
				public boolean onLongClick(View v)
				{
					// TODO Auto-generated method stub
					ViewHolder holder = (ViewHolder)v.getTag();
					if (holder != null)
					{
						//Toast.makeText(mContext, "test", Toast.LENGTH_SHORT).show();
						Intent detail_intent = new Intent(mContext, New_Activity_DetailedInformation.class);
					
						//将用户账户号码、昵称，传递到个人详情界面
						Bundle bundle_account = new Bundle();
						GetPositionAccount = mList.get(holder.position).getAccount();
						GetPositionNickName = mList.get(holder.position).getName();
						bundle_account.putString("bundle_account", GetPositionAccount);
						bundle_account.putString("bundle_nick_name", GetPositionNickName);
						detail_intent.putExtras(bundle_account);
						
						mContext.startActivity(detail_intent);
						
					}
					return true;
				}
			});
			
		}

		return convertView;
	}

	private class ViewHolder {
		RelativeLayout linkLayout;
		TextView mTvNickname;
		TextView mTvAccount;
		ImageView mIconUser;
		int position;
	}

}
