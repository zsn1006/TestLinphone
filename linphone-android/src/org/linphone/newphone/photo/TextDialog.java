package org.linphone.newphone.photo;


import org.linphone.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class TextDialog extends Dialog implements OnClickListener
{
	private OnDialogDismissListener callback = null;
	private String content = null;
	private String posBtnContent = "确定";
	private String negBtnContent = null;

	private Button btnPos;
	private Button btnNeg;
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
	 * @param onDialogDismissListener
	 *            返回接口
	 */
	public TextDialog(Context context, String content, String posBtnContent,
			String negBtnContent, OnDialogDismissListener onDialogDismissListener )
	{
		super(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
		if (!TextUtils.isEmpty(posBtnContent)) {
			this.posBtnContent = posBtnContent;
		}
		this.negBtnContent = negBtnContent;
		this.content = content == null ? "" : content;
		this.callback = onDialogDismissListener;
	}


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_text);
		onInitButton();
	}
	
	/**
	 * 初始化控件
	 */
	private void onInitButton() {
		TextView tvContent = (TextView) findViewById(R.id.content);
		tvContent.setText(content);

		btnPos = (Button) findViewById(R.id.btn_positive);
		btnNeg = (Button) findViewById(R.id.btn_negative);
		btnPos.setOnClickListener(this);
		btnNeg.setOnClickListener(this);

		btnPos.setText(posBtnContent);
		if (TextUtils.isEmpty(negBtnContent)) {
			btnNeg.setVisibility(View.GONE);
		} else {
			btnNeg.setText(negBtnContent);
		}
	}

	/**
	 * 按钮事件
	 */
	@Override
	public void onClick(View v)
	{
		/** When OK Button is clicked, dismiss the dialog */
		if (callback != null)
		{
			int vId = v.getId();
			if (vId == R.id.btn_positive)
			{
				callback.onDialogDismiss(BUTTON_POSITIVE);
			}
			else if (vId == R.id.btn_negative) {
				callback.onDialogDismiss(BUTTON_NEGATIVE);
			}
		}
		dismiss();
	}
	
	/**
	 * 回调方法
	 */
	public interface OnDialogDismissListener{
		
		public void onDialogDismiss(int which);
	}

}
