package com.eip.common.config.apollo.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RsaEncrypUtils
 * Function:
 * Date: 2022年01月12 18:25:25
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class RSAEncrypUtils {

    public static final String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKxbD" +
            "+DruEJD8L0xFe4wZ8D9987T/Q1WQkKAEjYqbasD1aQ5JlaN9wEkrEbQJIOFmaMwxugLM0KfqGuHbsNiS2DnCKOf+TJ" +
            "+pZMQmGExAJITTuD31gMZAiycXDOBs67n6ZFZ8gYOp5uk73oZ" +
            "+XZKOugJ5yl3wyczsXxilpZLkHCdAgMBAAECgYBQYKs4BlXIuWx0noaOrQ5Fx2qgjuYNKTP/VUCo+AJq5X/ldS124f" +
            "/WDac14u2UVGLA4wdvwYFnSZO+ictS3O0vgKoNcxbbK07mLSzIFrQm5k402P3" +
            "+Z1KsryBEkLSVME1K7E3JrZCPlSGb9JQZWsWjgPKyiBU54brz6jc9DLMOFQJBANjUhFVz/IuYJtXfOtz7vy1EhwOY0KiuUPI/UDuh" +
            "/LFgbtfj9mxjq7tHzFzrTrvf2QYwAy1bXDC3FOfjDk7x0WcCQQDLfcoYnwHQoDxXpXr3DM9Cb89vp4mF2oVvDZZQXtuovYaSojwzSbLettJuy+qjMhJm5UgF8N5tinF8xNerZ1dbAkBGZ7sQhDjcmusBRxq15oiNClNMt7IJE5D1F9nVgyOp9MYJE9xQFY0NoqKZCU7ncifl+nENqTymFMyU/wUGXT7BAkAwX1+aSLoquNx/TUQU1EO0nFWoSiBD5HVRt94ijtoSm7MQymYbHwrZLIaLdgf58V40fY6IcwC882sU6MoH4dH/AkAclXAE6kiUm0iraxT/fQtVYKbu5pMyzMGbddsaTo67gOwT5pQd3Q8nVnodOMg3dqBV1PHiMcKgqrwEEOvpxA4O";

    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsWw/g67hCQ/C9MRXuMGfA/ffO0" +
            "/0NVkJCgBI2Km2rA9WkOSZWjfcBJKxG0CSDhZmjMMboCzNCn6hrh27DYktg5wijn/kyfqWTEJhhMQCSE07g99YDGQIsnFwzgbOu5" +
            "+mRWfIGDqebpO96Gfl2SjroCecpd8MnM7F8YpaWS5BwnQIDAQAB";

    /**
     * RSA私钥加密
     *
     * @param str        原文
     * @param privateKey 私钥
     * @return 密文
     * @throws Exception
     */
    public static String encryptByPrivateKey(String str, String privateKey) throws Exception {
        //base64编码的私钥
        byte[] decoded = java.util.Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey =
                (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        String outStr = java.util.Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA公钥解密
     *
     * @param str       密文
     * @param publicKey 公钥
     * @return 明文
     * @throws Exception
     */
    public static String decryptByPublicKey(String str, String publicKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = java.util.Base64.getDecoder().decode(str.getBytes("UTF-8"));
        //base64编码的公钥
        byte[] decoded = java.util.Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey =
                (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }


    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @throws Exception
     */
    public static String encryptByPublicKey(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = java.util.Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey =
                (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = java.util.Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @throws Exception
     */
    public static String decryptByPrivateKey(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = java.util.Base64.getDecoder().decode(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = java.util.Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey =
                (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 随机生成密钥对
     */
    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        Map<String, String> keyMap = new HashMap<String, String>();  //用于封装随机产生的公钥与私钥
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(java.util.Base64.getEncoder().encode(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(java.util.Base64.getEncoder().encode((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put("publicKey", publicKeyString);  //0表示公钥
        keyMap.put("privateKey", privateKeyString);  //1表示私钥
        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(genKeyPair());

        //KeyPair pair = SecureUtil.generateKeyPair("RSA");
        //System.out.println(Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
        //System.out.println(Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()));

        String str = "aaaaa";
        System.out.println(encryptByPublicKey(str, publicKey));

        //String decypt = "UlFJ8+ijiANfLI7MFpjbQqRjR5pnvF0rB9Vap7BMWAOOZh3O7nRBVakEb98YAaCHE1PDub5voo+Q58AksL2Cmj
        // /2VL4LcxgDhOXErApgjsE+ud57niPLgjfw0Xkwd7NfO+nSXS/1Tnh/gG8FeS2n1yG5Mnvt0bcXb/OE/gbBP3U=";
        //System.out.println(decryptByPrivateKey(decypt,privateKey));
    }
}