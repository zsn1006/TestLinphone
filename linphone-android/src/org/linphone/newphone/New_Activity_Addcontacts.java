package org.linphone.newphone;

import java.io.File;
import java.util.ArrayList;
import org.linphone.R;
import org.linphone.newphone.bean.ContactSort;
import org.linphone.newphone.bean.Contact;
import org.linphone.newphone.db.ContactsDao;
import org.linphone.newphone.photo.CutPicActivity;
import org.linphone.newphone.photo.PhotoActivity;
import org.linphone.newphone.photo.TextDialog;
import org.linphone.newphone.photo.TextDialog.OnDialogDismissListener;
import org.linphone.newphone.photo.Util;
import org.linphone.newphone.tools.HanziToPinyin;
import org.linphone.newphone.tools.Constants;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class New_Activity_Addcontacts extends Activity
{
	private Context mContext = New_Activity_Addcontacts.this;
	private Button mfinishButton;
	private ImageView mAddIcon;
	private EditText mEtAccount;
	private EditText mEtSipAddr;
	private EditText mEtNickname;
	private Button left_button;
	/*新建用户的账号，用来判断以及存储照片时命名*/
	private String get_mEtAccount;

	/* 上传头像 */
	private String mUserIconPath = "";
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_ALBUM = 2;// 相册
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcontacts);
		onCreateDir(Constants.mImgPath);
		onInitButton();
	}

	private void onCreateDir(String path)
	{
		File file = new File(path);
		if (!file.exists())
		{
			file.mkdirs();
		}

	}

	/**
	 * 初始化控件
	 */
	private void onInitButton()
	{
		mAddIcon = (ImageView) findViewById(R.id.addcontacts_mIvHead);
		mEtAccount = (EditText) findViewById(R.id.addcontacts_mEtAccount);
		mEtSipAddr = (EditText) findViewById(R.id.addcontacts_mEtSipAddr);
		mEtNickname = (EditText) findViewById(R.id.addcontacts_mEtNickname);
		mfinishButton = (Button) findViewById(R.id.finish_button);
		left_button = (Button) findViewById(R.id.left_button);

		left_button.setOnClickListener(mOnClickListener);
		mfinishButton.setOnClickListener(mOnClickListener);
		mAddIcon.setOnClickListener(mOnClickListener);
	}

	/**
	 * 从照片保存的路径获得照片并且显示
	 */
	private void showPhoto()
	{
		mUserIconPath = Constants.mImgPath + get_mEtAccount + ".jpg";
		File f = new File(mUserIconPath);
		if (f.exists())
		{
			Bitmap bm = BitmapFactory.decodeFile(mUserIconPath);
			if (bm == null)
			{
				f.delete();
			} else
			{
				mAddIcon.setImageBitmap(Util.toRoundBitmap(bm));
			}
		} else
		{
			mAddIcon.setImageResource(R.drawable.portrait);
		}
	}

	/**
	 * startActivity返回
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case PHOTO_REQUEST_CAMERA:
			if (resultCode == -1)
			{
				onNextActivity(null);
			}
			break;
		case PHOTO_REQUEST_CUT:
			if (resultCode == 123)
			{
				showPhoto();
			}
			break;
		case PHOTO_REQUEST_ALBUM:
			if (resultCode == 9527)
			{
				Bundle bundle = data.getExtras();
				if (data != null && bundle != null)
				{
					String path = bundle.getString("name");
					onNextActivity(path);
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 调用剪裁界面
	 * 
	 * @param path
	 * 
	 */
	private void onNextActivity(String path)
	{
		Intent intent = new Intent(mContext, CutPicActivity.class);
		if (path != null)
		{
			intent.putExtra("fullPath", path);
		}
		intent.putExtra("bmpPath", Constants.mImgPath);
		intent.putExtra("bmpName", get_mEtAccount + ".jpg");
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private OnClickListener mOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.finish_button:
                String mEtAccount_content = mEtAccount.getText().toString().trim();
                String mEtSipAddr_content = mEtSipAddr.getText().toString().trim();
                String mEtNickname_content = mEtNickname.getText().toString().trim();
				// 新建的联系人不能为空
				if ((mEtAccount_content.equals("") || mEtNickname_content.equals("") || mEtSipAddr_content.equals(""))
						|| (mEtAccount_content.length() <= 0|| mEtNickname_content.length() <= 0 || mEtSipAddr_content.length() <= 0))
				{
					Toast.makeText(New_Activity_Addcontacts.this, "输入不能为空",
							Toast.LENGTH_SHORT).show();
				} else
				{
					if (-1 == addContact())
					{
						finish();
					}
				}

				break;

			case R.id.addcontacts_mIvHead:
				//TODO
			    get_mEtAccount = mEtAccount.getText().toString().trim();
				if (get_mEtAccount.equals("") || get_mEtAccount.length()<=0)
				{
					Toast.makeText(New_Activity_Addcontacts.this, "请先输入账号", Toast.LENGTH_SHORT).show();
				}
				else {
					ShowPickDialog();
				}
				
				break;

			case R.id.left_button: // 取消按钮返回
				onBackPressed();
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 添加联系人
	 * 
	 */
	private int addContact()
	{
		String name = mEtNickname.getText().toString();
		String account = mEtAccount.getText().toString();

		int exist = -1;
		ArrayList<ContactSort> contacts = New_Application.getInstance()
				.getContactSort();
		for (int i = 0; i < contacts.size(); i++)
		{
			ArrayList<Contact> con = contacts.get(i).getContacts();
			for (int j = 0; j < con.size(); j++)
			{
				String OldName = con.get(j).getName();
				String oldAccount = con.get(j).getAccount();
				if (OldName.equals(name))
				{
					Toast.makeText(mContext, "该昵称已被使用", Toast.LENGTH_SHORT)
							.show();
					return 1;
				} else if (oldAccount.equals(account))
				{
					Toast.makeText(mContext, "该账号已被使用", Toast.LENGTH_SHORT)
							.show();
					return 2;
				}
			}

		}
		if (-1 == exist)
		{    //TODO 
			String sipAddr = mEtSipAddr.getText().toString();
			String initial = HanziToPinyin.getInstance().toPinYin(name);
			Contact contact = new Contact(0, name, account, sipAddr,
					mUserIconPath, "", "", "", initial);
			ContactsDao.getInstance().insertContacts(contact);
			New_Application.getInstance().addNewContact(contact);
		}
		return exist;
	}

	/**
	 * 选择提示对话框
	 */
	protected void ShowPickDialog()
	{
		TextDialog dialog = new TextDialog(mContext, "设置头像", "相册", "相机",
				new OnDialogDismissListener()
				{

					@Override
					public void onDialogDismiss(int which)
					{
						if (which == Dialog.BUTTON_POSITIVE)
						{
							Intent intent = new Intent(mContext,
									PhotoActivity.class);
							intent.putExtra("isUserIcon", true);
							startActivityForResult(intent, PHOTO_REQUEST_ALBUM);
						} else
						{
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							String path = Constants.mImgPath
									+ get_mEtAccount + ".jpg";
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(path)));
							startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
						}
					}
				});
		dialog.show();
	}

}
