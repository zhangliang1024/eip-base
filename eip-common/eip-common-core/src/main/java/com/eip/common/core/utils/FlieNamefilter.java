package com.eip.common.core.utils;

import java.util.regex.Pattern;

/**
 * ClassName: FlieNamefilter
 * Function: 文件路径过滤
 * Date: 2022年04月22 18:28:31
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class FlieNamefilter {

    private static Pattern FilePattern = Pattern.compile("[\\s\\.:?<>|]"); //过滤规则

    public static String filenameFilter(String str) {
        return str == null ? null : FilePattern.matcher(str).replaceAll("");
    }

    public static String fileNameValidate(String str) {
        String strInjectListStr = "../|./|/..| |<|>|:|?";
        if (null != strInjectListStr && !"".equals(strInjectListStr)) {
            str = str.toLowerCase();
            String[] badStrs = strInjectListStr.split("\\|");
            for (int i = 0; i < badStrs.length; i++) {
                if (str.indexOf(badStrs[i]) >= 0) {
                    str = str.replace(badStrs[i], "");
                }
            }
        }
        return str;
    }

    public static void main(String[] args) {
        //String str = "home/..  <>|logs/../:edata?";
        String str = "/home/logs/edata/adfdb";
        String filenameFilter1 = filenameFilter(str);
        String filenameFilter = fileNameValidate(str);
        System.out.println(filenameFilter1);
        System.out.println(filenameFilter);
    }
}
