
package com.efuture.wechat.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wudong
 * @description 日期工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static final DateFormat Format = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 得到当前日期 不含时间
	 * 
	 * @return date
	 */
	public static Date gettoday() {
		return trunctoday(new Date());
	}
	/**
	 * 截断日期到天
	 * @param d
	 * @return Date
	 */
	public static Date trunctoday(Date d) {
		return truncate(d, Calendar.DAY_OF_MONTH);
	}
	/**
	 * 得到两个日期之间的天数（包含最后一天）
	 * @param startDate
	 * @param endDate
	 * @return 天数
	 */
	public static long getDaysBetween(Date startDate, Date endDate) {
		return (trunctoday(startDate).getTime() - trunctoday(endDate).getTime())
				/ (1000 * 60 * 60 * 24) + 1;
	}
	
	public static String getWeek(Date date)
	{
		return String.valueOf(getDayOfWeek(date) - 1);
	}

	/**
	 * 返回日期是星期几
	 * @param date
	 * @return
	 */
	public static long getDayOfWeek(Date date){
		return toCalendar(date).get(Calendar.DAY_OF_WEEK);
	}
	/**
	 * 获取两个日期之间的所有日期
	 *
	 * @param startTime 开始日期
	 * @param endTime   结束日期
	 * @return
	 */
	public static List<String> getDays(String startTime, String endTime) {

		// 返回的日期集合
		List<String> days = new ArrayList<String>();

		try {
			Date start = Format.parse(startTime);
			Date end = Format.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(Format.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;
	}

	/**
	 * 计算两个日期之间的天数
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 */
	public static int getDaySub(String beginDateStr, String endDateStr)
	{
		int day=0;
		Date beginDate;
		Date endDate;
		try
		{
			beginDate = Format.parse(beginDateStr);
			endDate= Format.parse(endDateStr);
			day=(int) ((endDate.getTime()-beginDate.getTime())/(24*60*60*1000));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 获取指定日期的天数
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDaysByDate(Date date,int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,day);
		return calendar.getTime();
	}

	//判断两个日期是否相等,只判断年月日
	public static boolean isSameDate(Date date1, Date date2) {
		try {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);

			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);

			boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
					.get(Calendar.YEAR);
			boolean isSameMonth = isSameYear
					&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
			boolean isSameDate = isSameMonth
					&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
					.get(Calendar.DAY_OF_MONTH);

			return isSameDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	// 获得本周一与当前日期相差的天数
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return -6;
		} else {
			return 2 - dayOfWeek;
		}
	}

	// 获得当前周 周一的日期
	public static String getCurrentMonday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		String format = Format.format(monday);
		return format;
	}


	// 获得当前周 周日  的日期
	public static String getPreviousSunday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();
		String preMonday = Format.format(monday);
		return preMonday;
	}

	// 获得当前月--开始日期
	public static String getMinMonthDate(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(Format.parse(date));
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			return Format.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获得当前月--结束日期
	public static String getMaxMonthDate(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(Format.parse(date));
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			return Format.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getTodayStr()
	{
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		return sdf.format( gettoday() );
	}

	public static String getDateTimeStr(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		return sdf.format( date );
	}

	public static String getNowStr()
	{
		return getDateTimeStr( new Date() );
	}
}

