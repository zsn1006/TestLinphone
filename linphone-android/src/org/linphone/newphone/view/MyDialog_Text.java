package org.linphone.newphone.view;

import org.linphone.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MyDialog_Text extends Dialog implements OnClickListener {

	private DialogTextCallBack callback = null;
	private String mContent = null;
	private String mBtnName1 = null;
	private String mBtnName2 = null;

	private Button mButton1;
	private Button mButton2;
	private View mBtnLine;
	
	/**
	 * 文本对话框
	 * @param context 上下文
	 * @param content 显示内容
	 * @param btn1 按钮1名字
	 * @param btn2 按钮2名字
	 * @param callback 返回接口
	 */
	public MyDialog_Text(Context context, String content, String btn1, String btn2, DialogTextCallBack callback) {
		super(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
		mBtnName1 = btn1;
		mBtnName2 = btn2;
		mContent = content;
		this.callback = callback;
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydialog_text);
		onInitButton();
	}

	/**
	 * 初始化控件
	 */
	private void onInitButton() {

		TextView mTvContext = (TextView) findViewById(R.id.mTvContext);
		mTvContext.setText(Html.fromHtml(mContent));
		
		mButton1 = (Button) findViewById(R.id.mButton1);
		mButton2 = (Button) findViewById(R.id.mButton2);
		mBtnLine = (View) findViewById(R.id.mBtnLine);
		mButton1.setOnClickListener(this);
		mButton2.setOnClickListener(this);
		mButton1.setText(mBtnName1);
		mButton2.setText(mBtnName2);

		if (mBtnName1 == null || mBtnName1.equals("null") || mBtnName1.length() <= 0) {
			mButton1.setVisibility(View.GONE);
			mBtnLine.setVisibility(View.GONE);
		} else if (mBtnName2 == null || mBtnName2.equals("null") || mBtnName2.length() <= 0) {
			mButton2.setVisibility(View.GONE);
			mBtnLine.setVisibility(View.GONE);
		}
	}

	/**
	 * 按钮事件
	 */
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		if (v == mButton1) {
			callback.setResult("mButton1");
			dismiss();			
		} else if (v == mButton2) {
			callback.setResult("mButton2");
			dismiss();			
		}
	}

	/***
	 * 回调方法
	 */
	public interface DialogTextCallBack {
		public void setResult(String result);
	}

}
