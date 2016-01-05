package org.linphone.newphone.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Time方法
 * @author ty
 * @time 2015/5/12
 */
public class MethodTime {

	/**
	 * 获取两个日期之间的间隔天数
	 * 
	 * @return
	 */
	public static int getGapCount(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}

	/***
	 * 计算今天是星期几 日 1， 一2， 二3， 三4， 四5， 五6， 六7
	 */
	public static int onGetWeekDay() {

		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String weekday = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		return Integer.valueOf(weekday);
	}


}
