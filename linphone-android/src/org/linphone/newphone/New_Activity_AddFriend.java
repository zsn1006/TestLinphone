package org.linphone.newphone;

import org.linphone.R;
import org.linphone.newphone.New_Dialog_CreateAccount.createCallBack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class New_Activity_AddFriend extends BaseActivity {

	private TextView left_text;
	private TextView title_text;

	private TextView mTvSearch;
	private TextView mTvAddrbook;
	private TextView mTvNewAdd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_add_friend);

		onInitTopBar();
		onInitControl();
	}

	private void onInitTopBar() {

		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText(R.string.add_friend);

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
	}

	private void onInitControl() {

		mTvSearch = (TextView) findViewById(R.id.mTvSearch);
		mTvAddrbook = (TextView) findViewById(R.id.mTvAddrbook);
		mTvNewAdd = (TextView) findViewById(R.id.mTvNewAdd);

		mTvSearch.setOnClickListener(mOnClick);
		mTvAddrbook.setOnClickListener(mOnClick);
		mTvNewAdd.setOnClickListener(mOnClick);
	}

	private OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.mTvSearch:
//				startActivity(new Intent(New_Activity_AddFriend.this, New_Activity_AddSearch.class));
				Toast.makeText(New_Activity_AddFriend.this, "敬请期待", Toast.LENGTH_SHORT).show();
				break;
			case R.id.mTvAddrbook:
				Toast.makeText(New_Activity_AddFriend.this, "敬请期待", Toast.LENGTH_SHORT).show();
				break;
			case R.id.mTvNewAdd:
				/*//旧的
				String info = "新建联系人";
				New_Dialog_CreateAccount dialog = new New_Dialog_CreateAccount(New_Activity_AddFriend.this, info, "确定",
						"取消", callBack);
				dialog.show();*/
				//TODO
				//新UI，由弹出对话框新增变为activity页面
				Intent addContactsIntent = new Intent(New_Activity_AddFriend.this, New_Activity_Addcontacts.class);
				startActivity(addContactsIntent);
				break;
			}
		}
	};
	
	private createCallBack callBack = new createCallBack() {
		
		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
			
		}
	};
}
