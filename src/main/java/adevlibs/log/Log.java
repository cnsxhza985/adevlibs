package adevlibs.log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;
import android.text.TextUtils;

/**
 * @author wangfan
 * @date 2011-8-2
 * @aim 本Log类无缝适配了android系统Log，且增加了将Log输出到本地SD卡的功能
 * 
 */
public final class Log {

	private static File logFile;
	private static String logFileName = "adevlibs_log.txt";
	private static BufferedOutputStream bfs;

	private static Date date;

	private Log() {
	}

	/**
	 * @param tag
	 *            log的标签
	 * @param msg
	 *            log内容
	 */
	public static void record(String tag, String msg) {
		if (logFile == null || bfs == null) {
			synchronized (Log.class) {
				init();
			}
		}

		try {
			if (logFile != null && bfs != null) {
				date = new Date();
				int month = date.getMonth();
				int day = date.getDate();
				int hour = date.getHours();
				int minute = date.getMinutes();
				int seconds = date.getSeconds();

				String time = "--" + month + "/" + day + "/" + hour + "/"
						+ minute + "/" + seconds + "--";

				String tempStr = tag + time + tag + msg + "\r\n";
				bfs.write(tempStr.getBytes());
				bfs.flush();
			}
		} catch (IOException e) {
			android.util.Log.e(tag, " log record exception");
			e.printStackTrace();
		}

	}

	/**
	 * 初始化，会在Log方法在第一次执行的时候被执行，做初始化的工作
	 */
	private static void init() {
		String sdcardState = Environment.getExternalStorageState();
		if (!TextUtils.isEmpty(sdcardState)
				&& sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			File rootDir = Environment.getExternalStorageDirectory();
			logFile = new File(rootDir, logFileName);

			try {
				bfs = new BufferedOutputStream(new FileOutputStream(logFile,
						true));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeStreams() {
		try {
			if (bfs != null) {
				bfs.flush();
				bfs.close();
				bfs = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过Verbos的方法输出log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            消息内容
	 */
	public static void v(String tag, String msg) {
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		msg = fileInfo + ": " + msg;
		android.util.Log.v(tag, msg);
	}

	/**
	 * 通过Danger的方式输出log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            消息内容
	 */
	public static void d(String tag, String msg) {
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		msg = fileInfo + ": " + msg;
		android.util.Log.d(tag, msg);
	}

	/**
	 * 通过Infomation的方式输出log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            消息内容
	 */
	public static void i(String tag, String msg) {
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		msg = fileInfo + ": " + msg;
		android.util.Log.i(tag, msg);
	}

	/**
	 * 通过warning的方式输出log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            消息内容
	 */
	public static void w(String tag, String msg) {
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		msg = fileInfo + ": " + msg;
		android.util.Log.w(tag, msg);
	}

	/**
	 * 通过warning的方式输出log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            消息内容
	 * @param tr
	 *            异常
	 */
	public static void w(String tag, String msg, Throwable tr) {
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		msg = fileInfo + ": " + msg;
		android.util.Log.w(tag, msg, tr);
	}

	/**
	 * 通过error的方式输出log
	 * @param tag	标签
	 * @param msg	消息内容
	 */
	public static void e(String tag, String msg) {
		android.util.Log.e(tag, msg);
		record(tag, msg);
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void e(String tag, String msg, Throwable tr) {
		android.util.Log.e(tag, msg, tr);
		record(tag, msg);
	}
}
