package adevlibs.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Display;

import cnsxhza985.com.adevlibs.R;

/**
 * 针对android APP的一些操作
 * 
 * @author wangfan
 * 
 *         <p>
 *         APKHelper覆盖的范围为:
 *         </p>
 *         <ul>
 *         <li>本地APK文件的安装</li>
 *         <li>根据包名 卸载现有的APP</li>
 *         <li>APP 快捷图标（ShortCut）操作（创建 和 删除）</li>
 *         <li>Activity对应的Window大小</li>
 *         </ul>
 * 
 */
public class AppHelper {
	/**
	 * 根据apk路径安装APK文件
	 * 
	 * @param context
	 *            上下文，用于广播安装意图
	 * @param apkFilePath
	 *            apk文件的路径
	 */
	public static void installApkFile(Context context, String apkFilePath) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkFilePath),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 根据包名 卸载现有的APP
	 * 
	 * @param context
	 *            上下文，用于广播卸载意图
	 * @param packageName
	 *            待卸载的APP的packageName
	 */
	public static void deletApp(Context context, String packageName) {
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}
	
	/**
	 * 创建程序的快捷方式,APP必须包含程序名称 “R.string.app_name” 以及 程序图标
	 * “R.drawable.ic_launcher”
	 * 
	 * @param context
	 *            上下文，用来获取APP名称以及资源图标并将创建ShortCut的意图广播出去
	 */
	public static void creatAppShortCut(Context context) {
		Intent shortcutIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		// 是否可以有多个快捷方式的副本，参数如果是true就可以生成多个快捷方式，如果是false就不会重复添加
		shortcutIntent.putExtra("duplicate", false);
		Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mainIntent.setClass(context, context.getClass());
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, mainIntent);
		Parcelable mParcelable = Intent.ShortcutIconResource.fromContext(
				context, R.drawable.ic_launcher); // 获取快捷键的图标
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				mParcelable);// 快捷方式的图标
		context.sendBroadcast(shortcutIntent);
	}

	/**
	 * 删除程序的快捷方式，APP必须包含程序名称 “R.string.app_name”
	 * 
	 * @param context
	 *            上下文，用来获取APP名称并将删除ShortCut的意图广播出去
	 */
	public static void deleteAppShortCut(Activity context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory("android.intent.category.LAUNCHER");
		mainIntent.setClass(context, context.getClass());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, mainIntent);
		context.sendBroadcast(shortcut);
	}	
	
	/**
	 * 获取当前APP的版本信息，默认为 null
	 * @param context 上下文，用于获取Package服务
	 * @return	packageInfo中的versionName
	 */
    public static String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String version = null;
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
        }
        return version;
    }
    
    /**
     * 获取当前Activity对应的window大小
     * @param activity
     * @return
     */
	public static int[] getWindowSize(Activity activity) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		Display display = activity.getWindowManager().getDefaultDisplay();
		display.getMetrics(outMetrics);
		int[] size = { outMetrics.widthPixels, outMetrics.heightPixels };
		return size;
	}    
}
