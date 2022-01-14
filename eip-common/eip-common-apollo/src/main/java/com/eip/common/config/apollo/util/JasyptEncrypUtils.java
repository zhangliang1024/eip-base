package com.eip.common.config.apollo.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * ClassName: JasyptEncrypUtils
 * Function:
 * Date: 2022年01月11 13:30:25
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class JasyptEncrypUtils {

    /**
     * 加密
     *
     * @param password 配置文件中设定的加密盐值
     * @param value    要加密的字符串
     */
    public static String encyptPwd(String password, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(password));
        String result = encryptor.encrypt(value);
        return result;
    }

    /**
     * 解密
     *
     * @param password 配置文件中设定的加密盐值
     * @param value    解密字符串
     */
    public static String decyptPwd(String password, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(password));
        String result = encryptor.decrypt(value);
        return result;
    }

    public static SimpleStringPBEConfig cryptor(String password) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    public static void main(String[] args) {
        // 加密
        String encPwd = encyptPwd("yinjihaunkey", "i coming home abcd");
        System.out.println(encPwd);

        // 解密
        String decPwd = decyptPwd("yinjihaunkey", encPwd);
        System.out.println(decPwd);
    }
}
