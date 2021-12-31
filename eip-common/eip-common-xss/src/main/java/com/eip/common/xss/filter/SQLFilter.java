package com.eip.common.xss.filter;

/**
 * ClassName: SQLFilter
 * Function: SQL过滤
 * Date: 2021年12月24 13:44:16
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String filter(String str) {
        String[] values = str.split(" ");

        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|,|like|//|/|%|#";

        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if (values[j].equalsIgnoreCase(badStrs[i])) {
                    values[j] = "forbid";
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i == values.length - 1) {
                sb.append(values[i]);
            } else {
                sb.append(values[i] + " ");
            }
        }

        str = sb.toString();

        return str;
    }

}
