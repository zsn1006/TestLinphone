package org.linphone.newphone.tools;

import android.os.Environment;

public class Constants
{

	// 屏幕宽高
	public static int ScreenWidth = 0;
	public static int ScreenHeight = 0;
	// apk版本
	public static String mCurVersion = null;
	public static String mNewVersion = null;
	public static String mNewPath = null;

	// sharedPreference
	public static String mSpLogin = "sp_login";
	public static String mSpUser = "sp_user";
	//add 2015-12-24
	public static String mSpBlackList = "sp_blacklist";
	public static String mSpPersonSetted = "sp_personsetted";

	// sip地址
	public static String userid = "";
	public static String password = "";
	public static String ip = "192.168.10.215";
	public static String port = "5060";

	// 配置服务器地址
	public static String netAddr = "http://192.168.0.0:8080";
	public static String appRootAddr = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/VideoPhone/";
	public static String mImgPath = Constants.appRootAddr + "img/";

	
	public static final String appFolderName = "VideoPhone";
    public static final String mCrashPath = appRootAddr + "crash/";
	// 是否登录标识 "100000"表示未登录
	public static String mUserId = "100000";

}
