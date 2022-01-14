package com.eip.common.config.apollo.service;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ApolloListenerNamespace
 * Function:
 * Date: 2022年01月11 16:19:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class ApolloDencryptService {

    public static String dencypt(String value, String privateKey) {
        try {
            String decryptValue = decryptByPrivateKey(value, privateKey);
            return decryptValue;
        } catch (Exception e) {
            log.info("[Apollo-dencypt] - error - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA私钥加密
     * @param str 原文
     * @param privateKey 私钥
     * @return 密文
     * @throws Exception
     */
    public static String encryptByPrivateKey( String str, String privateKey ) throws Exception{
        //base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA公钥解密
     * @param str 密文
     * @param publicKey 公钥
     * @return 明文
     * @throws Exception
     */
    public static String decryptByPublicKey(String str, String publicKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str.getBytes("UTF-8"));
        //base64编码的公钥
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }


    /**
     * RSA公钥加密
     * @param str 加密字符串
     * @param publicKey 公钥
     * @throws Exception
     */
    public static String encryptByPublicKey( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     * @param str 加密字符串
     * @param privateKey  私钥
     * @throws Exception
     */
    public static String decryptByPrivateKey(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
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
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.getEncoder().encode((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put("publicKey",publicKeyString);  //0表示公钥
        keyMap.put("privateKey",privateKeyString);  //1表示私钥
        return keyMap;
    }

}
