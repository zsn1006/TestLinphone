package org.linphone.newphone;

import java.io.File;

import org.linphone.R;
import org.linphone.newphone.photo.Util;
import org.linphone.newphone.tools.Constants;
import org.linphone.newphone.tools.SwitchButton;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class New_Activity_DetailedInformation extends Activity
{
	private ImageButton detail_left_button;
	private ImageView detailed_information_mIvHead;
    private View detailed_information_lay_setted;
    private TextView detailed_information_tv_show_account;
    private TextView detailed_information_tv_nickname;
    private String account;         //得到LinkmanAdapter2传递来的联系人账号
    private String nick_name;       //得到昵称
    private SwitchButton setted_vibration_switch;   //添加黑名单的按钮

  
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_detailed_information);
		init();
	}

	private void init()
	{
		detail_left_button = (ImageButton) findViewById(R.id.detail_left_button);
		detailed_information_lay_setted = findViewById(R.id.detailed_information_lay_setted);
		detailed_information_tv_show_account = (TextView) findViewById(R.id.detailed_information_tv_show_account);	
		detailed_information_mIvHead = (ImageView) findViewById(R.id.detailed_information_mIvHead);
		detailed_information_tv_nickname = (TextView) findViewById(R.id.detailed_information_tv_nickname);
		//加入黑名单的开关按钮
		setted_vibration_switch = (SwitchButton) findViewById(R.id.setted_vibration_switch);
		
		detailed_information_lay_setted.setOnClickListener(mOnClickListener);
		detail_left_button.setOnClickListener(mOnClickListener);
		detailed_information_mIvHead.setOnClickListener(mOnClickListener);
		setted_vibration_switch.setOnCheckedChangeListener(mOnCheckedChangeListener);	
		
		//接收LinkmanAdapter2传递来的联系人账号
		Bundle bundle = this.getIntent().getExtras();
	    account = bundle.getString("bundle_account");
		nick_name = bundle.getString("bundle_nick_name");
		detailed_information_tv_show_account.setText(account);  
		detailed_information_tv_nickname.setText(nick_name);
		
		showPhoto();   //显示头像
		SharedPreferences spBlackList = getSharedPreferences(Constants.mSpBlackList, MODE_PRIVATE);
		//TODO
	    //根据用户的设置读取黑名单的开关状态
		setted_vibration_switch.setChecked(spBlackList.getBoolean(account, false));
	}
	
	private void showPhoto() 
	{
		// TODO Auto-generated method stub
		String UserIconPath = Constants.mImgPath + account + ".jpg";
		File f = new File(UserIconPath);
		if (f.exists())
		{
			Bitmap bm = BitmapFactory.decodeFile(UserIconPath);
			if (bm == null)
			{
				f.delete();
			}
			else {
				detailed_information_mIvHead.setImageBitmap(Util.toRoundBitmap(bm));
			}
		}
		else {
			detailed_information_mIvHead.setImageResource(R.drawable.portrait);
		}
		
	}

	/**
	 * 
	 * 黑名单按钮改变监听事件
	 * 
	 */
	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener()
	{
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			// TODO Auto-generated method stub
			SharedPreferences spBlackList = getSharedPreferences(Constants.mSpBlackList, MODE_PRIVATE);
			Editor editor = spBlackList.edit();
			
			if (isChecked)
			{
				editor.putBoolean(account, true);
				Toast.makeText(New_Activity_DetailedInformation.this, "开", 2000).show();
			}
			else {
				editor.putBoolean(account, false);
				Toast.makeText(New_Activity_DetailedInformation.this, "关", 2000).show();
			}
			editor.commit();
		}
	};
	
	//单击事件
	private OnClickListener mOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			switch (v.getId())
			{
			case R.id.detail_left_button:
				onBackPressed();
				break;

			case R.id.detailed_information_lay_setted:
				Intent setted_intent = new Intent(New_Activity_DetailedInformation.this, New_Activity_Remarks.class);
				startActivity(setted_intent);
				break;
				
			default:
				break;
			}
			
		}
	};

}
