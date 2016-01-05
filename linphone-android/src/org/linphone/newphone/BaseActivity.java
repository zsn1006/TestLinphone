package org.linphone.newphone;

import org.linphone.newphone.tools.HttpAsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class BaseActivity extends Activity {
	
	protected HttpAsyncTask mAsyncTask;
	public int mTaskStep = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Aut似懂非懂是rated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
			mAsyncTask.cancel(true);
			mAsyncTask = null;
		}
		mTaskStep = 0;
		super.onDestroy();
	}

}
