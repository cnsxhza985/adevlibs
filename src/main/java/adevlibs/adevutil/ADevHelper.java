package adevlibs.adevutil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;

import cnsxhza985.com.adevlibs.R;


/**
 * android开发的常用工具方法
 * 
 * @author wangfan
 * 
 *         <p>
 *         本工具类中覆盖的范围为:
 *         </p>
 *         <ul>
 *         <li>SharedPreference 操作的封装 （方数据 和 取数据）</li>
 *         <li>对话框展示的封装 （单按钮对话框 和 双按钮对话框）</li>
 *         <li>dp和像素sp之间的转换</li>
 *         </ul>
 * 
 */
public class ADevHelper {
	/**
	 * 将数据按照给定的 key 值存入指定的SharedPreference中
	 * 
	 * @param context
	 *            上下文，用来获取SharedPreferences实例
	 * @param spname
	 *            SharedPreferences对应的name
	 * @param key
	 *            保存数据对应的key
	 * @param value
	 *            保存数据对应的值
	 */
	public static void setValueInSharedPreference(Context context,
			String spname, String key, String value) {
		SharedPreferences prefx = context.getSharedPreferences(spname,
				Context.MODE_PRIVATE);
		Editor editor = prefx.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获取SharedPreference 中对应的字段,如果数据没有找到默认会返回null
	 * 
	 * @param context
	 *            上下文，用来获取SharedPreferences实例
	 * @param spname
	 *            SharedPreferences对应的name
	 * @param key
	 *            获取数据对应的key
	 * @return key对应的value，默认返回null
	 */
	public static String getValueInSharedPreference(Context context,
			String spname, String key) {
		String result = null;
		SharedPreferences prefs = context.getSharedPreferences(spname,
				Context.MODE_PRIVATE);

		if (prefs != null) {
			result = prefs.getString(key, null);
		}

		return result;
	}

	/**
	 * 获取SharedPreference 中对应的字段,如果数据没有找到默认会返回指定的默认值
	 * 
	 * @param context
	 *            上下文，用来获取SharedPreferences实例
	 * @param spname
	 *            SharedPreferences对应的name
	 * @param key
	 *            获取数据对应的key
	 * @param defaultvalue
	 *            当按照key没有找到
	 * @return key对应的value,默认返回defaultvalue
	 */
	public static String getValueWithDefault(Context context, String spname,
			String key, String defaultvalue) {
		String result = null;
		SharedPreferences prefx = context.getSharedPreferences(spname,
				Context.MODE_PRIVATE);

		if (prefx != null) {
			result = prefx.getString(key, defaultvalue);
		}

		return result;
	}

	/**
	 * 展示一个对话框,只带确定（确定）
	 * 
	 * @param context
	 *            上下文，用来初始化Builder
	 * @param message
	 *            要在对话框上展示的内容
	 */
	public static void showSingleButtonAlertDialog(Context context,
			String message) {
		Builder builder = new Builder(context);
		builder.setTitle("提示:");
		builder.setMessage("" + message);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 展示一个对话框,带确定和取消2个按钮（确定 取消）
	 * 
	 * @param context
	 *            上下文，用来初始化Builder
	 * @param message
	 *            要在对话框上展示的内容
	 */
	public static void showOkCancelAlertDialog(Context context, String message) {
		Builder builder = new Builder(context);
		builder.setTitle("提示:");
		builder.setMessage("" + message);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 隐藏弹出的软键盘
	 * 
	 * @param context
	 *            上下文，用来后去输入法服务
	 * @param view
	 *            用来获取当前界面的windowtoke。（当前页面所有的View以一棵树的形式Attached to a
	 *            window,所有的view都对应一个WindowToken）view.getWindowToken() 的官方解释为
	 *            Retrieve a unique token identifying the window this view is
	 *            attached to.
	 */
	public final static void hideSoftKeyboard(final Context context,
			final View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm.isActive()) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					InputMethodManager imm = (InputMethodManager) context
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}, 50);
		}
	}

	/**
	 * dp 和 px 之间的转换
	 * 
	 * @param context
	 *            上下文，用来获取像素和DP之间的比率density. density 的含义如下： The logical density
	 *            of the display. This is a scaling factor for the Density
	 *            Independent Pixel unit, where one DIP is one pixel on an
	 *            approximately 160 dpi screen (for example a 240x320, 1.5"x2"
	 *            screen), providing the baseline of the system's display. Thus
	 *            on a 160dpi screen this density value will be 1; on a 120 dpi
	 *            screen it would be .75; etc.
	 * 
	 *            This value does not exactly follow the real screen size (as
	 *            given by xdpi and ydpi, but rather is used to scale the size
	 *            of the overall UI in steps based on gross changes in the
	 *            display dpi. For example, a 240x320 screen will have a density
	 *            of 1 even if its width is 1.8", 1.3", etc. However, if the
	 *            screen resolution is increased to 320x480 but the screen size
	 *            remained 1.5"x2" then the density would be increased (probably
	 *            to 1.5).
	 * @param dp	 适配的时候使用的长度单位
	 * @return	 返回dp对应的像素值
	 */
	public static int dp2px(Context context, int dp) {
		final float fDensity = context.getResources().getDisplayMetrics().density;
		return (int) (dp * fDensity + 0.5f);
	}
}
