package com.eip.common.core.utils.hutool;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Function: HutoolRSAUtils
 * Date: 2022年06月06 10:10:17
 *  RSA之基于hutool的快速实现：https://blog.csdn.net/qq_22260641/article/details/107751389
 *  基于hutool工具类的RSA加解密JAVA实现：https://blog.csdn.net/tttscofield/article/details/122958982
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class HutoolRSAUtils {

    public final static Map<String, String> KEY_MAP = new HashMap(2);
    /**
     * 类型
     */
    public static final String ENCRYPT_TYPE = "RSA";
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 私钥字符串
     */
    private static String privateKey;
    /**
     * 公钥字符串
     */
    private static String publicKey;

    static {
        initKey();
    }

    /**
     * 获取公私钥-请获取一次后保存公私钥使用
     */
    public static void initKey() {
        try {
            KeyPair pair = SecureUtil.generateKeyPair(ENCRYPT_TYPE);
            // 获取 公钥和私钥 的 编码格式（通过该 编码格式 可以反过来 生成公钥和私钥对象）
            byte[] pubEncBytes = pair.getPublic().getEncoded();
            byte[] priEncBytes = pair.getPrivate().getEncoded();

            // 把 公钥和私钥 的 编码格式 转换为 Base64文本 方便保存
            publicKey = new BASE64Encoder().encode(pubEncBytes);
            privateKey = new BASE64Encoder().encode(priEncBytes);

            KEY_MAP.put(PUBLIC_KEY, publicKey);
            KEY_MAP.put(PRIVATE_KEY, privateKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] rsa init error", e);
        }
    }

    /*-----------------------------------公钥加解密-------------------------------------*/

    /**
     * 公钥加密
     *
     * @param content   要加密的内容
     * @param publicKey 公钥
     */
    public static String encryptByPublic(String content, PublicKey publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.encryptBase64(content, KeyType.PublicKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] encryp by public error", e);
        }
        return null;
    }

    /**
     * 公钥解密
     *
     * @param content   要解密的内容
     * @param publicKey 公钥
     */
    public static String decryptByPublic(String content, PublicKey publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.decryptStr(content, KeyType.PublicKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] decrypt by public error", e);
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param content   要加密的内容
     * @param publicKey 公钥
     */
    public static String encryptByPublic(String content, String publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.encryptBase64(content, KeyType.PublicKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] encryp by public error", e);
        }
        return null;
    }

    /**
     * 公钥解密
     *
     * @param content   要解密的内容
     * @param publicKey 公钥
     */
    public static String decryptByPublic(String content, String publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.decryptStr(content, KeyType.PublicKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] decrypt by public error", e);
        }
        return null;
    }


    /*-----------------------------------私钥加解密-------------------------------------*/

    /**
     * 私钥加密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String encryptByPrivate(String content, PrivateKey privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.encryptBase64(content, KeyType.PrivateKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] encrypt by private error", e);
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String decryptByPrivate(String content, PrivateKey privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.decryptStr(content, KeyType.PrivateKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] decrypt by private error", e);
        }
        return null;
    }

    /**
     * 私钥加密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String encryptByPrivate(String content, String privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.encryptBase64(content, KeyType.PrivateKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] encrypt by private error", e);
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String decryptByPrivate(String content, String privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.decryptStr(content, KeyType.PrivateKey);
        } catch (Exception e) {
            log.error("[RSAUTIL] decrypt by private error", e);
        }
        return null;
    }


    public static void main(String[] args) {
        String data = "hello world";

        //公钥加密 - 私钥解密
        String resultOne = encryptByPublic(data, publicKey);
        System.out.println(resultOne);
        System.out.println(decryptByPrivate(resultOne, privateKey));

        //私钥加密 - 公钥解密
        String resultTwo = encryptByPrivate(data, privateKey);
        System.out.println(resultTwo);
        System.out.println(decryptByPublic(resultTwo, publicKey));

    }
}
