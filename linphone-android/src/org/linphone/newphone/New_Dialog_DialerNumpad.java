package org.linphone.newphone;

import org.linphone.LinphoneManager;
import org.linphone.R;
import org.linphone.core.LinphoneCoreException;
import org.linphone.newphone.New_Dialog_AskCallType.NewOutCallBack;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class New_Dialog_DialerNumpad extends LinearLayout {

	private View myView;
	private LinearLayout mLayoutNewAdd, mLayoutAdd, mLayoutSave;
	private ImageButton mIbDialer, mIbMenu;
	private Button mBtnDialer;
	private TextView mTvNum1, mTvNum2, mTvNum3, mTvNum4, mTvNum5, mTvNum6, mTvNum7, mTvNum8,
			mTvNum9, mTvNum_X, mTvNum0, mTvNum_C;

	private DialerCallBack mCallBack;
	private String mStrValue = "";
	private int ScreenHeight;
	private int KeyboardTop;
	private int offsetStep = 50;
	private Boolean mIsShowKey = true;

	public New_Dialog_DialerNumpad(Context context) {
		this(context, null);
	}

	public New_Dialog_DialerNumpad(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public New_Dialog_DialerNumpad(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		instance = this;
		DisplayMetrics dm = getResources().getDisplayMetrics();
		ScreenHeight = dm.heightPixels;
		onInitControl(context);
	}

	private void onInitControl(Context context) {

		mStrValue = "";
		myView = LayoutInflater.from(context).inflate(R.layout.new_dialog_dialer_numpad, this, true);
		myView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				KeyboardTop = myView.getTop();
			}
		}, 100);

		mLayoutNewAdd = (LinearLayout) myView.findViewById(R.id.mLayoutNewAdd);
		mLayoutAdd = (LinearLayout) myView.findViewById(R.id.mLayoutAdd);
		mLayoutSave = (LinearLayout) myView.findViewById(R.id.mLayoutSave);

		mIbDialer = (ImageButton) myView.findViewById(R.id.mIbDialer);
		mIbMenu = (ImageButton) myView.findViewById(R.id.mIbMenu);
		mBtnDialer = (Button) myView.findViewById(R.id.mBtnDialer);

		mTvNum1 = (TextView) myView.findViewById(R.id.mTvNum1);
		mTvNum2 = (TextView) myView.findViewById(R.id.mTvNum2);
		mTvNum3 = (TextView) myView.findViewById(R.id.mTvNum3);
		mTvNum4 = (TextView) myView.findViewById(R.id.mTvNum4);
		mTvNum5 = (TextView) myView.findViewById(R.id.mTvNum5);
		mTvNum6 = (TextView) myView.findViewById(R.id.mTvNum6);
		mTvNum7 = (TextView) myView.findViewById(R.id.mTvNum7);
		mTvNum8 = (TextView) myView.findViewById(R.id.mTvNum8);
		mTvNum9 = (TextView) myView.findViewById(R.id.mTvNum9);
		mTvNum_X = (TextView) myView.findViewById(R.id.mTvNum_X);
		mTvNum0 = (TextView) myView.findViewById(R.id.mTvNum0);
		mTvNum_C = (TextView) myView.findViewById(R.id.mTvNum_C);

		mLayoutAdd.setOnClickListener(mOnClick);
		mLayoutSave.setOnClickListener(mOnClick);

		mIbDialer.setOnClickListener(mOnClick);
		mIbMenu.setOnClickListener(mOnClick);
		mBtnDialer.setOnClickListener(mOnClick);

		mTvNum1.setOnClickListener(mOnClick);
		mTvNum2.setOnClickListener(mOnClick);
		mTvNum3.setOnClickListener(mOnClick);
		mTvNum4.setOnClickListener(mOnClick);
		mTvNum5.setOnClickListener(mOnClick);
		mTvNum6.setOnClickListener(mOnClick);
		mTvNum7.setOnClickListener(mOnClick);
		mTvNum8.setOnClickListener(mOnClick);
		mTvNum9.setOnClickListener(mOnClick);
		mTvNum_X.setOnClickListener(mOnClick);
		mTvNum0.setOnClickListener(mOnClick);
		mTvNum_C.setOnClickListener(mOnClick);
	}

	private OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int id = arg0.getId();
			switch (id) {
			case R.id.mIbDialer:
				onKeyboard(false);
				break;
			case R.id.mIbMenu:
				onSetPhone(R.id.mIbMenu);
				break;
			case R.id.mBtnDialer:
				onCallOut();
				break;
			case R.id.mTvNum0:
				mStrValue += "0";
				onSetPhone(0);
				break;
			case R.id.mTvNum1:
				mStrValue += "1";
				onSetPhone(0);
				break;
			case R.id.mTvNum2:
				mStrValue += "2";
				onSetPhone(0);
				break;
			case R.id.mTvNum3:
				mStrValue += "3";
				onSetPhone(0);
				break;
			case R.id.mTvNum4:
				mStrValue += "4";
				onSetPhone(0);
				break;
			case R.id.mTvNum5:
				mStrValue += "5";
				onSetPhone(0);
				break;
			case R.id.mTvNum6:
				mStrValue += "6";
				onSetPhone(0);
				break;
			case R.id.mTvNum7:
				mStrValue += "7";
				onSetPhone(0);
				break;
			case R.id.mTvNum8:
				mStrValue += "8";
				onSetPhone(0);
				break;
			case R.id.mTvNum9:
				mStrValue += "9";
				onSetPhone(0);
				break;
			case R.id.mTvNum_X:
				mStrValue += "*";
				onSetPhone(0);
				break;
			case R.id.mTvNum_C:
				mStrValue += "#";
				onSetPhone(0);
				break;

			}
		}
	};

	private void onSetPhone(int id) {

		if (id == R.id.mIbMenu) {
			if (mStrValue.length() <= 0) {
				mCallBack.setResult("-2");
				return;
			}
			if (mStrValue.length() == 1) {
				mStrValue = "";
			} else {
				mStrValue = mStrValue.substring(0, mStrValue.length() - 1);
			}
		}

		if (mStrValue.length() > 0) {
			mIbMenu.setImageResource(R.drawable.icon_delete);
		} else {
			mIbMenu.setImageResource(R.drawable.icon_menu);
		}
		mCallBack.setResult(mStrValue);
	}

	public void onKeyboard(Boolean isShow) {

		if (isShow) {
			mStrValue = "";
			onSetShowAdd(false);
		} else {
			if (mLayoutNewAdd.getVisibility() == View.VISIBLE) {
				mCallBack.setResult("");
			}
		}

		mIsShowKey = isShow;
		mHandler.postDelayed(mRunnable, 20);
	}

	Handler mHandler = new Handler();
	Runnable mRunnable = new Runnable() {

		public void run() {
			if (mIsShowKey) {
				if (myView.getTop() > KeyboardTop + offsetStep) {
					int top = myView.getTop() - offsetStep;
					myView.setTop(top);

					mHandler.postDelayed(mRunnable, 20);
				} else {
					myView.setTop(KeyboardTop);
				}
			} else {
				if (myView.getTop() < ScreenHeight - offsetStep) {
					int top = myView.getTop() + offsetStep;
					myView.setTop(top);

					mHandler.postDelayed(mRunnable, 20);
				} else {
					myView.setTop(ScreenHeight);
				}
			}
		}
	};

	public void onSetShowAdd(Boolean isShow) {

		if (isShow) {
			mLayoutNewAdd.setVisibility(View.VISIBLE);
		} else {
			mLayoutNewAdd.setVisibility(View.GONE);
		}
	}

	private void onCallOut() {

		try {
			if (!LinphoneManager.getInstance().acceptCallIfIncomingPending()) {
				if (mStrValue.length() > 0) {

					New_Dialog_AskCallType dialog = new New_Dialog_AskCallType(
							myView.getContext(), mStrValue, newOutCallback);
					dialog.show();

				} else {
					Toast.makeText(myView.getContext(), "请拨号", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (LinphoneCoreException e) {
			LinphoneManager.getInstance().terminateCall();
			onWrongDestinationAddress();
		}

	}

	public void onWrongDestinationAddress() {
		Toast toast = Toast.makeText(getContext(), String.format(
				getResources().getString(R.string.warning_wrong_destination_address), mStrValue),
				Toast.LENGTH_LONG);
		toast.show();
	}

	private NewOutCallBack newOutCallback = new NewOutCallBack() {

		@Override
		public void setResult(String result) {
			// TODO Auto-generated method stub
			if (result != null && result.length() > 0) {
				mCallBack.setResult("-1");
				mStrValue = "";
				mIbMenu.setImageResource(R.drawable.icon_menu);
			}
		}
	};

	/**
	 * 
	 * 拨号键盘返回接口 no.1----返回电话号码 No.2----返回长度为0的字符串 No.3----返回-1，隐藏键盘
	 * No.4----返回-2，清空电话记录
	 */
	public interface DialerCallBack {
		public void setResult(String result);
	}

	public void setCallBack(DialerCallBack callback) {
		mCallBack = callback;
	}

	private static New_Dialog_DialerNumpad instance;

	public static New_Dialog_DialerNumpad instance() {
		return instance;
	}

	public void onDestroy() {
		instance = null;
	}
}
