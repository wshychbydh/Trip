package cooleye.service.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by lenayan on 7/21/14.
 */
public class MD5 {

    /**
     * 获取MD5 结果字符串
     *
     * @param source
     * @return
     */
    public static String encode(byte[] source) {
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public final static String decode(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param file
     * @return 16位md5
     */
    public static String decode(File file) {
        FileInputStream fis = null;
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            fis = new FileInputStream(file);

            byte[] buffer = new byte[2048];

            int length = -1;

            long s = System.currentTimeMillis();

            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);

            }
            byte[] b = md.digest();
            // 16位加密
            return byteToHexString(b);
            // 32位加密
            // return byteToHexString(b);

        } catch (Exception ex) {

            ex.printStackTrace();

            return null;

        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            }

        }

    }
    /** */

    /**
     * 把byte[]数组转换成十六进制字符串表示形式
     *
     * @param tmp 要转换的byte[]
     * @return 十六进制字符串表示形式
     */

    private static String byteToHexString(byte[] tmp) {
        char hexdigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',

                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String s;

        // 用字节表示就是 16 个字节

        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，

        // 所以表示成 16 进制需要 32 个字符

        int k = 0; // 表示转换结果中对应的字符位置

        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节

            // 转换成 16 进制字符的转换

            byte byte0 = tmp[i]; // 取第 i 个字节

            str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,

            // >>> 为逻辑右移，将符号位一起右移

            str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换

        }
        s = new String(str); // 换后的结果转换为字符串
        return s;

    }

}
