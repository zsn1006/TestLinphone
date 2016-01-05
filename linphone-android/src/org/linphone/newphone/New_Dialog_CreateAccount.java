package org.linphone.newphone;

import java.util.ArrayList;

import org.linphone.R;
import org.linphone.newphone.bean.Contact;
import org.linphone.newphone.bean.ContactSort;
import org.linphone.newphone.db.ContactsDao;
import org.linphone.newphone.tools.HanziToPinyin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class New_Dialog_CreateAccount extends Dialog implements OnClickListener {

	private createCallBack callback = null;
	private Context mContext;
	private String mContent = null;
	private String mBtnName1 = null;
	private String mBtnName2 = null;

	private Button mButton1;
	private Button mButton2;
	private View mBtnLine;

	private ImageView mAddIcon;
	private EditText mEtAccount;
	private EditText mEtSipAddr;
	private EditText mEtNickname;
	private String mIconPath;

	public New_Dialog_CreateAccount(Context context, String content, String btn1, String btn2,
			createCallBack callback) {
		super(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
		mBtnName1 = btn1;
		mBtnName2 = btn2;
		mContent = content;
		mContext = context;
		this.callback = callback;
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_dialog_create_account);
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

		mAddIcon = (ImageView) findViewById(R.id.mAddIcon);
		mEtAccount = (EditText) findViewById(R.id.mEtAccount);
		mEtSipAddr = (EditText) findViewById(R.id.mEtSipAddr);
		mEtNickname = (EditText) findViewById(R.id.mEtNickname);
		mIconPath = "";
	}

	/**
	 * 按钮事件
	 */
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		if (v == mButton1) {
			if (-1 == addContact()) {
				callback.setResult("mButton1");
				dismiss();
			}
		} else if (v == mButton2) {
			callback.setResult("mButton2");
			dismiss();
		}
	}

	/***
	 * 回调方法
	 */
	public interface createCallBack {
		public void setResult(String result);
	}

	private int addContact() {

		String name = mEtNickname.getText().toString();
		String account = mEtAccount.getText().toString();
		
		int exist = -1;
		ArrayList<ContactSort> contacts = New_Application.getInstance().getContactSort();
		for (int i = 0; i < contacts.size(); i++) {
			ArrayList<Contact> con = contacts.get(i).getContacts();
			for (int j = 0; j < con.size(); j++) {
				String OldName = con.get(j).getName();
				String OldAccount = con.get(j).getAccount();
				if (OldName.equals(name)) {
					Toast.makeText(mContext, "呢称已被使用", Toast.LENGTH_SHORT).show();
					return 1;
				} else if (OldAccount.equals(account)) {
					Toast.makeText(mContext, "账号已被使用", Toast.LENGTH_SHORT).show();
					return 2;
				}
			}
		}
		if (-1 == exist) {
			String sipAddr = mEtSipAddr.getText().toString();
			String initial = HanziToPinyin.getInstance().toPinYin(name);
			Contact contact = new Contact(0, name, account, sipAddr, mIconPath, "", "", "", initial);
			ContactsDao.getInstance().insertContacts(contact);
			New_Application.getInstance().addNewContact(contact);
		}
		return exist;
	}
}
