package adevlibs.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneHelper {
    /**
     * 判断座机格式
     * 
     * @param mobilePhoneNo
     * @return
     */
    public static boolean checkTel(String mobilePhoneNo) {
        boolean result = false;
        try {
            Pattern p = Pattern.compile("^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$");
            Matcher m = p.matcher(mobilePhoneNo);
            result = m.matches();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }	
    
    /**
     * 获取本机号码
     * @param context
     * @return
     */
    public static String loadPhoneNumber(Context context) {
        TelephonyManager phoneMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = phoneMgr.getLine1Number();
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.indexOf("+86") > -1 ? phoneNumber.replace("+86", "") : phoneNumber;
        } else {
            phoneNumber = "";
        }

        return phoneNumber;
    }    

}
