package adevlibs.datetime;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作相关的方法
 * 
 * @author wangfan
 * 
 *         <p>
 *         本工具类中覆盖的范围为:
 *         </p>
 *         <ul>
 *         <li>获取：</li>
 *         <li>标准的北京时间，今天日期，字符串对应的日期是周几</li>
 *         <li>判断：</li>
 *         <li>字符串是否是日期格式，</li>
 *         <li>比较：</li>
 *         <li>日期之间的先后关系，日期之间的间隔</li>
 *         </ul>
 * 
 */
public class DateHelper {
	/** 默认的时间格式 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 获取网络时间（北京时间）千万注意当前方法会去联网，不建议直接在主线程中调用。
	 * 
	 * @param dateFormat
	 *            日期格式
	 * @return 北京时间
	 */
	public static String getNetBeijingDate(String dateFormat) {
		String result = null;
		URL url = null;
		URLConnection urlConnection = null;
		Date date = null;
		DateFormat dateFromat = null;
		try {
			url = new URL("http://www.bjtime.cn");
			urlConnection = url.openConnection();// 生成连接对象
			urlConnection.connect(); // 发出连接
			long ld = urlConnection.getDate(); // 取得网站日期时间
			date = new Date(ld); // 转换为标准时间对象
			dateFromat = new SimpleDateFormat(dateFormat);
			result = dateFromat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			url = null;
			urlConnection = null;
			date = null;
			dateFromat = null;
		}

		return result;
	}

	/**
	 * 按照期望的时间格式，返回今天的时间. 默认的格式(DateHelper.DEFAULT_DATE_FORMAT) 输出为：
	 * “2014-07-24”
	 * 
	 * @param format
	 *            时间格式
	 * @return 现在的日期时间
	 */
	public static String getTodayString(String format) {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 返回字符串对应的日期是周几。 Calendar 中对应的时间关系为：SUNDAY = 1; MONDAY = 2; TUESDAY =
	 * 3;WEDNESDAY = 4;THURSDAY = 5; FRIDAY = 6;SATURDAY = 7;
	 * 
	 * @param strDate
	 *            String格式的时间 （比如："2014-7-24"）
	 * @return 一周中的第几天 注意SUNDAY是第一天
	 */
	public static int getWeekDay(String strDate) {
		return getCalendar(strDate).get(java.util.Calendar.DAY_OF_WEEK);
	}

	/**
	 * 判断一个字符串的格式是否符合DateHelper.DEFAULT_DATE_FORMAT的格式
	 * 
	 * @param strDate
	 * @return
	 */
	public static boolean isDate(String strDate) {
		return isDate(strDate, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 判断一个字符串的格式是否符合指定的dateFormat格式
	 * 
	 * @param oriStr
	 * @param dateFormat
	 * @return
	 */
	public static boolean isDate(String oriStr, String dateFormat) {
		if (!isEmpty(oriStr)) {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			formatter.setLenient(false);
			try {
				formatter.format(formatter.parse(oriStr));
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 如果字符串是null或者其长度为0，返回true
	 * 
	 * @param str
	 *            待检验的字符串
	 * @return true 是否字符串为null或者长度为0
	 */
	private static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 比较一个日期是否在另一个日期之前。 日期的格式都必须是 DateHelper.DEFAULT_DATE_FORMAT
	 * 
	 * @param date1
	 *            第一个日期
	 * @param date2
	 *            第二个日期
	 * @return
	 */
	public static boolean isDateBefore(String date1, String date2) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			return sdf.parse(date1).before(sdf.parse(date2));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断一个日期是否在另一个日期之后。 日期的格式都必须是 DateHelper.DEFAULT_DATE_FORMAT
	 * 
	 * @param date1
	 *            第一个日期
	 * @param date2
	 *            第二个日期
	 * @return
	 */
	public static boolean isDateAfter(String date1, String date2) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			return sdf.parse(date1).after(sdf.parse(date2));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断两个日期之间间隔多少天 (date2-date1)
	 * 
	 * @param dateStr1
	 *            第一个date
	 * @param dateStr2
	 *            第二个date
	 * @return 间隔天数
	 */
	public static int intervalDays(String dateStr1, String dateStr2) {
		if (!isDate(dateStr1) || !isDate(dateStr2))
			return 0;
		Calendar cal1 = getCalendar(dateStr1);
		Calendar cal2 = getCalendar(dateStr2);
		float diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();
		float days = Math.round(diff / (24 * 60 * 60 * 1000));

		return new Float(days).intValue();
	}

	/**
	 * 按照字符串对应的日期实例化一个Calendar，格式采用默认格式"DateHelper.DEFAULT_DATE_FORMAT"
	 * 
	 * @param strDate
	 *            String格式的时间 （比如："2014-7-24"）
	 * @return 被初始化好的Calendar
	 */
	private static Calendar getCalendar(String strDate) {
		return getCalendar(strDate, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 按照字符串对应的日期实例化一个Calendar
	 * 
	 * @param strDate
	 *            String格式的时间
	 * @param dateFormat
	 *            时间的格式
	 * @return 被初始化好的Calendar
	 */
	private static Calendar getCalendar(String strDate, String dateFormat) {
		if (strDate == null || strDate.trim().equals(""))
			return null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		java.util.Date dt;
		try {
			dt = sdf.parse(strDate);
			cal.setTime(dt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cal;
	}

}
