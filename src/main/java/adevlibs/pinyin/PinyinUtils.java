package adevlibs.pinyin;

import java.util.ArrayList;
import adevlibs.pinyin.HanziToPinyin.Token;

/**
 * ClassName : PinyinUtils <br>
 * 功能描述： {@link #HanziToPinyin}取自Frameworks<br>
 * History <br>
 * Create User: wangfan <br>
 * Create Date: 2014-1-19 下午3:12:42 <br>
 * Update User: <br>
 * Update Date:
 */
public class PinyinUtils {

    /**
     * @param input
     * @return
     */
    public static char getPinYinAlpha(String input) {
        try {
            ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
            // StringBuilder sb = new StringBuilder();
            if (tokens != null && tokens.size() > 0) {
                Token token = tokens.get(0);
                if (Token.PINYIN == token.type) {
                    return token.target.charAt(0);
                }
                return token.source.charAt(0);
            }
        } catch (Exception e) {
        }
        return '#';
    }

    public static String getPinYin(String input) {
        StringBuilder sb = new StringBuilder();
        try {
            ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
            if (tokens != null && tokens.size() > 0) {
                for (Token token : tokens) {
                    if (Token.PINYIN == token.type) {
                        sb.append(token.target);
                    } else {
                        sb.append(token.source);
                    }
                }
            }
        } catch (Exception e) {
            sb.append(input);
        }
        return sb.toString().toLowerCase();
    }
}

