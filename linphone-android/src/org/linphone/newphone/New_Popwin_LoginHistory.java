package org.linphone.newphone;

import java.util.ArrayList;

import org.linphone.R;
import org.linphone.newphone.adapter.LoginHistoryAdapter;
import org.linphone.newphone.bean.UserInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class New_Popwin_LoginHistory {

	private Context mContext;
	private PopupWindow mPopWin = null;
	private LoginHistoryAdapter mAdapter = null;
	private ListView mPopList = null;
	private View mView;
	private CallbackPopWin mCallback = null;
	public final static int mListCount = 5;

	public New_Popwin_LoginHistory(Context context, ArrayList<UserInfo> list, View view,
			CallbackPopWin callback) {

		if (view != null) {
			this.mCallback = callback;
			this.mContext = context;
			iniPopupWindow(context, list, view);
		}
	}

	private void iniPopupWindow(Context context, ArrayList<UserInfo> list, View view) {

		// 列表窗体处理
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.new_login_popwin_list, null);
		mPopWin = new PopupWindow(layout);
		mView = layout;

		mPopWin.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				mCallback.setResult("dismiss");
			}
		});

		/*** 列表 */
		mPopList = (ListView) layout.findViewById(R.id.mListView);
		mAdapter = new LoginHistoryAdapter(context, list, mHandler);
		mPopList.setAdapter(mAdapter);

		// /*** 用于back键退出，但是会和外部退出冲突 */
		// layout.setFocusableInTouchMode(true);
		// layout.setOnKeyListener(new OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// // TODO Auto-generated method stub
		// if (keyCode == KeyEvent.KEYCODE_BACK) {
		// if (mPopWin != null) {
		// mPopWin.dismiss();
		// mPopWin = null;
		// }
		// return true;
		// }
		// return false;
		// }
		// });

		/*** 用于点外部退出，但是会和back键冲突 */
		mPopWin.setOutsideTouchable(true);
		mPopWin.setBackgroundDrawable(getDrawable());

		/*** 点击事件，获取焦点 */
		mPopWin.setFocusable(true);
		mPopList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mCallback.setResult(String.valueOf(arg2));
				mPopWin.dismiss();
			}
		});

		/*** 控制pop的宽度和高度自适应 */
		mPopList.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopWin.setWidth(LayoutParams.MATCH_PARENT);
		if (list.size() <= mListCount) {
			mPopWin.setHeight(LayoutParams.WRAP_CONTENT);
		} else {
			mPopWin.setHeight((mPopList.getMeasuredHeight()) * mListCount);
		}

		/*** 显示在view下 */
		mPopWin.showAsDropDown(view);

		// /***显示在view上，左，右*/
		// int popupWidth = mPopWin.getWidth();
		// int popupHeight = mPopWin.getHeight();
		// int[] location = new int[2];
		// view.getLocationOnScreen(location);
		// mPopWin.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] +
		// view.getWidth() / 2)
		// - popupWidth / 2, location[1] - popupHeight);
	}

	private Drawable getDrawable() {
		ShapeDrawable bgdrawable = new ShapeDrawable(new OvalShape());
		bgdrawable.getPaint().setColor(
				mContext.getResources().getColor(android.R.color.transparent));
		return bgdrawable;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				if (mPopWin != null) {
					mPopWin.dismiss();
				}
				break;
			case mListCount:
				if (mPopWin != null) {
					mPopWin.setContentView(mView);
					mPopList.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
					mPopWin.setWidth(LayoutParams.MATCH_PARENT);
					mPopWin.setHeight(LayoutParams.WRAP_CONTENT);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	public interface CallbackPopWin {
		public void setResult(String result);
	}
}
