package org.linphone.newphone;

import org.linphone.R;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;

public class New_Activity_Feedback extends Activity
{

	private TextView title_text;
	private ImageButton left_button;
	private Button right_button;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_feedback);
		initView();
	}

	private void initView()
	{
		title_text = (TextView)findViewById(R.id.title_text);
		title_text.setTextColor(getResources().getColor(R.color.right_btn_font_down));
		title_text.setText(R.string.feedback);
		
		left_button = (ImageButton)findViewById(R.id.left_button);
		left_button.setOnClickListener(mOnClickListener);
		
		right_button = (Button)findViewById(R.id.right_button);
		right_button.setVisibility(View.VISIBLE);
		right_button.setText(R.string.submit);
		right_button.setOnClickListener(mOnClickListener);
		right_button.setOnTouchListener(mOnTouchListener);
		
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
			case R.id.right_button:
				//right_button.setTextColor(getResources().getColor(R.color.setted));
			default:
				break;
			}
			
		}
	};
	
	private OnTouchListener mOnTouchListener = new OnTouchListener()
	{
		
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				right_button.setTextColor(getResources().getColor(R.color.right_btn_font_down));
				break;
			case MotionEvent.ACTION_UP:
				right_button.setTextColor(getResources().getColor(R.color.right_btn_font_up));
				break;
				
			case MotionEvent.ACTION_CANCEL:
				right_button.setTextColor(getResources().getColor(R.color.right_btn_font_up));
				break;
			default:
				break;
			}
			return false;
		}
	};




}
