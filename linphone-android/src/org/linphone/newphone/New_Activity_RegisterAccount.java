package org.linphone.newphone;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.linphone.R;
import org.linphone.newphone.tools.Constants;
import org.linphone.newphone.tools.HttpAsyncTask;
import org.linphone.newphone.tools.HttpAsyncTask.HttpCallBack;
import org.linphone.newphone.tools.MethodPhone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class New_Activity_RegisterAccount extends BaseActivity {

	private TextView left_text;
	private TextView title;

	private EditText mEtPhone;
	private Button mBtnNext;
	private Boolean mIsRegeister = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_regeister_account);

		mIsRegeister = getIntent().getBooleanExtra("isRegeister", true);
		onInitTopBar();
		onInitControl();
	}

	private void onInitTopBar() {

		title = (TextView) findViewById(R.id.title_text);
		if (mIsRegeister) {
			title.setText(R.string.login_register);
		} else if (!mIsRegeister) {
			title.setText(R.string.login_findPwd);
		}

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

		mEtPhone = (EditText) findViewById(R.id.mEtPhone);
		mBtnNext = (Button) findViewById(R.id.mBtnNext);
		mBtnNext.setOnClickListener(onClick);
		if (!mIsRegeister) {
			mEtPhone.setHint("请输入您注册的手机号");
		}
	}

	private OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.mBtnNext:
				String phone = mEtPhone.getText().toString();
				if (mIsRegeister) {
					if (phone == null || phone.length() <= 0) {
						mEtPhone.setError(Html.fromHtml("<font color=#ff0000>" + "请输入手机号"
								+ "</font>"));
					} else if (!MethodPhone.isMobileNum(phone)) {
						mEtPhone.setError(Html.fromHtml("<font color=#ff0000>" + "手机号格式不正确"
								+ "</font>"));
					} else {
						onCheckMobile(phone);
					}
				} else if (!mIsRegeister) {
					if (MethodPhone.isMobileNum(phone)) {
						onCheckMobile(phone);
					} else {
						mEtPhone.setError(Html.fromHtml("<font color=#ff0000>" + "手机号格式不正确"
								+ "</font>"));
					}
				}
				break;
			}
		}
	};

	private void onCheckMobile(String mobile) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("account", mEtPhone.getText().toString()));

		String path = Constants.netAddr + Constants.ip;
		CallbackUrl callback = new CallbackUrl();
		mAsyncTask = new HttpAsyncTask(this, path, list, callback, true);
		mAsyncTask.onSetBackData(true);
		mAsyncTask.execute("");
	}

	private class CallbackUrl implements HttpCallBack {
		@Override
		public void setResult(String result) {
			if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
				mAsyncTask.cancel(true);
				mAsyncTask = null;
			}

			if (!result.equals("errorNet") && !result.contentEquals("errorData")) {
				try {
					JSONObject step0 = new JSONObject(result);
					JSONArray GroupArray = step0.getJSONArray("Data");
					JSONObject step1 = (JSONObject) GroupArray.opt(0);
					String state = step1.getString("VerifyState");
					if (mIsRegeister) {
						if (state.equals("0")) {
							onEnterAuthCode(null);
						} else {
							Toast.makeText(New_Activity_RegisterAccount.this, "该手机号已被注册", Toast.LENGTH_SHORT)
							.show();
						}
					} else if (!mIsRegeister) {
						if (state.equals("0")) {
							Toast.makeText(New_Activity_RegisterAccount.this, "该手机号尚未注册过", Toast.LENGTH_SHORT)
									.show();
						} else {
							String userid = step1.getString("id");
							onEnterAuthCode(userid);
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void onEnterAuthCode(String userid) {

		Intent in = new Intent(New_Activity_RegisterAccount.this, New_Activity_RegisterAuthCode.class);
		in.putExtra("regeisterPhone", mEtPhone.getText().toString());
		in.putExtra("isRegeister", mIsRegeister);
		in.putExtra("sendUserId", userid);
		startActivity(in);
		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
