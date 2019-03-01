package adevlibs.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressHelper {
	/**
	 * 判断字符串是否是合格的邮编
	 * @param postCode
	 * @return true 代表合格邮编 false 代表不是邮编
	 */
    public static boolean checkPostCode(String postCode) {
        boolean result = false;
        try {

            Pattern p = Pattern.compile("[0-9]\\d{5}(?!\\d)");
            Matcher m = p.matcher(postCode);
            result = m.matches();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
