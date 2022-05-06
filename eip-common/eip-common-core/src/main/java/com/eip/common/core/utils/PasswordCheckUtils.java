package com.eip.common.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * ClassName: PasswordCheckUtils
 * Function:
 * Date: 2022年04月29 16:40:33
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class PasswordCheckUtils {


    public static final String PW_PATTERN = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";

    public static final int DEFALUT_MAX_ALLOWED = 4;

    public static final String[] WEAK_PASSWORD = {"admin", "picc"};

    public static Boolean checkPassword(String password) {
        return checkPassword(password, DEFALUT_MAX_ALLOWED);

    }

    public static Boolean checkPassword(String password, int maxAllowed) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        if (Arrays.asList(WEAK_PASSWORD).contains(password.toLowerCase())) {
            return false;
        }
        if (!matches(password)) {
            return false;
        }
        return hasValidSequence(password, maxAllowed);

    }

    private static Boolean matches(String password) {
        return password.matches(PW_PATTERN);
    }


    public static Boolean hasValidSequence(String input, int maxAllowed) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        int counter = 0;
        char c = (char) 0;
        for (int i = 0; i < input.length(); i++) {
            if (c == input.charAt(i) - 1) {
                counter++;
            }
            c = input.charAt(i);
            if (counter == maxAllowed) {
                return false;
            }
        }
        char c1 = (char) 0;
        int counter1 = 0;
        for (int i = 0; i < input.length(); i++) {
            if (c1 == input.charAt(i) + 1) {
                counter1++;
            }
            c1 = input.charAt(i);
            if (counter1 == maxAllowed) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        String pw1 = "ABCDEFGHIG";
        String pw2 = "abcdefghig";
        String pw3 = "0123456789";
        String pw4 = "!@#$%^&*()";
        String pw5 = "ABCDEabcde";
        String pw6 = "ABCDE01234";
        String pw7 = "ABCDE!@#$%";
        String pw8 = "abcde01234";
        String pw9 = "abcde!@#$%";
        String pw10 = "01234!@#$%";
        String pw11 = "abcde01234!@#$%";
        String pw12 = "ABCDE01234!@#$%";
        String pw13 = "ABCDEabcde!@#$%";
        String pw14 = "ABCDEabcde01234";
        String pw15 = "Aa0!";
        //符合要求密码
        String pw16="ABCedba014!@#";
        String pw17="ABCabd421!@#";
        String pw18="ACbde012!@#";
        String pw19="ACbec01245!@#";



        System.out.println(checkPassword(pw1));
        System.out.println(checkPassword(pw2));
        System.out.println(checkPassword(pw3));
        System.out.println(checkPassword(pw4));
        System.out.println(checkPassword(pw5));
        System.out.println(checkPassword(pw6));
        System.out.println(checkPassword(pw7));
        System.out.println(checkPassword(pw8));
        System.out.println(checkPassword(pw9));
        System.out.println(checkPassword(pw10));
        System.out.println(checkPassword(pw11));
        System.out.println(checkPassword(pw12));
        System.out.println(checkPassword(pw13));
        System.out.println(checkPassword(pw14));
        System.out.println(checkPassword(pw15));
        System.out.println(checkPassword(pw16));
        System.out.println(checkPassword(pw17));
        System.out.println(checkPassword(pw18));
        System.out.println(checkPassword(pw19));

    }
}
