package adevlibs.string;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUitls {

    public static final long MINUTE_SECOND = 60;
    public static final long HOUR_SECOND = 60 * 60;
    public static final long DAY_SECOND = 60 * 60 * 24;

    public static boolean checkText(String text) {

        if (text.contains("¿") || text.contains("¦") || text.contains("¡") || text.contains("…") || text.contains("￥")
                || text.contains("—") || text.contains(".") || text.contains(":") || text.contains(";")
                || text.contains("'") || text.contains("€") || text.contains("¥") || text.contains("_")
                || text.contains("（") || text.contains("）") || text.contains("！") || text.contains("？")
                || text.contains("%") || text.contains("&") || text.contains("：") || text.contains("；")
                || text.contains("“") || text.contains("”") || text.contains("——") || text.contains("-")
                || text.contains("+") || text.contains("=") || text.contains("、") || text.contains("*")
                || text.contains("`") || text.contains(".") || text.contains("/") || text.contains("'")
                || text.contains(";") || text.contains("\"") || text.contains("{") || text.contains("}")
                || text.contains("[") || text.contains("]") || text.contains("<") || text.contains(">")
                || text.contains("?") || text.contains("|") || text.contains("!") || text.contains("@")
                || text.contains("#") || text.contains("^") || text.contains("&") || text.contains("*")
                || text.contains("(") || text.contains(")") || text.contains("。") || text.contains("《")
                || text.contains("》") || text.contains("$") || text.contains("”") || text.contains("“")
                || text.contains("\"") || text.contains("×") || text.contains("÷") || text.contains("±")
                || text.contains("﹤") || text.contains("﹥") || text.contains("α") || text.contains("β")
                || text.contains("γ") || text.contains("δ") || text.contains("ε") || text.contains("θ")
                || text.contains("λ") || text.contains("μ") || text.contains("π") || text.contains("τ")
                || text.contains("ο") || text.contains("★") || text.contains("☆") || text.contains("•")
                || text.contains("⊙") || text.contains("㊣") || text.contains(" ") || text.contains("，")
                || text.contains("\n") || text.contains(",")) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * filter illegal characters, only numbers & Letter are allowed 
     * @param str	inputed string
     * @return		numbers & Letter
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }   
    
    /**
     * check if password leagal
     * password should be 0-9 a-z A-z  length should be 6-16
     * @param pwd
     * @return
     */
    public static boolean checkPassword(String pwd) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[0-9a-zA-Z]{6,16}$");
            Matcher m = p.matcher(pwd);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }    

    public static HashMap<String, String> getQuery(String uri) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (uri != null) {
            Pattern pattern = Pattern.compile("(?:[\\?|&]+)(.+?)=([^&?]*)");
            Matcher matcher = pattern.matcher(uri);
            while (matcher != null && matcher.find()) {
                String param = matcher.group(1);
                map.put(param, matcher.group(2));
            }
        }
        return map;
    }

    public static String getDisplayTime(long second) {
        long current = System.currentTimeMillis() / 1000;
        long offset = current - second;
        if (offset <= 0) {
            return "刚刚";
        }
        if (offset > DAY_SECOND) {
            return offset / DAY_SECOND + "天前";
        }
        if (offset > HOUR_SECOND) {
            return offset / HOUR_SECOND + "小时前";
        }
        if (offset > MINUTE_SECOND) {
            return offset / MINUTE_SECOND + "分钟前";
        }
        return offset + "秒前";
    }
    
    /**
     * @param mobiles
     * @return 是否为email
     */
    public static boolean isEmail(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern
                    .compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    
	/**
	 * 截取字符串
	 * 
	 * @param source
	 *            原始字符串
	 * @param chaString
	 *            从哪里开始截取
	 * @return 返回字符串
	 */
	public static String fetchStr(String source, String chaString) {
		// 判断条件
		if (source == null) {
			return null;
		}
		int index = 0;// 开始的索引
		String sumChar = "";// 定义返回的字符串
		boolean bo = false;

		while (index < source.length()) {
			char currentChar = source.charAt(index);// 取得当前的字符
			String ii = "" + currentChar;
			if (ii.equals(chaString)) {
				bo = true;
			}
			if (bo) {
				sumChar += currentChar;
			}
			// 进行累加
			index++;
		}
		return sumChar;

	}    
	
    /**
     * 是否为汉字
     * 
     * @param name
     * @return
     */
    public static boolean checkIsChinses(String name) {
        boolean result = false;
        try {

            Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
            Matcher m = p.matcher(name);
            result = m.matches();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }	
    
    /**
     * 判断是否为只是英文或数字
     * 
     * @param detail
     * @return
     */
    public static boolean checkIsEngANum(String detail) {
        //
        boolean result = false;
        try {

            Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
            Matcher m = p.matcher(detail);
            result = m.matches();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
    
    /**
     * 判断是否是纯数字
     * @param str
     * @return
     */
    public static boolean checkIsNumeric(String str) {
        boolean result = false;
        try {

            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher m = p.matcher(str);
            result = m.matches();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }    
}
