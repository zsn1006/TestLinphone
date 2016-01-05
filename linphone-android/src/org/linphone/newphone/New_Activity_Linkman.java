package org.linphone.newphone;

import java.util.ArrayList;

import org.linphone.R;
import org.linphone.newphone.adapter.LinkmanAdapter1;
import org.linphone.newphone.adapter.LinkmanIndexAdapter;
import org.linphone.newphone.bean.ContactSort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class New_Activity_Linkman extends BaseActivity {

	private TextView left_text;
	private TextView title_text;
	private TextView right_text;

	private EditText mEtSearch;
	private ListView mListView;
	private ListView mListIndex;
	private LinkmanIndexAdapter mIndexAdapter;
	private LinkmanAdapter1 mLinkmanAdapter;
	private Boolean mIsClick = false;
	private ArrayList<ContactSort> mDataList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_linkman);
		
		onInitTopBar();
		onInitList();
		onInitIndex();
	}

	private void onInitTopBar() {

		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText(R.string.new_app_name);

		left_text = (TextView) findViewById(R.id.left_text);
		left_text.setVisibility(View.VISIBLE);
		left_text.setText(R.string.new_app_back);
		left_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.new_back_icon, 0, 0, 0);
		left_text.setCompoundDrawablePadding(12);
		left_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		right_text = (TextView) findViewById(R.id.right_text);
		right_text.setVisibility(View.VISIBLE);
		right_text.setText(R.string.new_add);
		right_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_add, 0);
		right_text.setCompoundDrawablePadding(0);
		right_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(New_Activity_Linkman.this, New_Activity_AddFriend.class);
				startActivityForResult(in, 1736);
			}
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1736) {
			mDataList = New_Application.getInstance().getContactSort();
			mLinkmanAdapter.notifyDataSetChanged();
			mIndexAdapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void onInitList() {

		mDataList = New_Application.getInstance().getContactSort();
		
		mListView = (ListView) findViewById(R.id.mListView);
		mLinkmanAdapter = new LinkmanAdapter1(this, mDataList);
		mListView.setAdapter(mLinkmanAdapter);

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (!mIsClick) {
					// 点击索引项时不执行
					if (mIndexAdapter != null) {
						mIndexAdapter.onSetPosition(firstVisibleItem);
						mListIndex.setSelection(firstVisibleItem);
					}
				}
				mIsClick = false;
			}
		});
	}

	private void onInitIndex() {

		mListIndex = (ListView) findViewById(R.id.mListIndex);
		mIndexAdapter = new LinkmanIndexAdapter(this, mDataList);
		mListIndex.setAdapter(mIndexAdapter);

		mListIndex.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (mListView != null) {
					mListView.setSelection(position);
				}
				mIsClick = true;
				mIndexAdapter.onSetPosition(position);
			}
		});
	}
	
}
