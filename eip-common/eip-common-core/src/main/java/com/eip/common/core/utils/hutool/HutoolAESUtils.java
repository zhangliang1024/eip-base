package com.eip.common.core.utils.hutool;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * ClassName: HutoolAESUtils
 * Function:
 * Date: 2022年06月06 10:18:02
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class HutoolAESUtils {

    /**
     * 默认加解密key
     * 支持：16、24、32 位的加密key，否则报错
     */
    private static final String DEFAULT_SECRET_KEY = "eipbasesecretkey";

    public static AES aes;

    static {
        initKey(DEFAULT_SECRET_KEY);
    }

    public static void initKey(String secretKey){
        if(StringUtils.isNotBlank(secretKey)){
            aes = SecureUtil.aes(secretKey.getBytes());
        }else {
            //随机生成密钥
            byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
            aes = SecureUtil.aes(key);
        }
    }
    /*-----------------------------加解密-----------------------*/

    /**
     * 加密
     */
    public static String encrypt(String content) {
        return aes.encryptBase64(content);
    }

    /**
     * 解密
     */
    public static String decrypt(String content) {
        return aes.decryptStr(content);
    }

    /*-----------------------------加解密为16进制表示-----------------------*/
    public static String encryptHex(String content) {
        return aes.encryptHex(content);
    }

    public static String decryptStr(String content) {
        return aes.decryptStr(content);
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
