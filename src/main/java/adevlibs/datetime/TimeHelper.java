package adevlibs.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 针对android APP的一些操作
 * 
 * @author wangfan
 * 
 *         <p>
 *         TimeHelper覆盖的范围为:
 *         </p>
 *         <ul>
 *         <li>Date和String之间的转换，long和日期String之间的转换，long和Date之间的转换,int和Date之间的转换</li>
 *         <li>根据包名 卸载现有的APP</li>
 *         <li>APP 快捷图标（ShortCut）操作（创建 和 删除）</li>
 *         <li>Activity对应的Window大小</li>
 *         </ul>
 * 
 */
public class TimeHelper {
	
	public static final String FORMAT_24H_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_12H_Y_M_D_H_M_S = "yyyy-MM-dd hh:mm:ss";

	/**
	 * convert a Data to String according to it's formatType
	 * @param data
	 * @param formatType
	 * @return
	 */
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}
	
	/**
	 * convert string to data according to formatType
	 * @param strTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public static Date stringToDate(String strTime, String formatType)
			throws ParseException, java.text.ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}	

	/**
	 * change long to time string according to formatType
	 * @param currentTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public static String longToString(long currentTime, String formatType)
			throws ParseException, java.text.ParseException {
		Date date = longToDate(currentTime, formatType); 
		String strTime = dateToString(date, formatType); 
		return strTime;
	}

	/**
	 * convert a timestring to long according to formatType
	 * @param strTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public static long stringToLong(String strTime, String formatType)
			throws ParseException, java.text.ParseException {
		Date date = stringToDate(strTime, formatType); 
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); 
			return currentTime;
		}
	}
	
	/**
	 * convert long to data according to formatType
	 * @param currentTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public static Date longToDate(long currentTime, String formatType)
			throws ParseException, java.text.ParseException {
		Date dateOld = new Date(currentTime); 
		String sDateTime = dateToString(dateOld, formatType); 
		Date date = stringToDate(sDateTime, formatType); 
		return date;
	}

	/**
	 * convert a Date to long 
	 * @param date
	 * @return
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	/**
	 * convert a int to Date according to formatType
	 * @param number
	 * @param formatType
	 * @return
	 */
	public static String numToDate(int number, String formatType) {
		Date date = new Date(number);
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}
}
