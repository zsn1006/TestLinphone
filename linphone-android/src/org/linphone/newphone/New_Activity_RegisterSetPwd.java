package org.linphone.newphone;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.linphone.R;
import org.linphone.newphone.tools.Constants;
import org.linphone.newphone.tools.HttpAsyncTask;
import org.linphone.newphone.tools.HttpAsyncTask.HttpCallBack;
import org.linphone.newphone.view.MyDialog_Text;
import org.linphone.newphone.view.MyDialog_Text.DialogTextCallBack;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class New_Activity_RegisterSetPwd extends BaseActivity {

	private TextView left_text;
	private TextView title;

	private EditText mEtUserName;
	private EditText mEtUserPwd1;
	private EditText mEtUserPwd2;
	private Button mBtnComplete;
	private View mLine;

	private String mRegeisterPhone;
	private String sendUserId;
	private Boolean mIsRegeister = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_regeister_setpwd);

		mRegeisterPhone = getIntent().getStringExtra("regeisterPhone");
		sendUserId = getIntent().getStringExtra("sendUserId");
		mIsRegeister = getIntent().getBooleanExtra("isRegeister", true);

		onInitTopBar();
		onInitControl();
	}

	private void onInitTopBar() {

		title = (TextView) findViewById(R.id.title_text);
		if (mIsRegeister) {
			title.setText("注册");
		} else if (!mIsRegeister) {
			title.setText("找回密码");
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

		mEtUserName = (EditText) findViewById(R.id.mEtUserName);
		mEtUserPwd1 = (EditText) findViewById(R.id.mEtUserPwd1);
		mEtUserPwd2 = (EditText) findViewById(R.id.mEtUserPwd2);
		mLine = (View) findViewById(R.id.mLine);

		if (mIsRegeister) {
			mEtUserName.setVisibility(View.VISIBLE);
			mLine.setVisibility(View.VISIBLE);
		} else {
			mEtUserName.setVisibility(View.GONE);
			mLine.setVisibility(View.GONE);
		}
		mBtnComplete = (Button) findViewById(R.id.mBtnComplete);
		mBtnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = mEtUserName.getText().toString();
				String pwd1 = mEtUserPwd1.getText().toString();
				String pwd2 = mEtUserPwd2.getText().toString();

				if (((name == null || name.length() <= 0)) && mIsRegeister) {
					mEtUserName.setError(Html
							.fromHtml("<font color=#ff0000>" + "请输入昵称" + "</font>"));
				} else if (pwd1 == null || pwd1.length() <= 0) {
					mEtUserPwd1.setError(Html
							.fromHtml("<font color=#ff0000>" + "请输入密码" + "</font>"));
				} else if (pwd1.length() < 6 || pwd1.length() > 16) {
					mEtUserPwd1.setError(Html.fromHtml("<font color=#ff0000>" + "请输入6-16位密码"
							+ "</font>"));
				} else if (pwd2 == null || pwd2.length() <= 0) {
					mEtUserPwd2.setError(Html.fromHtml("<font color=#ff0000>" + "请重新输入密码"
							+ "</font>"));
				} else if (pwd2.length() < 6 || pwd2.length() > 16) {
					mEtUserPwd2.setError(Html.fromHtml("<font color=#ff0000>" + "请输入6-16位密码"
							+ "</font>"));
				} else if (!pwd2.equals(pwd1)) {
					mEtUserPwd1.setError(Html.fromHtml("<font color=#ff0000>" + "两次密码不一致，请重新输入"
							+ "</font>"));
					mEtUserPwd1.requestFocus();
					mEtUserPwd1.setText("");
					mEtUserPwd2.setText("");
				} else {
					if (mIsRegeister) {
						onRegeisterTask(name, mRegeisterPhone, pwd1);
					} else {
						onModifyPwd(pwd1);
					}
				}
			}
		});
	}

	private void onRegeisterTask(String username, String phone, String pwd) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("account", phone));
		list.add(new BasicNameValuePair("password", pwd));
		list.add(new BasicNameValuePair("Alias", username));

		String path = Constants.netAddr + Constants.ip;
		CallbackUrl callback = new CallbackUrl();
		mAsyncTask = new HttpAsyncTask(this, path, list, callback, false);
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
				String pwd = mEtUserPwd1.getText().toString();
				doLoginTask(mRegeisterPhone, pwd);
			} else {
				Toast.makeText(New_Activity_RegisterSetPwd.this, "设置密码失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void doLoginTask(String phone, String pwd) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("account", phone));
		list.add(new BasicNameValuePair("password", pwd));

		String path = Constants.netAddr + Constants.ip;
		loginCallBack callback = new loginCallBack();
		mAsyncTask = new HttpAsyncTask(this, path, list, callback, true);
		mAsyncTask.execute("");
	}

	private class loginCallBack implements HttpCallBack {
		@Override
		public void setResult(String result) {
			if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
				mAsyncTask.cancel(true);
				mAsyncTask = null;
			}
			if (!result.equals("errorNet") && !result.contentEquals("errorData")) {

				String info = "";
				if (mIsRegeister) {
					info = "恭喜您，注册成功！";
				} else {
					info = "重置密码成功";
				}
				dialogCallBack dialogback = new dialogCallBack();
				MyDialog_Text dialog = new MyDialog_Text(New_Activity_RegisterSetPwd.this, info,
						"确定", null, dialogback);
				dialog.show();
			}
		}
	}

	public class dialogCallBack implements DialogTextCallBack {

		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
			finish();
		}
	}
	
	private void onModifyPwd(String pwd) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", sendUserId));
		list.add(new BasicNameValuePair("password", pwd));

		String path = Constants.netAddr + Constants.ip;
		CallbackModify callback = new CallbackModify();
		mAsyncTask = new HttpAsyncTask(this, path, list, callback, false);
		mAsyncTask.execute("");
	}

	private class CallbackModify implements HttpCallBack {

		@Override
		public void setResult(String result) {
			if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
				mAsyncTask.cancel(true);
				mAsyncTask = null;
			}
			if (!result.equals("errorNet") && !result.contentEquals("errorData")) {
				String pwd = mEtUserPwd1.getText().toString();
				doLoginTask(mRegeisterPhone, pwd);
			} else {
				Toast.makeText(New_Activity_RegisterSetPwd.this, "设置密码失败", Toast.LENGTH_SHORT).show();
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
