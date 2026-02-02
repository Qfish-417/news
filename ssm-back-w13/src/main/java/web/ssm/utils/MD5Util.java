package web.ssm.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * MD5加密
     * @param str 要加密的字符串
     * @return 加密后的MD5字符串
     */
    public static String encrypt(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : byteDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证字符串与MD5是否匹配
     * @param str 原始字符串
     * @param md5 MD5值
     * @return 是否匹配
     */
    public static boolean verify(String str, String md5) {
        String encrypted = encrypt(str);
        return encrypted != null && encrypted.equals(md5);
    }
}