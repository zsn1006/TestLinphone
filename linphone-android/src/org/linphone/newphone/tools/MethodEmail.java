package org.linphone.newphone.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email方法
 * @author ty
 * @time 2015/5/12
 */
public class MethodEmail {

	/***
	 * 判断是否邮箱
	 */
	public static boolean isEmail(String email) {

		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	/***
	 * 根据邮箱跳转到对应邮箱登陆网址
	 */
	public static String gotoEmail(String email) {

		String t = email.substring(email.indexOf("@")+1);
        if (t.contentEquals("163.com")) {
            return "mail.163.com";
        } else if (t.contentEquals("vip.163.com")) {
            return "vip.163.com";
        } else if (t.contentEquals("126.com")) {
            return "mail.126.com";
        } else if (t.contentEquals("qq.com") || t.contentEquals("vip.qq.com") || t.contentEquals("foxmail.com")) {
            return "mail.qq.com";
        } else if (t.contentEquals("gmail.com")) {
            return "mail.google.com";
        } else if (t.contentEquals("sohu.com")) {
            return "mail.sohu.com";
        } else if (t.contentEquals("tom.com")) {
            return "mail.tom.com";
        } else if (t.contentEquals("vip.sina.com")) {
            return "vip.sina.com";
        } else if (t.contentEquals("sina.com") || t.contentEquals("sina.com.cn")) {
            return "mail.sina.com.cn";
        } else if (t.contentEquals("yahoo.com") || t.contentEquals("yahoo.com.cn")) {
            return "mail.cn.yahoo.com";
        } else if (t.contentEquals("tom.com")) {
            return "mail.tom.com";
        } else if (t.contentEquals("yeah.com")) {
            return "www.yeah.net";
        } else if (t.contentEquals("21cn.com")) {
            return "mail.21cn.com";
        } else if (t.contentEquals("hotmail.com") || t.contentEquals("msn.com")) {
            return "www.hotmail.com";
        } else if (t.contentEquals("sogou.com")) {
            return "mail.sogou.com";
        } else if (t.contentEquals("188.com")) {
            return "www.188.com";
        } else if (t.contentEquals("139.com")) {
            return "mail.10086.cn";
        } else if (t.contentEquals("189.cn")) {
            return "webmail15.189.cn/webmail";
        } else if (t.contentEquals("wo.com.cn")) {
            return "mail.wo.com.cn/smsmail";
        } else if (t.contentEquals("139.com")) {
            return "mail.10086.cn";
        } else {
            return "";
        }
    };
}
