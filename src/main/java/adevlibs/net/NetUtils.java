package adevlibs.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * net related methods
 * @author wangfan
 *
 */
public class NetUtils {
    // *********************网络状况相关方法*********************
    /**
     * Check if current network is ready
     * 
     * @return
     */
    public static boolean checkNetworkState(Context context) {
        NetworkInfo info = getNetworkInfo(context);

        return (info != null && info.isConnected());
    }

    /**
     * Check if WIFI is enabled
     * 
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isConnected()) {
            return info.getTypeName().equals("WIFI");
        } else {
            return false;
        }
    }

    /**
     * 是否是手机网络
     * 
     * @return
     */
    public static boolean isMobileNet(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if in airplane mode
     * 
     * @return
     */
    public static boolean isAirplaneMode(Context context) {
        return (android.provider.Settings.System.getInt(context.getContentResolver(),
                android.provider.Settings.System.AIRPLANE_MODE_ON, 0) != 0);
    }

    /**
     * Get current network information
     * 
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        NetworkInfo info = null;
        try {
            ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connMan.getActiveNetworkInfo();
        } catch (Throwable t) {
        }

        return info;
    }
}
