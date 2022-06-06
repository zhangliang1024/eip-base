package com.eip.common.core.utils.hutool;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * ClassName: HutoolAESUtils
 * Function:
 * Date: 2022年06月06 10:18:02
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class HutoolDESUtils {

    /**
     * 默认加解密key
     */
    private static final String DEFAULT_SECRET_KEY = "eip-secret-key";

    public static DES des;

    static {
       initKey(DEFAULT_SECRET_KEY);
    }

    public static void initKey(String secretKey){
       if(StringUtils.isNotBlank(secretKey)){
           des = SecureUtil.des(secretKey.getBytes());
       }else {
           //随机生成密钥
           byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
           des = SecureUtil.des(key);
       }
    }
    /*-----------------------------加解密-----------------------*/

    /**
     * 加密
     */
    public static String encrypt(String content) {
        return des.encryptBase64(content);
    }

    /**
     * 解密
     */
    public static String decrypt(String content) {
        return des.decryptStr(content);
    }

    /*-----------------------------加解密为16进制表示-----------------------*/
    public static String encryptHex(String content) {
        return des.encryptHex(content);
    }

    public static String decryptStr(String content) {
        return des.decryptStr(content);
    }

    public static void main(String[] args) {
        String content = "test中文";

        String encrypt = encrypt(content);
        System.out.println(encrypt);
        System.out.println(decrypt(encrypt));

        //加密为16进制表示
        String encryptHex = encryptHex(content);
        System.out.println(encryptHex);
        //解密为原字符串
        System.out.println(decryptStr(encryptHex));

    }


}
