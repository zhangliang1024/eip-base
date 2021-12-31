package com.eip.common.core.utils;

import java.security.MessageDigest;

/**
 * ClassName: MD5Utils
 * Function: MD5加解密
 * Date: 2021年12月08 11:01:18
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class MD5Utils {


    public static void main(String[] args) {
        System.out.println(MD5Encode("abcd"));
    }

    private static final String hexDigIts[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    /**
     * MD5加密
     *
     * @param origin 字符
     * @return
     */
    public static String MD5Encode(String origin) {
        String resultString = MD5Encode(origin, "utf8");
        return resultString;
    }


    /**
     * MD5加密
     *
     * @param origin      字符
     * @param charsetname 编码
     * @return
     */
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (null == charsetname || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception e) {
        }
        return resultString;
    }


    public static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

}
