package com.taixin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/***
 * 关于时间处理的接口
 * 
 * @author Administrator
 *
 */
public class TimeUtil {

	public TimeUtil() {
	}

	/*
	 * attention : Maybe the return value could be less than zero  
	 */
	public static int dayDifference(String dateBegin, String dateEnd) {
		int dayDuration = -1;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date begin = df.parse(dateBegin);
			Date end = df.parse(dateEnd);

			long milliSeconds = end.getTime() - begin.getTime();
			dayDuration = (int) (milliSeconds / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			
		}
		
		
		return dayDuration;
	}
	
	/**
	 * 计算时间差，输入格式为2016-07-25 14:58:30，返回时间差格式为7.5小时
	 * 
	 * @param timeStart
	 *            开始时间
	 * @param timeEnd
	 *            结束时间
	 * 若timeStart晚于(>)timeEnd，则差值为负
	 */
	public static float dateDifference(String timeStart, String timeEnd) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		float diffs = 0.0f;

		try {
			Date start = df.parse(timeStart);
			Date end = df.parse(timeEnd);
			long diff = end.getTime() - start.getTime(); // 差值为微妙级别

			int days = (int) (diff / (1000 * 60 * 60 * 24));
			int hours = (int) ((diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
			int minutes = (int) ((diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60));

			diffs = days * 24 + hours + ((float) minutes / 60);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return diffs;
	}
	
	/**
	 * 计算时间差，输入格式为2016-07-25 14:58:30，返回时间差格式为7.5小时
	 * 
	 * @param timeStart
	 *            开始时间
	 * @param timeEnd
	 *            结束时间
	 * 若timeStart晚于(>)timeEnd，则差值为负
	 */
	public static float dateDifferenceWithFormat(String timeStart, String timeEnd, String format) {
		DateFormat df = new SimpleDateFormat(format);
		float diffs = 0.0f;

		try {
			Date start = df.parse(timeStart);
			Date end = df.parse(timeEnd);
			long diff = end.getTime() - start.getTime(); // 差值为微妙级别

			int days = (int) (diff / (1000 * 60 * 60 * 24));
			int hours = (int) ((diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
			int minutes = (int) ((diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60));

			diffs = days * 24 + hours + ((float) minutes / 60);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return diffs;
	}

	/**
	 * 返回格式 2016-07-25 13:44:50 分别以字节形式返回。 年占两个字节，其他的各占一个字节，总共7个字节。
	 */
	public static byte[] getUTCTimeTobyte() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONDAY) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		byte[] UTCTime = new byte[7];

		UTCTime[0] = (byte) (year);
		UTCTime[1] = (byte) (year >> 8);
		UTCTime[2] = (byte) (month);
		UTCTime[3] = (byte) (day);
		UTCTime[4] = (byte) (hour);
		UTCTime[5] = (byte) (minute);
		UTCTime[6] = (byte) (second);

		return UTCTime;
	}

	/**
	 * 年-月-日 小时:分钟:秒的格式输入，年占两个字节，其他各占一个字节，共7个字节。 返回格式:2016-05-07 12:23:20
	 */
	public static String getTimeFromUTCByte(byte[] utc) {
		int year = (utc[0] & 0xFF) + ((utc[1] & 0xFF) << 8);
		int month = (utc[2] & 0xFF);
		int day = (utc[3] & 0xFF);
		int hour = (utc[4] & 0xFF);
		int minute = (utc[5] & 0xFF);
		int second = (utc[6] & 0xFF);

		String time = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
		
		return time;
	}

	public static long getUTCTime() {

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONDAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);


		final int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		final int dstOffset = cal.get(Calendar.DST_OFFSET);

		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		cal.set(year, month, day, hour, minute, second);
		long utc = cal.getTime().getTime();
		return utc;
	}

	public static String getTimeFromUTC(long utc) {

		Date date = new Date(utc);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


		return formatter.format(date);
	}

	/**
	 * 返回格式:2016-5-7 12:23:20
	 */
	public static String getCurrentTime() {
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = ca.getTime();
		return df.format(date);
	}

	public static int getCurWeek() {
		int week = 1;
		Calendar ca = Calendar.getInstance();
		week = ca.get(Calendar.DAY_OF_WEEK);
		switch (week) {
		case Calendar.SUNDAY:
			return 7;
		case Calendar.MONDAY:
			return 1;
		case Calendar.TUESDAY:
			return 2;
		case Calendar.WEDNESDAY:
			return 3;
		case Calendar.THURSDAY:
			return 4;
		case Calendar.FRIDAY:
			return 5;
		case Calendar.SATURDAY:
			return 6;
		default:
			return 1;
		}
	}

	public static int getCurYear() {
		int year = 1970;
		Calendar ca = Calendar.getInstance();
		year = ca.get(Calendar.YEAR);
		return year;
	}

	public static int getCurHour() {
		int hour = 1;
		Calendar ca = Calendar.getInstance();
		hour = ca.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	public static int getCurMonth() {
		int month = 1;
		Calendar ca = Calendar.getInstance();
		month = ca.get(Calendar.DAY_OF_MONTH) + 1;
		return month;
	}

	/**
	 * 获取某月第一天日期，格式为2016-08-18
	 * 
	 * @param year
	 *            年份
	 * @param month
	 * @return Date
	 */
	public static String getMonthFirst(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date currMonthFirst = calendar.getTime();

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		return f.format(currMonthFirst);
	}

	/**
	 * 获取某月最后一天日期，格式为2016-08-18
	 * 
	 * @param year
	 *            年份
	 * @param month
	 * @return Date
	 */
	public static String getMonthLast(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.roll(Calendar.DAY_OF_MONTH, -1);
		Date currYearLast = calendar.getTime();

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		return f.format(currYearLast);
	}

	/**
	 * 格式化日期 2016-08-13
	 * 
	 * @param date
	 *            日期对象
	 * @return String 日期字符串
	 */
	public static String formatDate(Date date) {
		String DEFAULT_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
		String sDate = f.format(date);
		return sDate;
	}

	/** 根据当前天的日期得到某一天的时间，格式为2016-08-16，offset为0表示当前天日期，offset+为当前日期往后，offset-为当前日期往前 */
	public static String getAnyDayDate(int offset) {
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		ca.setTime(ca.getTime());
		ca.add(Calendar.DAY_OF_MONTH, offset);
		Date date = ca.getTime();

		return df.format(date);
	}

	/** 根据当前月的日期得到某月的时间，格式为2016-08，offset为0表示当前月日期，offset+为当前日期往后，offset-为当前日期往前 */
	public static String getAnyMonthDate(int offset) {
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");

		ca.setTime(ca.getTime());
		ca.add(Calendar.MONTH, offset);
		Date date = ca.getTime();

		return df.format(date);
	}

	/** 判断某月总共有多少天 */
	public static int getMonthDays(int year, int month) {
		int number = 0;

		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			number = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			number = 30;
			break;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
				number = 29;
			else
				number = 28;
			break;
		default:
			break;
		}

		return number;
	}

	/** 得到一年给定周的第一天，格式为2016-08-17 */
	public static String getStartDayOfWeekNo(int year, int weekNo) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.YEAR, year);

		cal.set(Calendar.WEEK_OF_YEAR, weekNo);

		String weekFirstDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return weekFirstDate;
	}

	/** 得到一年给定周的最后一天，格式为2016-08-17 */
	public static String getEndDayOfWeekNo(int year, int weekNo) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.YEAR, year);

		cal.set(Calendar.WEEK_OF_YEAR, weekNo);
		cal.add(Calendar.DAY_OF_WEEK, 6);

		String weekEndDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return weekEndDate;
	}

	/** 得到当前周是本年的第几周 ，其中周一是一周的第一天 */
	public static int getCurWeekNo() {
		Calendar cal = Calendar.getInstance();
		int weekNo = cal.get(Calendar.WEEK_OF_YEAR);

		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			weekNo -= 1;

		return weekNo;
	}

	/** 得到一年总共有多少周 */
	public static int getAllWeekofYear(int year) {
		int allWeekNo = 52;
		String date = getStartDayOfWeekNo(year, 53);
		if (date.substring(0, 4).equals(year + "")) // 判断年度是否相符，如果相符说明有53个周。
			allWeekNo = 53;
		else
			allWeekNo = 52;

		return allWeekNo;
	}

	/** 得到指定日期的后一天日期 ，格式为2016-09-10 */
	public static String getNextDayofSpecifiedDay(String specifiedDay) {
		Calendar c = Calendar.getInstance();

		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/** 得到指定日期的前一天日期 ，格式为2016-09-10 */
	public static String getPreviousDayofSpecifiedDay(String specifiedDay) {
		Calendar c = Calendar.getInstance();

		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 比较两个日期的先后，若是date1<=date2则返回false，否则返回true。
	 * format表示要比较的日期的格式，诸如"yyyy-MM-dd"或者"yyyy-MM"
	 */
	public static boolean compareDate(String date1, String date2, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date time1 = null;
		Date time2 = null;
		try {
			time1 = sdf.parse(date1);
			time2 = sdf.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

//		if (time1.before(time2))// 表示date1小于date2
//			return false;
//		else
//			return true;
		if (time1.after(time2))// 表示date1大于date2
			return true;
		else
			return false;
	}

	/**
	 * @param time
	 *            输入时间，格式为10:54
	 * @param minute
	 *            从指定时间往前减去minute分钟
	 * @return 返回从指定时间减去minute分钟后的时间
	 */
	public static String subtractDateMinute(String time, int minute) {
		DateFormat format = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (date == null)
			return null;

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, -minute);
		date = cal.getTime();

		return format.format(date);
	}

	/** 从字符串中得到数字，譬如从“10分钟”中得到“10” */
	public static String getTimeFromString(String str) {
		String minuteStr = "";
		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) >= 48) && (str.charAt(i) <= 57))
				minuteStr += str.charAt(i);
		}

		return minuteStr;
	}

	/** 根据指定格式得到当前时间 */
	public static String getCurTimeByFormat(String format) {
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = ca.getTime();

		return df.format(date);
	}

	/** 得到当前时间的秒数 */
	public static int getCurTimeToSeconds() {
		Calendar ca = Calendar.getInstance();
		int hour = ca.get(Calendar.HOUR_OF_DAY);
		int minute = ca.get(Calendar.MINUTE);
		int sec = ca.get(Calendar.SECOND);
		int nNowTimeToSeconds = hour * 3600 + minute * 60 + sec;

		return nNowTimeToSeconds;
	}
	
	/**
	 * date为指定的年月日，其中年占俩字节、低位在先，月占一个字节，日占一个字节。
	 * time格式是小时:分钟:秒（20:00:00），将给定的日期拼接转换为String格式时间输出。
	 * return:格式为2017-09-04 10:21:00
	 */
	public static String dateConvertAndJoint(byte[] date, String time) {
		int year = (date[0] & 0xFF) + ((date[1] & 0xff) << 8);
		int month = (date[2] & 0xFF);
		int day = (date[3] & 0xFF);

		int hourOfDay;
		int minute;
		int second;
		String[] time0 = time.split(":");
		if (time0.length == 3) {
			hourOfDay = Integer.parseInt(time0[0]);
			minute = Integer.parseInt(time0[1]);
			second = Integer.parseInt(time0[2]);
		} else {
			hourOfDay = 0;
			minute = 0;
			second = 0;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hourOfDay, minute, second);

		String timeConvert = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		return timeConvert;
	}
	
	/**
	 * @param time
	 *            输入时间，格式为09:22
	 * @param minute
	 *            从指定时间往后加上minute分钟
	 * @return 返回从指定时间加上minute分钟后的时间
	 */
	public static String addDateMinute(String time, int minute) {
		DateFormat format = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		date = calendar.getTime();

		return format.format(date);
	}

}
