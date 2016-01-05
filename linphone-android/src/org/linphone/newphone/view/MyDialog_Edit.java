package org.linphone.newphone.view;


import org.linphone.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MyDialog_Edit extends Dialog implements OnClickListener {

	private DialogEditCallBack callback = null;
	private TextView mTvTitle = null;

	private Button mButton1;
	private Button mButton2;

	private String mStrTitle = null;
	private String mStrError = null;
	
	private EditText mEtContext = null;
	
	
	/**
	 * 文本对话框
	 * @param context 上下文
	 * @param content 显示内容
	 * @param btn1 按钮1名字
	 * @param btn2 按钮2名字
	 * @param callback 返回接口
	 */
	public MyDialog_Edit(Context context, String title, String error, DialogEditCallBack callback) {
		super(context, R.style.MyDialog);
		mStrTitle = title;
		mStrError = error;
		this.callback = callback;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydialog_edit);
		onInitButton();
	}

	/**
	 * 初始化控件
	 */
	private void onInitButton() {

		mEtContext = (EditText)findViewById(R.id.mEtContext);
		mTvTitle = (TextView) findViewById(R.id.mTvContext);
		if (mStrTitle != null && !mStrTitle.isEmpty()) {
			mTvTitle.setText(mStrTitle);
		}
		
		mButton1 = (Button) findViewById(R.id.mButton1);
		mButton2 = (Button) findViewById(R.id.mButton2);
		mButton1.setOnClickListener(this);
		mButton2.setOnClickListener(this);
	}

	/**
	 * 按钮事件
	 */
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		if (v == mButton1) {
			String str = mEtContext.getText().toString();
			if (str == null || str.isEmpty()) {
				mEtContext.setError(Html.fromHtml("<font color=#ff0000>" + mStrError + "</font>"));
			} else {
				callback.setResult(str);
				dismiss();	
			}		
		} else if (v == mButton2) {
			callback.setResult("cancel");
			dismiss();			
		}
	}

	/***
	 * 回调方法
	 */
	public interface DialogEditCallBack {
		public void setResult(String result);
	}

}
