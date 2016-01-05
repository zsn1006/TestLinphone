package org.linphone.newphone;

import java.io.File;

import org.linphone.R;
import org.linphone.newphone.photo.CutPicActivity;
import org.linphone.newphone.photo.PhotoActivity;
import org.linphone.newphone.photo.TextDialog;
import org.linphone.newphone.photo.TextDialog.OnDialogDismissListener;
import org.linphone.newphone.photo.Util;
import org.linphone.newphone.tools.Constants;
import org.linphone.newphone.view.MyDialog_Edit;
import org.linphone.newphone.view.MyDialog_Edit.DialogEditCallBack;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class New_Mycenter_Activity extends Activity
{
	private ImageButton left_button;
	private TextView title_text;
	private ImageView mIvHead;
	private TextView tv_show_nickname;
	private TextView tv_show_account;
	private TextView tv_show_sip_address;
	
	private String mUserIconPath = "";
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_ALBUM = 2;// 相册
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_mycenter);
		onCreateDir(Constants.mImgPath);
		initView();
	}

	private void initView()
	{
		left_button = (ImageButton) findViewById(R.id.left_button);
		left_button.setOnClickListener(mOnClickListener);

		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText(R.string.my_center);
		title_text.setTextColor(getResources().getColor(R.color.black));
		
		tv_show_nickname = (TextView) findViewById(R.id.tv_show_nickname);
	    tv_show_account = (TextView) findViewById(R.id.tv_show_account);
	    tv_show_account.setText(Constants.userid);
	    tv_show_sip_address = (TextView) findViewById(R.id.tv_show_sip_address);
        
		mIvHead = (ImageView) findViewById(R.id.mIvHead);
		mIvHead.setOnClickListener(mOnClickListener);
		tv_show_nickname.setOnClickListener(mOnClickListener);
		tv_show_account.setOnClickListener(mOnClickListener);
		tv_show_sip_address.setOnClickListener(mOnClickListener);

		showPhoto();
	}

	private OnClickListener mOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.left_button:
				onBackPressed();
				break;
			case R.id.mIvHead:
				ShowPickDialog();
				break;
			case R.id.tv_show_nickname:
				Dialog_Edit(tv_show_nickname, "请输入新的昵称", InputType.TYPE_CLASS_TEXT, "输入不能为空");
				break;
			case R.id.tv_show_account: 
				//Dialog_Edit(tv_show_account, "请输入账号", InputType.TYPE_CLASS_TEXT, "输入不能为空");
				break;
			case R.id.tv_show_sip_address: 
				//Dialog_Edit(tv_show_sip_address, "请输入sip地址", InputType.TYPE_CLASS_TEXT, "输入不能为空");
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 选择提示对话框
	 */
	private void ShowPickDialog()
	{
		TextDialog dialog = new TextDialog(New_Mycenter_Activity.this, "设置头像",
				"相册", "相机", new OnDialogDismissListener()
				{

					@Override
					public void onDialogDismiss(int which)
					{
						if (which == Dialog.BUTTON_POSITIVE)
						{
							Intent intent = new Intent(
									New_Mycenter_Activity.this,
									PhotoActivity.class);
							intent.putExtra("isUserIcon", true);
							startActivityForResult(intent, PHOTO_REQUEST_ALBUM);
						} else
						{
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							String path = Constants.mImgPath
									+ Constants.mUserId + ".jpg";
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(path)));
							startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
						}

					}
				});
		dialog.show();
	}

	/**
	 * 
	 * @param title
	 * @param error
	 */
	protected void Dialog_Edit(TextView view,  String title, int inputType,  String error)
	{
		// TODO Auto-generated method stub
        DialogEditCallBack callBack = new DialogEditCallBack()
		{
			
			@Override
			public void setResult(String result)
			{
				// TODO Auto-generated method stub
				if (!result.equals("cancel"))
				{
					Toast.makeText(New_Mycenter_Activity.this, "请输入--------", Toast.LENGTH_LONG).show();
				}
				
			}
		};
		MyDialog_Edit dialog_Edit = new MyDialog_Edit(New_Mycenter_Activity.this, title, error, callBack);
		dialog_Edit.show();
	}

	/**
	 * 从照片保存的路径获得照片并且显示
	 */
	private void showPhoto()
	{
		mUserIconPath = Constants.mImgPath + Constants.mUserId + ".jpg";
		File f = new File(mUserIconPath);
		if (f.exists())
		{
			Bitmap bm = BitmapFactory.decodeFile(mUserIconPath);
			if (bm == null)
			{
				f.delete();
			} else
			{
				mIvHead.setImageBitmap(Util.toRoundBitmap(bm));
			}
		} else
		{
			mIvHead.setImageResource(R.drawable.portrait);
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
		case PHOTO_REQUEST_CAMERA: // 拍照时调用
			if (resultCode == -1)
			{
				onNextActivity(null);
			}
			break;
		case PHOTO_REQUEST_CUT: // 剪裁完成后调用
			if (resultCode == 123)
			{
				showPhoto();
			}
			break;
		case PHOTO_REQUEST_ALBUM: // 相册调用
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
		Intent intent = new Intent(New_Mycenter_Activity.this,
				CutPicActivity.class);
		if (path != null)
		{
			intent.putExtra("fullPath", path);
		}
		intent.putExtra("bmpPath", Constants.mImgPath);
		intent.putExtra("bmpName", Constants.mUserId + ".jpg");
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private void onCreateDir(String path)
	{
		File file = new File(path);
		if (!file.exists())
		{
			file.mkdirs();
		}

	}

}
