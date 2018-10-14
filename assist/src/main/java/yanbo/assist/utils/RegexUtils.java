package yanbo.assist.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串正则匹配
 *
 * @author Yanbo
 * @date 2018/7/18-下午1:52
 */
public class RegexUtils {

    private static final String REGEX_E_MAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private static final String REGEX_PHONE = "^(1[3456789][0-9])\\d{8}";
    private static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
    private static final String REGEX_ID_NUM = "^\\d{15}|\\d{18}|\\d{17}(\\d|X|x)";

    /**
     * 邮箱格式是否正确
     *
     * @param email String
     * @return boolean
     */
    public static boolean matchEmail(String email) {
        return match(email, REGEX_E_MAIL);
    }

    /**
     * 手机号格式是否正确
     *
     * @param phone String
     * @return boolean
     */
    public static boolean matchPhone(String phone) {
        return match(phone, REGEX_PHONE);
    }

    /**
     * 密码格式是否正确
     *
     * @param psw String
     * @return boolean
     */
    public static boolean matchPassword(String psw) {
        return match(psw, REGEX_PASSWORD);
    }

    /**
     * 身份证号格式是否正确
     *
     * @param id String
     * @return boolean
     */
    public static boolean matchIdNum(String id) {
        return match(id, REGEX_ID_NUM);
    }

    /**
     * 字符串正则匹配
     *
     * @param s     待匹配字符串
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean match(String s, String regex) {
        if (s == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
