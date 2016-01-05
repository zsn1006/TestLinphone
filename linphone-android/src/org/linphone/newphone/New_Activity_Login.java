package org.linphone.newphone;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.linphone.LinphoneManager;
import org.linphone.LinphonePreferences;
import org.linphone.R;
import org.linphone.LinphonePreferences.AccountBuilder;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneAddress.TransportType;
import org.linphone.newphone.New_Popwin_LoginHistory.CallbackPopWin;
import org.linphone.newphone.bean.UserInfo;
import org.linphone.newphone.tools.AnimationUtil;
import org.linphone.newphone.tools.Constants;
import org.linphone.newphone.tools.HttpAsyncTask;
import org.linphone.newphone.tools.HttpAsyncTask.HttpCallBack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class New_Activity_Login extends BaseActivity {

	private TextView title;

	private EditText mEtAccount = null;
	private EditText mEtPassword = null;
	private Button mBtnEnter = null;
	private TextView mTvRegister = null;
	private TextView mTvFindPwd = null;
	private ImageView mIvArrow = null;
	private Boolean mRotateFlag = false;

	@SuppressWarnings("unused")
	private New_Popwin_LoginHistory mPop;
	private ArrayList<UserInfo> mListRecords;

	private LinphonePreferences mPrefs;
	private boolean accountCreated = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_login);

		mPrefs = LinphonePreferences.instance();
		if (!readPreference()) {
			onInitControl();
			onSetControl();
		}
	}

	private void onInitControl() {

		title = (TextView) findViewById(R.id.title_text);
		title.setText(R.string.new_app_name);

		mEtAccount = (EditText) findViewById(R.id.mEtAccount);
		mEtPassword = (EditText) findViewById(R.id.mEtPassword);
		mBtnEnter = (Button) findViewById(R.id.mBtnEnter);
		mTvRegister = (TextView) findViewById(R.id.mTvRegister);
		mTvFindPwd = (TextView) findViewById(R.id.mTvFindPwd);
		mIvArrow = (ImageView) findViewById(R.id.mIvArrow);

		 mEtAccount.setText(Constants.userid);
		 mEtPassword.setText(Constants.password);
	}

	private OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.mBtnEnter:
				onEnterFunc();
				break;
			case R.id.mTvRegister:
				onEnterRegeister(true);
				break;
			case R.id.mTvFindPwd:
				onEnterRegeister(false);
				break;
			case R.id.mIvArrow:
				if (!mRotateFlag) {
					mRotateFlag = true;
					AnimationUtil.setRotateAnimation(mIvArrow, New_Activity_Login.this,
							R.anim.anim_rotate0);
					onPopShow();
				} else {
					mRotateFlag = false;
					AnimationUtil.setRotateAnimation(mIvArrow, New_Activity_Login.this,
							R.anim.anim_rotate1);
				}
				break;
			}
		}
	};

	private void onEnterRegeister(Boolean isRegeister) {

		Intent intent2 = new Intent(New_Activity_Login.this, New_Activity_RegisterAccount.class);
		intent2.putExtra("isRegeister", isRegeister);
		startActivity(intent2);
		// finish();
	}

	private void onSetControl() {

		mBtnEnter.setOnClickListener(mOnClick);
		mTvRegister.setOnClickListener(mOnClick);
		mTvFindPwd.setOnClickListener(mOnClick);
		mIvArrow.setOnClickListener(mOnClick);

		mTvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		mTvFindPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	private void onEnterFunc() {

		String name = mEtAccount.getText().toString();
		String pwd = mEtPassword.getText().toString();
		if (name == null || name.isEmpty()) {
			mEtAccount.requestFocus();
			mEtAccount.setError(Html.fromHtml("<font color=#ff0000>" + "请输入账号！" + "</font>"));
		} else if (pwd == null || pwd.isEmpty()) {
			mEtPassword.requestFocus();
			mEtPassword.setError(Html.fromHtml("<font color=#ff0000>" + "请输入密码！" + "</font>"));
		} else {
			writePreference();
			startActivity(new Intent(this, New_Activity_Main.class));
			finish();
//			onLogin();
		}
	}
	
	private void writePreference() {
		
		SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = mPref.edit();
		
		editor.putString(getString(R.string.pref_username_key), mEtAccount.getText().toString());
		editor.putString(getString(R.string.pref_passwd_key), mEtPassword.getText().toString());
		editor.putString(getString(R.string.pref_domain_key), Constants.ip);
		editor.commit();
		
//		startService(new Intent().setClass(this, LinphoneService.class));
//		LinphoneService.instance().setActivityToLaunchOnIncomingReceived(New_Activity_Main.class);
		
		Constants.userid = mEtAccount.getText().toString();
		logIn(mEtAccount.getText().toString(), mEtPassword.getText().toString(), Constants.ip, false);
	}
	
	private Boolean readPreference() {

		if (!LinphonePreferences.instance().isFirstLaunch()) {
			SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
			String account = mPref.getString(getString(R.string.pref_username_key), "");
			String domain = mPref.getString(getString(R.string.pref_domain_key), "");
			
			if (account.length() > 0 && domain.length() > 0) {

				Constants.userid = account;
				Intent intent = new Intent(New_Activity_Login.this, New_Activity_Main.class);
				startActivity(intent);
				finish();
				return true;
			}
		}
		return false;
	}
	
	private void logIn(String username, String password, String domain, boolean sendEcCalibrationResult) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}

        saveCreatedAccount(username, password, domain);

		if (LinphoneManager.getLc().getDefaultProxyConfig() != null) {
			launchEchoCancellerCalibration(sendEcCalibrationResult);
		}
	}
	
	public void saveCreatedAccount(String username, String password, String domain) {
		if (accountCreated)
			return;

		if(username.startsWith("sip:")) {
			username = username.substring(4);
		}

		if (username.contains("@"))
			username = username.split("@")[0];

		if(domain.startsWith("sip:")) {
			domain = domain.substring(4);
		}

		String identity = "sip:" + username + "@" + domain;
		try {
			LinphoneCoreFactory.instance().createLinphoneAddress(identity);
		} catch (LinphoneCoreException e) {
			e.printStackTrace();
		}
		boolean isMainAccountLinphoneDotOrg = domain.equals(getString(R.string.default_domain));
		boolean useLinphoneDotOrgCustomPorts = getResources().getBoolean(R.bool.use_linphone_server_ports);
		AccountBuilder builder = new AccountBuilder(LinphoneManager.getLc())
		.setUsername(username)
		.setDomain(domain)
		.setPassword(password);
		
		if (isMainAccountLinphoneDotOrg && useLinphoneDotOrgCustomPorts) {
			if (getResources().getBoolean(R.bool.disable_all_security_features_for_markets)) {
				builder.setProxy(domain + ":5228")
				.setTransport(TransportType.LinphoneTransportTcp);
			}
			else {
				builder.setProxy(domain + ":5223")
				.setTransport(TransportType.LinphoneTransportTls);
			}
			
			builder.setExpires("604800")
			.setOutboundProxyEnabled(true)
			.setAvpfEnabled(true)
			.setAvpfRRInterval(3)
			.setQualityReportingCollector("sip:voip-metrics@sip.linphone.org")
			.setQualityReportingEnabled(true)
			.setQualityReportingInterval(180)
			.setRealm("sip.linphone.org");
			
			
			mPrefs.setStunServer(getString(R.string.default_stun));
			mPrefs.setIceEnabled(true);
		} else {
			String forcedProxy = getResources().getString(R.string.setup_forced_proxy);
			if (!TextUtils.isEmpty(forcedProxy)) {
				builder.setProxy(forcedProxy)
				.setOutboundProxyEnabled(true)
				.setAvpfRRInterval(5);
			}
		}
		
		if (getResources().getBoolean(R.bool.enable_push_id)) {
			String regId = mPrefs.getPushNotificationRegistrationID();
			String appId = getString(R.string.push_sender_id);
			if (regId != null && mPrefs.isPushNotificationEnabled()) {
				String contactInfos = "app-id=" + appId + ";pn-type=google;pn-tok=" + regId;
				builder.setContactParameters(contactInfos);
			}
		}
		
		try {
			builder.saveNewAccount();
			accountCreated = true;
		} catch (LinphoneCoreException e) {
			e.printStackTrace();
		}
	}
	
	private void launchEchoCancellerCalibration(boolean sendEcCalibrationResult) {
		boolean needsEchoCalibration = LinphoneManager.getLc().needsEchoCalibration();
		if (needsEchoCalibration && mPrefs.isFirstLaunch()) {
			//We'll enable it after the echo calibration
			mPrefs.setAccountEnabled(mPrefs.getAccountCount() - 1, false);
		} else {
			mPrefs.firstLaunchSuccessful();
			New_Activity_Main.instance().isNewProxyConfig();
			finish();
		}		
	}

	/**
	 * 登录
	 */
	private void onLogin() {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("account", mEtAccount.getText().toString()));
		list.add(new BasicNameValuePair("password", mEtPassword.getText().toString()));

		String path = Constants.netAddr + Constants.mCurVersion;
		CallbackUrl callback = new CallbackUrl();
		mAsyncTask = new HttpAsyncTask(this, path, list, callback, true);
		mAsyncTask.execute("");
	}

	private class CallbackUrl implements HttpCallBack {

		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
			if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
				mAsyncTask.cancel(true);
				mAsyncTask = null;
			}

			if (!result.equals("errorNet") && !result.contentEquals("errorData")) {
				finish();
			} else {
				Toast.makeText(New_Activity_Login.this, "登录失败，请重新登录！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void onPopShow() {

		mListRecords = New_Application.getInstance().getLoginRecords();
		if (mListRecords != null && mListRecords.size() > 0) {
			mPop = new New_Popwin_LoginHistory(this, mListRecords, mEtAccount, callPopWin);
		} else {
			Toast.makeText(this, "没有历史登录账号", Toast.LENGTH_SHORT).show();
		}
	}

	private CallbackPopWin callPopWin = new CallbackPopWin() {

		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.equals("dismiss")) {
					mRotateFlag = false;
					AnimationUtil.setRotateAnimation(mIvArrow, New_Activity_Login.this,
							R.anim.anim_rotate1);
				} else {
					Toast.makeText(New_Activity_Login.this, "===" + result, Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	};

}
