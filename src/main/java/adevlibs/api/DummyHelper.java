package adevlibs.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 
 * @author An Zewei
 * @since 2013-8-1
 * 
 *        假数据创建工具
 * 
 *        <ul>
 *        <li>
 *        <p>
 *        <b>DummyHelper 简单理解如下： 我们本地返回Api要返回的数据，对于不同的api会获取assets里的不同文件</b> --
 *        </p>
 *        <p>
 *        <b>命名规则如下：</b> -- * 截取 "/" 之后 "." 之前的部分作为文件名，扩展名为txt<br>
 *        如 ： "http://host:8080/pa/pb.htm" 的文件应该为pa_pb.txt
 *        </p>
 *        </ul>
 * */
public class DummyHelper {

	private static boolean sbDebug = false;

	/**
	 * @return
	 */
	public static boolean isDebug() {
		return sbDebug;
	}

	/**
	 * Turn off debug
	 */
	public static void turnOff() {
		sbDebug = false;
	}

	/**
	 * Turn on debug
	 */
	public static void turnOn() {
		sbDebug = true;
	}

	/**
	 * 对于不同的api会获取assets里的不同文件，命名如下：
	 * <p>
	 * 截取 "/" 之后 "." 之前的部分作为文件名，扩展名为txt<br>
	 * 如 ： "http://host:8080/pa/pb.htm" 的文件应该为pa_pb.txt
	 * 
	 * @param obj
	 * @return
	 */
	public static String getApiString(String obj) {
		String filepath = getFileName4Api(obj);
		if (TextUtils.isEmpty(filepath)) {
			return "";
		}
		InputStream abpath = DummyHelper.class.getResourceAsStream("/assets/"
				+ filepath + ".txt");
		return new String(InputStreamToByte(abpath));
	}

	private static byte[] InputStreamToByte(InputStream is) {
		try {

			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (Exception e) {
		}
		return new byte[0];
	}

	public static String getFileName4Api(String uri) {
		uri = uri.replace("//", "?");
		StringBuilder builder = new StringBuilder();
		Pattern pattern = Pattern.compile("(?:/)(\\w+)");
		Matcher matcher = pattern.matcher(uri);
		while (matcher.find()) {
			builder.append(matcher.group(1));
			builder.append("_");
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}
}
