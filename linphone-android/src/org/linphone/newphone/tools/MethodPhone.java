package org.linphone.newphone.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Phone方法
 * @author ty
 * @time 2015/5/12
 */
public class MethodPhone {

	private TelephonyManager telephonyManager = null;
	private Context context = null;
	
	public MethodPhone(Context context) {

		this.context = context;
		telephonyManager = (TelephonyManager) this.context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	/***
	 * 判断是否手机号
	 */
	public static boolean isMobileNum(String mobiles) {

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/***
	 * 获取手机号
	 * @return String
	 */
	public String getPhoneNo() {
		String phoneNo = telephonyManager.getLine1Number();
		return phoneNo;
	}
	
	/***
	 * 获取IMSI
	 * @return String
	 */
	public String getIMSI() {
		String imsi = telephonyManager.getSubscriberId();
		return imsi;
	}
	
	/***
	 * 获取IMEI
	 * @return String
	 */
	public String getIMEI() {
		String imei = telephonyManager.getDeviceId();
		return imei;
	}
}
