package org.linphone.newphone;

import org.linphone.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.app.Activity;

public class New_Activity_Remarks extends Activity
{
	private ImageButton detail_left_button;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new__activity__remarks);
		init();
	}


	private void init()
	{
		detail_left_button = (ImageButton) findViewById(R.id.detail_left_button);
		
		detail_left_button.setOnClickListener(mOnClickListener);
		
	}
	
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

			default:
				break;
			}
			
		}
	};

}
