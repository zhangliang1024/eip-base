package com.eip.common.core.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.jwt.Claims;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: JwtTokenHandler
 * Function:
 * Date: 2022年02月15 13:12:31
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class JwtTokenHandler {

    /** jwt签名算法 RSA256 */
    public static final String JWT_SIGN_KEY = SignAlgorithm.SHA256withRSA.getValue();
    /** jwt有效期 */
    public static final long JWT_EXPIRE_TIME = 60 * 60 * 24;
    /** RSA结果*/
    public static final Map<String, KeyPair> SIGN_KEY = new HashMap<>();
    /** JwtSigner结果*/
    public static final Map<String, JWTSigner> JWT_SIGNER = new HashMap<>();


    static {
        KeyPair keyPair = KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm(JWT_SIGN_KEY));
        JWTSigner singer = JWTSignerUtil.createSigner(JWT_SIGN_KEY, keyPair);
        SIGN_KEY.put(JWT_SIGN_KEY, keyPair);
        JWT_SIGNER.put(JWT_SIGN_KEY, singer);
    }


    public static String getPublicKey(){
        KeyPair keyPair = SIGN_KEY.get(JWT_SIGN_KEY);
        return Base64.encodeBase64String(keyPair.getPublic().getEncoded());
    }

    public static Claims parseToken(String token) {
        final JWT jwt = JWTUtil.parseToken(token);
        JWTPayload payload = jwt.getPayload();
        return payload;
    }

    public static Object parseToken(String token, String key) {
        Claims payload = parseToken(token);
        return payload.getClaim(key);
    }

    public static boolean verify(String token) {
        final JWTSigner signer = JWT_SIGNER.get(JWT_SIGN_KEY);
        return  JWT.of(token)
                .setSigner(signer)
                .validate(JWT_EXPIRE_TIME);
    }

    public static String getJwtToken(Map<String, Object> payload) {
        final JWTSigner signer = JWT_SIGNER.get(JWT_SIGN_KEY);
        JWT.create()
                .addPayloads(payload)
                .setSigner(signer)
                .setIssuedAt(DateUtil.date())  //设置签发时间
                .sign();

        return JWTUtil.createToken(payload, signer);
    }


    public static void main(String[] args) {
        System.out.println("publicKey = " + getPublicKey());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", Integer.parseInt("123"));
        map.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);

        String token = getJwtToken(map);
        //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjEyMywiZXhwaXJlX3RpbWUiOjE2NDYyMDE2NDUyMTV9.Bz1dpZ0KbrcFxwwAnl-ZTxJlK8dbRF_CgKTB2CV9xSrbKrXgZa-j7X_-qYAQZpoWpbe46T6HP836XtSWNNQw-94r9asFgzL2Dum1mBSnNbUeie3Ht19Nx5BoqBzC4Rt8f8MrVyKUQjvXO_LH8CDltlAj5--WalJ4VufwZ23eXlQ";
        System.out.println("token = " + token);
        System.out.println("verify result = " + verify(token));
        System.out.println("parse token uid = " + parseToken(token, "uid"));
    }
}
