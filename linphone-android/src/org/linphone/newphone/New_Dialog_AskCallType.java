package org.linphone.newphone;

import org.linphone.LinphoneManager;
import org.linphone.R;
import org.linphone.ui.AddressText;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class New_Dialog_AskCallType extends Dialog implements OnClickListener {

	private String mContent = null;
	private String mBtnName1 = null;
	private String mBtnName2 = null;
	private NewOutCallBack callback = null;

	private Button mButton1;
	private Button mButton2;
	private AddressText mAddressText;

	/**
	 * 文本对话框
	 * 
	 * @param context
	 *            上下文
	 * @param content
	 *            显示内容
	 * @param btn1
	 *            按钮1名字
	 * @param btn2
	 *            按钮2名字
	 */
	public New_Dialog_AskCallType(Context context, String phone, NewOutCallBack callback) {
		super(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
		mBtnName1 = "视频电话";
		mBtnName2 = "语音电话";
		mContent = "请选择电话类型";
		this.callback = callback;

		mAddressText = new AddressText(context, null);
		mAddressText.setText(phone);
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
		mButton1.setOnClickListener(this);
		mButton2.setOnClickListener(this);
		mButton1.setText(mBtnName1);
		mButton2.setText(mBtnName2);

	}

	/**
	 * 按钮事件
	 */
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		if (v == mButton1) {
			New_Application.isVideoPhone = true;
			LinphoneManager.getInstance().newOutgoingCall(mAddressText);
			callback.setResult("mButton1");
			dismiss();			
		} else if (v == mButton2) {
			New_Application.isVideoPhone = false;
			LinphoneManager.getInstance().newOutgoingCall(mAddressText);
			callback.setResult("mButton1");
			dismiss();			
		}
	}

	public interface NewOutCallBack {
		public void setResult(String result);
	}

}
