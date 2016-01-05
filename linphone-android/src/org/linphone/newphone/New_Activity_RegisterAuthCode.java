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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class New_Activity_RegisterAuthCode extends BaseActivity {

	private TextView left_text;
	private TextView title;
	private TextView right_text;

	private TextView mTvPhone;
	private EditText mEtAuthCode;
	private Button mBtnNext;

	private String mRegeisterPhone;
	private String mStrAuthCode;
	private String sendUserId;
	private int mTimeCnt = 0;
	private Boolean mIsRegeister = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_regeister_authcode);

		mRegeisterPhone = getIntent().getStringExtra("regeisterPhone");
		sendUserId = getIntent().getStringExtra("sendUserId");
		mIsRegeister = getIntent().getBooleanExtra("isRegeister", true);

		onInitTopBar();
		onInitControl();
		sendAuthNumber();
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

		right_text = (TextView) findViewById(R.id.right_text);
		right_text.setBackgroundColor(0xffffffff);
		right_text.setVisibility(View.VISIBLE);
		right_text.setOnClickListener(onClick);
	}

	private void onInitControl() {

		String info = "验证码已发送至您的手机 " + mRegeisterPhone.substring(0, 3) + "****"
				+ mRegeisterPhone.substring(7);
		mTvPhone = (TextView) findViewById(R.id.mTvPhone);
		mTvPhone.setText(info);
		mTvPhone.setTextColor(0xffffffff);

		mEtAuthCode = (EditText) findViewById(R.id.mEtAuthCode);
		mBtnNext = (Button) findViewById(R.id.mBtnNext);
		mBtnNext.setOnClickListener(onClick);
	}

	private OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.mBtnNext:
				String code = mEtAuthCode.getText().toString();
				if (code != null && code.equals(mStrAuthCode)) {
					onEnterSetPwd();
				} else {
					Toast.makeText(New_Activity_RegisterAuthCode.this, "验证码不正确，请重新输入！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.right_text:
				sendAuthNumber();
				break;
			}
		}
	};

	private void onEnterSetPwd() {

		Intent in = new Intent(New_Activity_RegisterAuthCode.this, New_Activity_RegisterSetPwd.class);
		in.putExtra("regeisterPhone", mRegeisterPhone);
		in.putExtra("isRegeister", mIsRegeister);
		in.putExtra("sendUserId", sendUserId);
		startActivity(in);
		finish();
	}

	private void sendAuthNumber() {

		onSendMobileMes(mRegeisterPhone);
		time(60);
	}

	private void time(int start) {

		mTimeCnt = start;
		right_text.setText("获取中(" + mTimeCnt + "s)");
		right_text.setTextColor(getResources().getColor(R.color.text_black));
		right_text.setClickable(false);

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						mTimeCnt--;
						handler.sendEmptyMessage(10);
						if (mTimeCnt <= 0) {
							handler.sendEmptyMessage(20);
							break;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				right_text.setText("获取中(" + mTimeCnt + "s)");
				break;
			case 20:
				right_text.setText(R.string.regeister_reGetCode);
				right_text.setTextColor(getResources().getColor(R.color.black));
				right_text.setClickable(true);
				break;
			case 30:
				time(60);
				break;
			}
		};
	};

	protected void onSendMobileMes(String phonNum) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("phone", phonNum));
		
		String path = Constants.netAddr + Constants.ip;
		AuthCodeCallBack jsonCallback = new AuthCodeCallBack();
		mAsyncTask = new HttpAsyncTask(getBaseContext(), path, list, jsonCallback, false);
		mAsyncTask.execute("");
	}

	private class AuthCodeCallBack implements HttpCallBack {
		@Override
		public void setResult(String result) {
			if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
				mAsyncTask.cancel(true);
				mAsyncTask = null;
			}
			// 验证码发送成功，返回验证码的判断
			if (!result.equals("errorNet") && !result.contentEquals("errorData")) {
				try {
					JSONObject obj = new JSONObject(result);
					JSONArray object = obj.getJSONArray("Data");
					mStrAuthCode = object.getJSONObject(0).getString("VerifyCode");
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else if (!result.equals("errorNet")) {
				Toast.makeText(New_Activity_RegisterAuthCode.this, "验证码发送失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
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
