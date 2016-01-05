package org.linphone.newphone;

import static android.content.Intent.ACTION_MAIN;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.linphone.ChatStorage;
import org.linphone.InCallActivity;
import org.linphone.LinphoneManager;
import org.linphone.LinphonePreferences;
import org.linphone.LinphoneService;
import org.linphone.LinphoneUtils;
import org.linphone.R;
import org.linphone.LinphoneManager.AddressType;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallLog;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreListenerBase;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.core.Reason;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore.EcCalibratorStatus;
import org.linphone.core.LinphoneCore.RegistrationState;
import org.linphone.mediastream.Log;
import org.linphone.newphone.New_Dialog_DialerNumpad.DialerCallBack;
import org.linphone.newphone.New_Dialog_AskCallType.NewOutCallBack;
import org.linphone.newphone.adapter.MainLatelyAdapter;
import org.linphone.newphone.bean.CallRecords;
import org.linphone.newphone.db.CallRecordsDao;
import org.linphone.newphone.slidmenu.ResideMenu;
import org.linphone.newphone.slidmenu.ResideMenuItem;
import org.linphone.setup.SetupActivity;
import org.linphone.ui.AddressText;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class New_Activity_Main extends BaseActivity implements ContactPicked {

	private ImageButton left_button;
	private ImageButton right_button;
	private TextView title_text;

	private ResideMenu mResideMenu;
	private ResideMenuItem mItemUser;
	private ResideMenuItem mItemUpdate;
	private ResideMenuItem mItemFeedback;
	private ResideMenuItem mItemSettings;

	private MainLatelyAdapter mAdapter = null;
	private ListView mListLately = null;
	private ArrayList<CallRecords> mData;

	private LinearLayout mShortLayout;
	private TextView mTvPhone;
	private New_Dialog_DialerNumpad mDialogDialer;
	private Boolean mFirstEnter = true;

	private static final int CALL_ACTIVITY = 19;
	private static New_Activity_Main instance;
	private LinphoneCoreListenerBase mListener;
	private OrientationEventListener mOrientationHelper;
	private boolean newProxyConfig;
	private boolean mSendEcCalibrationResult = false;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_main);

		if (!LinphoneManager.isInstanciated()) {
			Log.e("No service running: avoid crash by starting the launcher", this.getClass()
					.getName());
			finish();
			return;
		}

		/** UI */
		setUpMenu();
		onInitTopBar();
		onInitListView();
		onInitDialer();
		/** 注册电话功能 */
		onRegisterLinphone();
	}

	private void onInitTopBar() {

		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText(R.string.new_app_name);

		left_button = (ImageButton) findViewById(R.id.left_button);
		left_button.setVisibility(View.VISIBLE);
		left_button.setBackgroundResource(R.drawable.new_menu_left0);
		left_button.setOnClickListener(mOnTopBtnListener);

		right_button = (ImageButton) findViewById(R.id.right_button);
		right_button.setVisibility(View.VISIBLE);
		right_button.setBackgroundResource(R.drawable.new_menu_right0);
		right_button.setOnClickListener(mOnTopBtnListener);
	}

	private void onInitListView() {

		mData = New_Application.getInstance().getCallRecords();

		mListLately = (ListView) findViewById(R.id.mListLately);
		mAdapter = new MainLatelyAdapter(this, mData);
		mListLately.setAdapter(mAdapter);

		mListLately.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				if (mDialogDialer != null) {
					mDialogDialer.onKeyboard(false);
				}
			}
		});

		//长按事件
		mListLately.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				CallRecords call = mData.get(position);
				if (call != null)
				{
					int type = call.getCallType();
					String phone = call.getAccount();
					if (!(type == 0))
					{
						New_Dialog_AskCallType dialog = new New_Dialog_AskCallType(
								New_Activity_Main.this, phone, newOutCallback);
						dialog.show();
					}
				}
				return false;
			}
		});
		
		mListLately.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				CallRecords call = mData.get(arg2);
				if (call != null) {
					int type = call.getCallType();
					String phone = call.getAccount();
					if (type == 0) {
						New_Dialog_AskCallType dialog = new New_Dialog_AskCallType(
								New_Activity_Main.this, phone, newOutCallback);
						dialog.show();
					} else {
						if (type == 1) {
							New_Application.isVideoPhone = true;
						} else if (type == 2) {
							New_Application.isVideoPhone = false;
						}
						AddressText mAddressText = new AddressText(New_Activity_Main.this, null);
						mAddressText.setText(phone);
						LinphoneManager.getInstance().newOutgoingCall(mAddressText);
					}
				}
			}
		});
	}

	private NewOutCallBack newOutCallback = new NewOutCallBack() {

		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
		}
	};

	private void onInitDialer() {

		mShortLayout = (LinearLayout) findViewById(R.id.mShortLayout);
		mDialogDialer = (New_Dialog_DialerNumpad) findViewById(R.id.mDialogDialer);
		mTvPhone = (TextView) findViewById(R.id.mTvPhone);

		mShortLayout.setOnClickListener(mOnTopBtnListener);
		mDialogDialer.setCallBack(dialerBack);
		// if (mDialogDialer != null) {
		// mDialogDialer.onKeyboard(false);
		// mDialogDialer.setVisibility(View.INVISIBLE);
		// }
	}

	private void setUpMenu() {

		// attach to current activity;
		mResideMenu = new ResideMenu(this);
		mResideMenu.setBackground(R.drawable.new_menu_bg);
		mResideMenu.attachToActivity(this);
		mResideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		mResideMenu.setScaleValue(0.6f);

		// create menu items;
		mItemUser = new ResideMenuItem(this, R.drawable.new_menu_icon0, "个人中心");
		mItemUpdate = new ResideMenuItem(this, R.drawable.new_menu_icon1, "更　　新");
		mItemFeedback = new ResideMenuItem(this, R.drawable.new_menu_icon2, "反　　馈");
		mItemSettings = new ResideMenuItem(this, R.drawable.new_menu_icon3, "设　　置");

		mItemUser.setOnClickListener(mOnClick);
		mItemUpdate.setOnClickListener(mOnClick);
		mItemFeedback.setOnClickListener(mOnClick);
		mItemSettings.setOnClickListener(mOnClick);

		mResideMenu.addMenuItem(mItemUser, ResideMenu.DIRECTION_LEFT);
		mResideMenu.addMenuItem(mItemUpdate, ResideMenu.DIRECTION_LEFT);
		mResideMenu.addMenuItem(mItemFeedback, ResideMenu.DIRECTION_LEFT);
		mResideMenu.addMenuItem(mItemSettings, ResideMenu.DIRECTION_LEFT);
	}

	private OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0 == mItemUser) {
				//Toast.makeText(New_Activity_Main.this, "个人中心", Toast.LENGTH_SHORT).show();
				Intent  intent_myCenter = new Intent(New_Activity_Main.this, New_Mycenter_Activity.class);
				startActivity(intent_myCenter);
				
			} else if (arg0 == mItemUpdate) {
				Toast.makeText(New_Activity_Main.this, "更新", Toast.LENGTH_SHORT).show();
				
			} else if (arg0 == mItemFeedback) {
				//Toast.makeText(New_Activity_Main.this, "反馈", Toast.LENGTH_SHORT).show();
				Intent  intent_feedBack = new Intent(New_Activity_Main.this, New_Activity_Feedback.class);
				startActivity(intent_feedBack);
				
			} else if (arg0 == mItemSettings) {
				//Toast.makeText(New_Activity_Main.this, "设置", Toast.LENGTH_SHORT).show();
				Intent intent_setted = new Intent(New_Activity_Main.this, New_Activity_Setted.class);
				startActivity(intent_setted);
			}
			mResideMenu.closeMenu();
		}
	};

	private OnClickListener mOnTopBtnListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.left_button:
				mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
				break;
			case R.id.right_button:
				startActivity(new Intent(New_Activity_Main.this, New_Activity_Linkman.class));
				break;
			case R.id.mShortLayout:
				if (mDialogDialer != null) {
					mDialogDialer.onKeyboard(true);
					if (mFirstEnter) {
						mFirstEnter = false;
						mDialogDialer.setVisibility(View.VISIBLE);
					}
				}
				break;
			}
		}
	};

	private DialerCallBack dialerBack = new DialerCallBack() {

		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
			if (result != null) {
				mTvPhone.setText(result);
				if (result.equals("-2")) {
					onClearRecord();
				} else if (result.equals("-1")) {
					mTvPhone.setVisibility(View.GONE);
				} else if (result.length() > 0) {
					mTvPhone.setVisibility(View.VISIBLE);
				} else {
					mTvPhone.setVisibility(View.GONE);
				}
			}
		}
	};

	private void onClearRecord() {

		if (mData != null && mData.size() > 0) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setMessage("是否清除聊天记录？")
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							getDataAndRefreshList(true);
							arg0.dismiss();
						}
					}).setNeutralButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
						}
					}).show();
		} else {
			Toast.makeText(this, "没有通讯记录", Toast.LENGTH_SHORT).show();
		}
	}

	private void getDataAndRefreshList(Boolean refresh) {

		LinphoneManager.getLc().clearCallLogs();
		CallRecordsDao.getInstance().deleteAll();
		if (refresh) {
			mData.clear();
			mAdapter.onRefresh(mData);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mResideMenu.dispatchTouchEvent(ev);
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
		}

		@Override
		public void closeMenu() {
		}
	};

	public ResideMenu getmResideMenu() {
		return mResideMenu;
	}

	private void onRegisterLinphone() {

		instance = this;
		mListener = new LinphoneCoreListenerBase() {

			public void ecCalibrationStatus(LinphoneCore lc,
					LinphoneCore.EcCalibratorStatus status, int delay_ms, Object data) {
				LinphoneManager.getInstance().routeAudioToReceiver();
				if (mSendEcCalibrationResult) {
					sendEcCalibrationResult(status, delay_ms);
				} else {
					LinphonePreferences.instance().setAccountEnabled(
							LinphonePreferences.instance().getAccountCount() - 1, true);
					LinphonePreferences.instance().firstLaunchSuccessful();
					isNewProxyConfig();
				}
			}

			@Override
			public void registrationState(LinphoneCore lc, LinphoneProxyConfig proxy,
					LinphoneCore.RegistrationState state, String smessage) {
				if (state.equals(RegistrationState.RegistrationCleared)) {
					if (lc != null) {
						LinphoneAuthInfo authInfo = lc.findAuthInfo(proxy.getIdentity(),
								proxy.getRealm(), proxy.getDomain());
						if (authInfo != null)
							lc.removeAuthInfo(authInfo);
					}
				}

				if (state.equals(RegistrationState.RegistrationFailed) && newProxyConfig) {
					newProxyConfig = false;
					if (proxy.getError() == Reason.BadCredentials) {
						displayCustomToast(getString(R.string.error_bad_credentials),
								Toast.LENGTH_LONG);
					}
					if (proxy.getError() == Reason.Unauthorized) {
						displayCustomToast(getString(R.string.error_unauthorized),
								Toast.LENGTH_LONG);
					}
					if (proxy.getError() == Reason.IOError) {
						displayCustomToast(getString(R.string.error_io_error), Toast.LENGTH_LONG);
					}
				}

				if (state.equals(RegistrationState.RegistrationOk)) {
					LinphonePreferences.instance().setVideoPolicy(true, true);
				}
			}

			@Override
			public void callState(LinphoneCore lc, LinphoneCall call, LinphoneCall.State state,
					String message) {
				if (state == State.IncomingReceived) {
					New_Application.isIncoming = true;
					New_Application.LongStartTime = 0;
					startActivity(new Intent(New_Activity_Main.instance(),
							New_Activity_Incoming.class));
				} else if (state == State.OutgoingInit) {
					New_Application.isIncoming = false;
					New_Application.LongStartTime = 0;
					if (call.getCurrentParamsCopy().getVideoEnabled()) {
						startVideoActivity(call);
					} else {
						startAudioActivity(call);
					}
				} else if (state == State.CallEnd || state == State.Error
						|| state == State.CallReleased) {
					// Convert LinphoneCore message for internalization
					if (message != null && call.getReason() == Reason.Declined) {
						displayCustomToast(getString(R.string.error_call_declined),
								Toast.LENGTH_LONG);
					} else if (message != null && call.getReason() == Reason.NotFound) {
						displayCustomToast(getString(R.string.error_user_not_found),
								Toast.LENGTH_LONG);
					} else if (message != null && call.getReason() == Reason.Media) {
						displayCustomToast(getString(R.string.error_incompatible_media),
								Toast.LENGTH_LONG);
					} else if (message != null && state == State.Error) {
						displayCustomToast(getString(R.string.error_unknown) + " - " + message,
								Toast.LENGTH_LONG);
					}
					resetClassicMenuLayoutAndGoBackToCallIfStillRunning();

					if (state == State.CallReleased) {
						CallRecords record = new CallRecords();
						record.setId(0);
						if (!New_Application.isIncoming) {
							List<LinphoneCallLog> logs = Arrays.asList(lc.getCallLogs());
							String name = logs.get(0).getTo().getUserName();
							record.setAccount(name);
						} else {
							List<LinphoneCallLog> logs = Arrays.asList(lc.getCallLogs());
							String name = logs.get(0).getFrom().getUserName();
							record.setAccount(name);
						}
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = format.format(new Date(New_Application.LongStartTime));
						record.setStartTime(time);

						long endTime = System.currentTimeMillis();
						int useTime = (int) ((endTime - New_Application.LongStartTime) / 1000);
						record.setTimeLength(useTime);

						int type = New_Application.isVideoPhone ? 1 : 2;
						if (New_Application.LongStartTime == 0) {
							type = 0;
						}
						record.setCallType(type);
						record.setIncoming(New_Application.isIncoming ? 1 : 0);

						New_Application.getInstance().addNewRecords(record);
						mData = New_Application.getInstance().getCallRecords();
						mAdapter.notifyDataSetChanged();

						New_Application.isVideoPhone = false;
						New_Application.isIncoming = false;
					}
				}
			}
		};

		if (LinphonePreferences.instance().isFirstLaunch()) {
			try {
				LinphoneManager.getInstance().startEcCalibration(mListener);
			} catch (LinphoneCoreException e) {
				Log.e(e, "Unable to calibrate EC");
			}
		}

		LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
		if (lc != null) {
			lc.addListener(mListener);
		}

		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			rotation = 0;
			break;
		case Surface.ROTATION_90:
			rotation = 90;
			break;
		case Surface.ROTATION_180:
			rotation = 180;
			break;
		case Surface.ROTATION_270:
			rotation = 270;
			break;
		}

		LinphoneManager.getLc().setDeviceRotation(rotation);
		mAlwaysChangingPhoneAngle = rotation;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Bundle extras = intent.getExtras();
		if (extras != null && extras.getBoolean("Notification", false)) {
			if (LinphoneManager.getLc().getCallsNb() > 0) {
				LinphoneCall call = LinphoneManager.getLc().getCalls()[0];
				if (call.getCurrentParamsCopy().getVideoEnabled()) {
					startVideoActivity(call);
				} else {
					startAudioActivity(call);
				}
			}
		} else {
			if (LinphoneManager.getLc().getCalls().length > 0) {
				LinphoneCall calls[] = LinphoneManager.getLc().getCalls();
				if (calls.length > 0) {
					LinphoneCall call = calls[0];

					if (call != null && call.getState() != LinphoneCall.State.IncomingReceived) {
						if (call.getCurrentParamsCopy().getVideoEnabled()) {
							startVideoActivity(call);
						} else {
							startAudioActivity(call);
						}
					}
				}

				// If a call is ringing, start incomingcallactivity
				Collection<LinphoneCall.State> incoming = new ArrayList<LinphoneCall.State>();
				incoming.add(LinphoneCall.State.IncomingReceived);
				if (LinphoneUtils.getCallsInState(LinphoneManager.getLc(), incoming).size() > 0) {
					if (InCallActivity.isInstanciated()) {
						InCallActivity.instance().startIncomingCallActivity();
					} else {
						startActivity(new Intent(this, New_Activity_Incoming.class));
					}
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mOrientationHelper != null) {
			mOrientationHelper.disable();
			mOrientationHelper = null;
		}

		LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
		if (lc != null) {
			lc.removeListener(mListener);
		}

		instance = null;
		super.onDestroy();

		System.gc();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!LinphoneService.isReady()) {
			startService(new Intent(ACTION_MAIN).setClass(this, LinphoneService.class));
		}

		LinphoneManager.getInstance().changeStatusToOnline();

		if (getIntent().getIntExtra("PreviousActivity", 0) != CALL_ACTIVITY) {
			if (LinphoneManager.getLc().getCalls().length > 0) {
				LinphoneCall call = LinphoneManager.getLc().getCalls()[0];
				LinphoneCall.State callState = call.getState();
				if (callState == State.IncomingReceived) {
					startActivity(new Intent(this, New_Activity_Incoming.class));
				} else {

					if (call.getCurrentParamsCopy().getVideoEnabled()) {
						startVideoActivity(call);
					} else {
						startAudioActivity(call);
					}
				}
			}
		}
	}

	@Override
	protected void onPause() {
		getIntent().putExtra("PreviousActivity", 0);
		super.onPause();
	}

	public void exit() {
		finish();
		stopService(new Intent().setClass(this, LinphoneService.class));
	}

	private int mAlwaysChangingPhoneAngle = -1;

	private class LocalOrientationEventListener extends OrientationEventListener {
		public LocalOrientationEventListener(Context context) {
			super(context);
		}

		@Override
		public void onOrientationChanged(final int o) {
			if (o == OrientationEventListener.ORIENTATION_UNKNOWN) {
				return;
			}

			int degrees = 270;
			if (o < 45 || o > 315)
				degrees = 0;
			else if (o < 135)
				degrees = 90;
			else if (o < 225)
				degrees = 180;

			if (mAlwaysChangingPhoneAngle == degrees) {
				return;
			}
			mAlwaysChangingPhoneAngle = degrees;

			Log.d("Phone orientation changed to ", degrees);
			int rotation = (360 - degrees) % 360;
			LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
			if (lc != null) {
				lc.setDeviceRotation(rotation);
				LinphoneCall currentCall = lc.getCurrentCall();
				if (currentCall != null && currentCall.cameraEnabled()
						&& currentCall.getCurrentParamsCopy().getVideoEnabled()) {
					lc.updateCall(currentCall, null);
				}
			}
		}
	}

	public ChatStorage getChatStorage() {
		return ChatStorage.getInstance();
	}

	public void displayCustomToast(final String message, final int duration) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastRoot));

		TextView toastText = (TextView) layout.findViewById(R.id.toastMessage);
		toastText.setText(message);

		final Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(duration);
		toast.setView(layout);
		toast.show();
	}

	public void startVideoActivity(LinphoneCall currentCall) {

		Intent intent = new Intent(this, New_Activity_Outgoing.class);
		startOrientationSensor();
		startActivityForResult(intent, CALL_ACTIVITY);
	}

	public void startAudioActivity(LinphoneCall currentCall) {
		if (New_Application.isVideoPhone) {
			startVideoActivity(currentCall);
		} else {
			Intent intent = new Intent(this, New_Activity_Outgoing.class);
			startOrientationSensor();
			startActivityForResult(intent, CALL_ACTIVITY);
		}
	}

	public void resetClassicMenuLayoutAndGoBackToCallIfStillRunning() {

		if (LinphoneManager.isInstanciated() && LinphoneManager.getLc().getCallsNb() > 0) {
			LinphoneCall call = LinphoneManager.getLc().getCalls()[0];
			if (call.getState() == LinphoneCall.State.IncomingReceived) {
				startActivity(new Intent(this, New_Activity_Incoming.class));
			} else if (call.getCurrentParamsCopy().getVideoEnabled()) {
				startVideoActivity(call);
			} else {
				startAudioActivity(call);
			}
		}
	}

	/**
	 * Register a sensor to track phoneOrientation changes
	 */
	private synchronized void startOrientationSensor() {
		if (mOrientationHelper == null) {
			mOrientationHelper = new LocalOrientationEventListener(this);
		}
		mOrientationHelper.enable();
	}

	private void sendEcCalibrationResult(EcCalibratorStatus status, int delayMs) {
		try {
			XMLRPCClient client = new XMLRPCClient(new URL(getString(R.string.wizard_url)));

			XMLRPCCallback listener = new XMLRPCCallback() {
				Runnable runFinished = new Runnable() {
					public void run() {
						SetupActivity.instance().isEchoCalibrationFinished();
					}
				};

				public void onResponse(long id, Object result) {
					mHandler.post(runFinished);
				}

				public void onError(long id, XMLRPCException error) {
					mHandler.post(runFinished);
				}

				public void onServerError(long id, XMLRPCServerException error) {
					mHandler.post(runFinished);
				}
			};

			Boolean hasBuiltInEchoCanceler = LinphoneManager.getLc().hasBuiltInEchoCanceler();
			Log.i("Add echo canceller calibration result: manufacturer=" + Build.MANUFACTURER
					+ " model=" + Build.MODEL + " status=" + status + " delay=" + delayMs + "ms"
					+ " hasBuiltInEchoCanceler " + hasBuiltInEchoCanceler);
			client.callAsync(listener, "add_ec_calibration_result", Build.MANUFACTURER,
					Build.MODEL, status.toString(), delayMs, hasBuiltInEchoCanceler);
		} catch (Exception ex) {
		}
	}

	public void isNewProxyConfig() {
		newProxyConfig = true;
	}

	@Override
	public void setAddresGoToDialerAndCall(String number, String name, Uri photo) {
		// TODO Auto-generated method stub
		AddressType address = new AddressText(this, null);
		address.setDisplayedName(name);
		address.setText(number);
		LinphoneManager.getInstance().newOutgoingCall(address);
	}

	@Override
	public void goToDialer() {
		// TODO Auto-generated method stub

	}

	static final boolean isInstanciated() {
		return instance != null;
	}

	public static final New_Activity_Main instance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new New_Activity_Main();
			return instance;
		}
	}
}

interface ContactPicked {
	void setAddresGoToDialerAndCall(String number, String name, Uri photo);

	void goToDialer();
}
