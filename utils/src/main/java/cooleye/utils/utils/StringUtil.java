package cooleye.utils.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenayan-x230s on 7/14/14.
 */
public class StringUtil {

    public static boolean isMobileNum(CharSequence phoneNumber) {
        Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean isPhoneNumber(CharSequence phoneNumber) {
        Pattern p = Pattern.compile("\\d{3}-{0,1}\\d{8}|\\d{4}-{0,1}\\d{7,8}");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean isEmail(CharSequence email) {
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPasswordValid(CharSequence password) {
        Pattern p = Pattern.compile("^\\w{6,20}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isCardIdValid(CharSequence cardID) {
        Pattern p = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher m = p.matcher(cardID);
        return m.matches();
    }

    public static String isNull(String s) {
        return isNull(s, "");
    }

    public static String isNull(String s, String format) {
        return TextUtils.isEmpty(s) ? format : s;
    }

    public static boolean hasChinese(String str) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isUrl(CharSequence urlString) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(urlString);
        return matcher.matches();
    }

    public static boolean isBarCode(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_\\*\\-]$");
        return pattern.matcher(str).matches();
    }

    public static String formatBarCode(String barcode) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < barcode.length(); i++) {
            String c = barcode.substring(i, i + 1);
            if (StringUtil.isBarCode(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String ifBlank(String str, String def) {
        return isBlank(str) ? str : def;
    }

    public static boolean isBlank(String str) {
        return str == null || TextUtils.isEmpty(str.trim());
    }

    public static boolean equal(String str1, String str2) {
        return str1 == str2
                || (str1 != null && str1.equals(str2))
                || (str2 != null && str2.equals(str1));
    }

    /**
     * @param str include number or other char
     * @return remain number only
     * @throws Exception
     */
    public static int filterNorNumber(String str) throws Exception {
        try {
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            //替换与模式匹配的所有字符（即非数字的字符将被""替换）
            Integer integer = Integer.valueOf(m.replaceAll("").trim());
            return integer;
        } catch (Exception e) {
            throw new NumberFormatException("number format exception");
        }
    }
    public static String formatToMoney(String prefix,String format,double value){
        DecimalFormat dFormat = new DecimalFormat(format);
        String formated = "";
        try {
            format = dFormat.format(value);
        } catch (Exception e) {
        }
        return prefix+formated;
    }
}
