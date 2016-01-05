package org.linphone.newphone;

import java.util.List;

import org.linphone.LinphoneManager;
import org.linphone.LinphoneUtils;
import org.linphone.R;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreListenerBase;
import org.linphone.core.LinphoneCall.State;
import org.linphone.mediastream.Log;
import org.linphone.newphone.New_Fragment_Video.SwitchWindow1;
import org.linphone.newphone.New_Fragment_Video2.SwitchWindow2;
import org.linphone.newphone.bean.Contact;
import org.linphone.newphone.db.ContactsDao;
import org.linphone.newphone.db.saveRecordDao;
import org.linphone.newphone.tools.Constants;
import org.linphone.newphone.tools.MethodBitmap;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class New_Activity_Incoming extends FragmentActivity {

	private ImageView mWave1, mWave2, mWave3;
	private AnimationSet mAnimationSet1, mAnimationSet2, mAnimationSet3;
	private ImageView mIncomeIcon;
	private TextView mIncomeName, mIncomeInfo;
	private Button mBtnAnswer, mBtnRecord;
	private ImageButton mBtnHangup;
	private Chronometer mCallTimer;

	private static final int OFFSET = 600; // 每个动画的播放时间间隔
	private static final int MSG_WAVE2_ANIMATION = 2;
	private static final int MSG_WAVE3_ANIMATION = 3;

	private static New_Activity_Incoming instance;
	private LinphoneCall mCall;
	private LinphoneCoreListenerBase mListener;
	private String Blackaccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_incoming);

		instance = this;
		onInitAnimation();
		onListener();
	}

	private void onInitAnimation() {

		mIncomeIcon = (ImageView) findViewById(R.id.mIncomeIcon);
		mIncomeName = (TextView) findViewById(R.id.mIncomeName);
		mIncomeInfo = (TextView) findViewById(R.id.mIncomeInfo);
		mCallTimer = (Chronometer) findViewById(R.id.mCallTimer);
		mBtnHangup = (ImageButton) findViewById(R.id.mBtnHangup);
		mBtnAnswer = (Button) findViewById(R.id.mBtnAnswer);
		mBtnRecord = (Button) findViewById(R.id.mBtnRecord);
		mBtnHangup.setOnClickListener(mOnClick);
		mBtnAnswer.setOnClickListener(mOnClick);
		mBtnRecord.setOnClickListener(mOnClick);
		mBtnAnswer.setVisibility(View.VISIBLE);

		mWave1 = (ImageView) findViewById(R.id.wave1);
		mWave2 = (ImageView) findViewById(R.id.wave2);
		mWave3 = (ImageView) findViewById(R.id.wave3);

		mAnimationSet1 = initAnimationSet((long) (OFFSET * 6));
		mAnimationSet2 = initAnimationSet((long) (OFFSET * 6));
		mAnimationSet3 = initAnimationSet((long) (OFFSET * 6));

		mWave1.startAnimation(mAnimationSet1);
		mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, (long) (OFFSET * 1));
		mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, (long) (OFFSET * 2));
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_WAVE2_ANIMATION:
				mWave2.startAnimation(mAnimationSet2);
				break;
			case MSG_WAVE3_ANIMATION:
				mWave3.startAnimation(mAnimationSet3);
				break;
			default:
				break;
			}
		}
	};

	private AnimationSet initAnimationSet(long offset) {
		// TODO Auto-generated method stub
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 3.0f, 1f, 3.0f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(offset);
		scaleAnimation.setRepeatCount(AnimationSet.INFINITE); // 设置循环
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.1f);
		alphaAnimation.setDuration(offset);
		alphaAnimation.setRepeatCount(Animation.INFINITE); // 设置循环
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);
		return animationSet;
	}

	/**
	 * 关闭动画
	 */
	private void clearWaveAnimation() {
		if (mWave1 != null) {
			mWave1.clearAnimation();
		}
		if (mWave2 != null) {
			mWave2.clearAnimation();
		}
		if (mWave3 != null) {
			mWave3.clearAnimation();
		}
	}

	private OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.mBtnHangup:
				decline();
				break;
			case R.id.mBtnAnswer:
				mBtnAnswer.setVisibility(View.GONE);
				mBtnRecord.setVisibility(View.VISIBLE);
				answer();
				break;
			case R.id.mBtnRecord:
				saveRecordDao.getInstance().onStartRecording(null, false);
				break;
			}
		}
	};

	public static final boolean isInstanciated() {
		return instance != null;
	}

	public static final New_Activity_Incoming instance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new New_Activity_Incoming();
			return instance;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		instance = null;
		clearWaveAnimation();
		Log.i("==========onDestroy=====incoming==");
		saveRecordDao.getInstance().onStopRecording();
		super.onDestroy();
		System.gc();
	}

	@Override
	protected void onResume() {
		instance = this;
		LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
		if (lc != null) {
			lc.addListener(mListener);
		}
		if (1 == onGetLinphoneCall()) {
			//onSetNameAndIcon();
			//TODO 2015-12-25	
			LinphoneAddress address = mCall.getRemoteAddress();
			String username = address.getUserName();
			Contact contact = ContactsDao.getInstance().readContacts(username);
			if (contact != null)
			{
			   Blackaccount = contact.getAccount();
			}

			SharedPreferences getBlackList = getSharedPreferences(Constants.mSpBlackList, MODE_PRIVATE);
			Boolean BlackListState = getBlackList.getBoolean(Blackaccount, false);
			if (BlackListState)
			{
				Toast.makeText(New_Activity_Incoming.this, "您有黑名单来电已被拦截", 1000).show();
				LinphoneManager.getLc().terminateConference();
				finish();
			}
			else {
				onSetNameAndIcon();
			}
			
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
		if (lc != null) {
			lc.removeListener(mListener);
		}
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (LinphoneManager.isInstanciated()
				&& (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)) {
			LinphoneManager.getLc().terminateCall(mCall);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private int onGetLinphoneCall() {

		if (LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
			List<LinphoneCall> calls = LinphoneUtils.getLinphoneCalls(LinphoneManager.getLc());
			for (LinphoneCall call : calls) {
				if (State.IncomingReceived == call.getState()) {
					mCall = call;
					break;
				}
			}
		}
		if (mCall == null) {
			Log.e("Couldn't find incoming call");
			finish();
			return 0;
		}
		return 1;
	}

	private void onSetNameAndIcon() {

		LinphoneAddress address = mCall.getRemoteAddress();
		String username = address.getUserName();
		Contact contact = ContactsDao.getInstance().readContacts(username);
		if (contact == null) {
			mIncomeName.setText(username);
			LinphoneCallParams remoteParams = mCall.getRemoteParams();
			if (remoteParams != null && remoteParams.getVideoEnabled()) {
				mIncomeInfo.setText(R.string.wait_info2);
			} else {
				mIncomeInfo.setText(R.string.wait_info1);
			}

			Bitmap bmp = null;
			try {
				bmp = BitmapFactory.decodeResource(getResources(), R.drawable.wait_icon);
				bmp = MethodBitmap.getCircleBitmap(bmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mIncomeIcon.setImageBitmap(bmp);
		} else {
			String icon = contact.getIcon();
			String account = contact.getAccount();
			String name = contact.getName();

			mIncomeName.setText(name != null ? name : account);
			LinphoneCallParams remoteParams = mCall.getRemoteParams();
			if (remoteParams != null && remoteParams.getVideoEnabled()) {
				mIncomeInfo.setText(R.string.wait_info2);
			} else {
				mIncomeInfo.setText(R.string.wait_info1);
			}
			Bitmap bmp = null;
			try {
				if (icon != null && icon.length() > 0) {
					bmp = BitmapFactory.decodeFile(icon);
				} else {
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.wait_icon);
				}
				bmp = MethodBitmap.getCircleBitmap(bmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mIncomeIcon.setImageBitmap(bmp);
		}
	}

	private void onListener() {

		mListener = new LinphoneCoreListenerBase() {
			@Override
			public void callState(LinphoneCore lc, LinphoneCall call, LinphoneCall.State state,
					String message) {

				Log.i("==========State=====incoming==:" + state.toString());

				if (LinphoneManager.getLc().getCallsNb() == 0) {
					finish();
					return;
				}
				if (call == mCall && (State.CallEnd == state || state == State.Error)) {
					finish();
					return;
				}

				if (state == State.StreamsRunning) {
					if (New_Application.isVideoPhone) {
						LinphoneManager.getLc().enableSpeaker(true);
					} else {
						LinphoneManager.getLc().enableSpeaker(
								LinphoneManager.getLc().isSpeakerEnabled());
					}
				} else if (State.Connected == state) {
					New_Application.LongStartTime = System.currentTimeMillis();
				}
				registerCallDurationTimer();
			}
		};
	}

	private void registerCallDurationTimer() {

		if (mCall != null) {
			int callDuration = mCall.getDuration();
			if (callDuration == 0 && mCall.getState() != State.StreamsRunning) {
				return;
			}
			mCallTimer.setBase(SystemClock.elapsedRealtime() - 1000 * callDuration);
			mCallTimer.start();
		}
	}

	private void decline() {
		LinphoneManager.getLc().terminateCall(mCall);
	}

	private void answer() {
		LinphoneCallParams params = LinphoneManager.getLc().createCallParams(mCall);

		boolean isLowBandwidthConnection = !LinphoneUtils.isHighBandwidthConnection(this);
		if (isLowBandwidthConnection) {
			params.enableLowBandwidth(true);
			Log.d("Low bandwidth enabled in call params");
		}

		if (!LinphoneManager.getInstance().acceptCallWithParams(mCall, params)) {
			Toast.makeText(this, R.string.couldnt_accept_call, Toast.LENGTH_LONG).show();
		} else {
			if (!New_Activity_Main.isInstanciated()) {
				return;
			}
			final LinphoneCallParams remoteParams = mCall.getRemoteParams();
			if (remoteParams != null && remoteParams.getVideoEnabled()) {
				New_Application.isVideoPhone = true;
				onStartVideoFragment();
			} else {
				New_Application.isVideoPhone = false;
				New_Activity_Main.instance().startAudioActivity(mCall);

				mIncomeInfo.setVisibility(View.GONE);
				mCallTimer.setVisibility(View.VISIBLE);
			}
		}
	}

	private void onStartVideoFragment() {

		RelativeLayout userLayout = (RelativeLayout) findViewById(R.id.userLayout);
		RelativeLayout containerLayout = (RelativeLayout) findViewById(R.id.containerLayout);
		containerLayout.setVisibility(View.VISIBLE);
		userLayout.setVisibility(View.GONE);

		addVideoFragment(true);
	}

	private void addVideoFragment(Boolean flag) {

		if (flag) {
			New_Fragment_Video fragment = new New_Fragment_Video();
			fragment.setSwitchWindow(switchWindowCall1);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.containerLayout, fragment, fragment.getClass().getSimpleName());
			ft.commit();
		} else {
			New_Fragment_Video2 fragment = new New_Fragment_Video2();
			fragment.setSwitchWindow(switchWindowCall2);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.containerLayout, fragment, fragment.getClass().getSimpleName());
			ft.commit();
		}
	}

	private SwitchWindow1 switchWindowCall1 = new SwitchWindow1() {

		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
			if (result != null && result.endsWith("SwitchWindow")) {
				addVideoFragment(false);
			}
		}
	};

	private SwitchWindow2 switchWindowCall2 = new SwitchWindow2() {

		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
			if (result != null && result.endsWith("SwitchWindow")) {
				addVideoFragment(true);
			}
		}
	};

}
