package adevlibs.sdk;

import android.os.Build;

/**
 * 
 * @author wangfan
 * @since 2013-7-26
 * 
 *        android系统SDK信息工具
 * 
 *        <ul>
 *        <li>
 *        <p>
 *        <b>SDKHelper 简单理解如下： 是对SDK信息访问的常用方法的集合</b> --
 *        </p>
 *        </ul>
 * */
public class SDKHelper {
	/**
	 * @return 2.2以上
	 */
	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * @return 2.3以上
	 */
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * @return 3.0 以上
	 */
	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * @return 3.1 以上
	 */
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * @return 4.1 以上
	 */
	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
}
