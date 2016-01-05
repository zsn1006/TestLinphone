package org.linphone.newphone.tools;

import android.util.Log;

/**
 * 类名：LogUtil 功能描述：LOG打印工具类
 * 
 * @author android_ls
 */
public class LogUtil {
	private static boolean isPrint = true;

	private static final String TAG = "NewPhone";

	public static void i(String msg) {
		if (isPrint) {
			Log.i(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (isPrint) {
			Log.e(TAG, msg);
		}
	}
	
	public static void d(String msg) {
		if (isPrint) {
			Log.i(TAG, msg);
		}
	}

	public static void v(String msg) {
		if (isPrint) {
			Log.e(TAG, msg);
		}
	}
	
	public static void w(String msg) {
		if (isPrint) {
			Log.i(TAG, msg);
		}
	}

	public static void log(String msg) {
		if (isPrint) {
			Log.v(TAG, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (isPrint) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isPrint) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (isPrint) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (isPrint) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isPrint) {
			Log.e(tag, msg);
		}
	}
}
