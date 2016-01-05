package org.linphone.newphone;

import org.linphone.R;
import org.linphone.newphone.tools.Constants;
import org.linphone.newphone.tools.ExitApplication;
import org.linphone.newphone.tools.SwitchButton;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class New_Activity_Setted extends Activity
{
	// private RelativeLayout topBar;
	private TextView title_text;
	private ImageButton left_button;
	private View lay_about;
	private Button setted_exit;
	// 声音开关
	private SwitchButton setted_voice_switch;
	// 震动开关
	private SwitchButton setted_vibration_switch;
	// 系统声音服务声明
	private AudioManager mAudioManager;
	
	private int voice = 0; // voice=1,有声音； voice=2,无声音；
	private int vibration = 0; // vibration=3,振动； vibration=4,不振动；
	/*
	 * 为0时表示初始状态
	 * 
	 * 1,3 有声音，震动； 1,4 有声音，不震动；
	 * 
	 * 2,3 无声音，震动； 2,4 无声音，不震动；
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_setted);
		initView();
	}

	private void initView()
	{
		// topBar = (RelativeLayout)findViewById(R.id.setted_topbar);
		title_text = (TextView) findViewById(R.id.title_text);
		left_button = (ImageButton) findViewById(R.id.left_button);
		lay_about = findViewById(R.id.lay_about);
		setted_exit = (Button) findViewById(R.id.setted_exit);
		setted_voice_switch = (SwitchButton) findViewById(R.id.setted_voice_switch);
		setted_vibration_switch = (SwitchButton) findViewById(R.id.setted_vibration_switch);

		title_text.setText(R.string.setted);
		title_text.setTextColor(getResources().getColor(R.color.black));
		left_button.setOnClickListener(mOnClick);
		lay_about.setOnClickListener(mOnClick);
		setted_exit.setOnClickListener(mOnClick);
		setted_voice_switch
				.setOnCheckedChangeListener(mOnCheckedChangeListener);
		SharedPreferences spSetted = getSharedPreferences(
				Constants.mSpPersonSetted, MODE_PRIVATE);

		setted_vibration_switch
				.setOnCheckedChangeListener(mOnCheckedChangeListener);

		// 获取系统声音服务
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		setted_voice_switch.setChecked(spSetted.getBoolean(
				"setted_voice_switch", true));

		setted_vibration_switch.setChecked(spSetted.getBoolean(
				"setted_vibration_switch", true));

	}

	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked)
		{
			// TODO Auto-generated method stub
			SharedPreferences spPersonSetted = getSharedPreferences(
					Constants.mSpPersonSetted, MODE_PRIVATE);
			Editor editor = spPersonSetted.edit();
			switch (buttonView.getId())
			{
			case R.id.setted_voice_switch:
				if (isChecked)
				{
					voice = 1;
					if (voice == 1 && vibration == 3) //有声音振动
					{
					    ringAndVibrate(mAudioManager);	
					}
					else if (voice == 1 && vibration == 4)     //有声音不振动 
					{
						ring(mAudioManager);		
					}
					else if (voice == 1 && vibration == 0) 
					{
					  	mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					}
					
					
					editor.putBoolean("setted_voice_switch", true);
					editor.commit();
				} else
				{	
					voice = 2;
					if (voice == 2 && vibration == 3) //无声，振动
					{
						noRingAndVibrate(mAudioManager);
					}
					else if (voice == 2 && vibration == 4) //无声，无振动
					{
						noRingAndVibrate(mAudioManager);
					}
					else if (voice == 2 && vibration == 0)
					{
					   	mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					}
					
					editor.putBoolean("setted_voice_switch", false);
					editor.commit();
				}
				break;
			case R.id.setted_vibration_switch:
				if (isChecked)
				{
					vibration = 3;
					
					if (voice == 2 && vibration == 3)  //无声音，振动
					{
						onlyvibrate(mAudioManager);
					}
					else if (voice == 1 && vibration == 3)   //有声音，振动
					{
						ringAndVibrate(mAudioManager);	
					}
					else if (voice == 0 && vibration == 3) {
						mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					}
					
					editor.putBoolean("setted_vibration_switch", true);
					editor.commit();
				} else
				{
					vibration = 4;
					
					if (voice == 1 && vibration == 4)   //有声音，不振动
					{
						ring(mAudioManager);
					}
					else if (voice == 2 && vibration == 4) // 无声音，不振动
					{
						noRingAndVibrate(mAudioManager);
					}
					else if (voice == 0 && vibration == 4) 
					{
						mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					}
					
					editor.putBoolean("setted_vibration_switch", false);
					editor.commit();
				}
				break;
			default:
				break;
			}

		}
	};

	
	/**
	 * 有声音，有振动
	 * 
	 */
	@SuppressWarnings("deprecation")
	void ringAndVibrate(AudioManager audio)
	{
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
		Toast.makeText(New_Activity_Setted.this, "声音-----振动", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 有声音，不振动
	 * 
	 */
	@SuppressWarnings("deprecation")
	void ring(AudioManager audio)
	{
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
		Toast.makeText(New_Activity_Setted.this, "声音-----", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 无声音，无振动
	 * 
	 */
	@SuppressWarnings("deprecation")
	void noRingAndVibrate(AudioManager audio)
	{
		audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
		Toast.makeText(New_Activity_Setted.this, "无声音--无振动", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 无声音，振动
	 * 
	 */
	@SuppressWarnings("deprecation")
	void onlyvibrate(AudioManager audio)
	{
		audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
		Toast.makeText(New_Activity_Setted.this, "无声音-----振动", Toast.LENGTH_LONG).show();
	}
	
	private OnClickListener mOnClick = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.left_button:
				onBackPressed();
				break;

			case R.id.lay_about:
				Intent intent_about = new Intent(New_Activity_Setted.this,
						New_Activity_AboutUs.class);
				startActivity(intent_about);
				break;

			case R.id.setted_exit:
				ExitApplication.getInstance().exit();
				break;

			default:
				break;
			}

		}

	};

}
