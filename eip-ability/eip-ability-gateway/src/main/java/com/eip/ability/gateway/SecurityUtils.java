package com.eip.ability.gateway;

import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eip.ability.gateway.domain.UserInfoDetails;
import com.google.common.base.Throwables;
import com.sun.xml.internal.ws.api.model.CheckedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Levin
 */
public class SecurityUtils {

    public static OAuth2Authentication getAuthentication() {
        return (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户详细信息(只适用于 authority 模块)
     *
     * @return 结果
     */
    public static UserInfoDetails getAuthInfo() {
        OAuth2Authentication authentication = getAuthentication();
        if (authentication == null || anonymous()) {
            throw new RuntimeException("认证信息不存在");
        }
        Authentication userAuthentication = authentication.getUserAuthentication();
        if (userAuthentication.getPrincipal() instanceof UserInfoDetails) {
            return (UserInfoDetails) userAuthentication.getPrincipal();
        }
        String detailsText = JSON.toJSONString(userAuthentication.getDetails());
        final JSONObject detailJson = JSON.parseObject(detailsText);
        return detailJson.getObject(AUTH_DETAILS_PRINCIPAL, UserInfoDetails.class);
    }

    public static final String AUTH_DETAILS_PRINCIPAL = "principal";
    public static final String ANONYMOUS_USER = "anonymousUser";

    /**
     * 是否为匿名用户
     *
     * @return 是（true）|不是（false）
     */
    public static boolean anonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return true;
        }
        if (authentication.getPrincipal() == null || authentication instanceof UsernamePasswordAuthenticationToken) {
            return true;
        }
        return authentication.getPrincipal().equals(ANONYMOUS_USER);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     token
     * @param publicKey base64加密公钥
     * @return /
     */
    public static Claims getJwtFromToken(String token, String publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        if (claimsJws != null) {
            Claims body = claimsJws.getBody();
            return body;


        }
        return null;
    }

    /**
     * 获取token中的用户信息
     *
     * @param token token
     * @return /
     */
    public static Object getUserId(String token) {
        Jws<Claims> claimsJws = parserToken(token, pubkey);
        if (claimsJws != null) {
            Claims body = claimsJws.getBody();
            Object userId =  body.get("user_id");
            return userId;
        }
        return null;
    }

    /**
     * 公钥解析token
     *
     * @param token     token
     * @param publicKey base64加密公钥
     * @return /
     */
    private static Jws<Claims> parserToken(String token, String publicKey) {
        try {
            return Jwts.parser().setSigningKey(getRSAPublicKey(publicKey)).parseClaimsJws(token);
        } catch (Exception e) {
            // 抛出自定义异常
        }
        return null;
    }

    public static final String pubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqI/1ah3MdXUeSxKSlYqalvk9Kdns8K8cqKDaJ5qz0RCJw+CAT032iaGL1PuK/hIbNaarLBzB0SZPxp5v1UE/ERnLfYiq" +
            "+yFlTfp3FniWKEjdWby1vvvWJYtYNb+zuk07gVLOzONAX/ZdOaZVtwUm/+lEU5wcHk6GnSvM/q8KYJJ/2VXGkuXw7sQDok1deVuDsN7DBbXhfahYAdWupC22fy5RRPm8ea8oOxif" +
            "/eZxzkgWiccoBgFExdiOZMTXg3y8LxDl2c23FRXYegT8ZmxANyjvuVDIchTmkqbMoa6SkRVL8B3WesEpB35Ag8g6h71CMT6S2mUMgFBlU7JgSIM5SQIDAQAB";

    /**
     * 获取 RSAPublicKey
     *
     * @param pubKey base64加密公钥
     */
    private static RSAPublicKey getRSAPublicKey(String pubKey) throws NoSuchAlgorithmException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKey));
        RSAPublicKey publicKey = null;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        try {
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
        }
        return publicKey;
    }


    public static void main(String[] args) {
        String pubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqI/1ah3MdXUeSxKSlYqalvk9Kdns8K8cqKDaJ5qz0RCJw+CAT032iaGL1PuK/hIbNaarLBzB0SZPxp5v1UE/ERnLfYiq+yFlTfp3FniWKEjdWby1vvvWJYtYNb" +
                "+zuk07gVLOzONAX/ZdOaZVtwUm/+lEU5wcHk6GnSvM/q8KYJJ/2VXGkuXw7sQDok1deVuDsN7DBbXhfahYAdWupC22fy5RRPm8ea8oOxif" +
                "/eZxzkgWiccoBgFExdiOZMTXg3y8LxDl2c23FRXYegT8ZmxANyjvuVDIchTmkqbMoa6SkRVL8B3WesEpB35Ag8g6h71CMT6S2mUMgFBlU7JgSIM5SQIDAQAB";
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyX2lkIjoxLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInNlcnZlciJdLCJleHAiOjE2NjQ1Mjc4MjAsImF1dGhvcml0aWVzIjpbInN5c3RlbTpnYXRld2F5OnZpZXciLCJyb2xlOm1hbmFnZW1lbnQ6ZGlzdHJpYnV0aW9uX3JlcyIsImRldmVsb3BtZW50OmdhdGV3YXk6bGltaXQiLCJyb2xlOm1hbmFnZW1lbnQiLCJyb2xlOm1hbmFnZW1lbnQ6ZWRpdCIsIm1lc3NhZ2U6bXkiLCJkaWN0Om1hbmFnZW1lbnQ6YWRkIiwiYmFzaWM6dmlldyIsImRldmVsb3BtZW50OmdhdGV3YXk6cm91dGUiLCJyZXNvdXJjZTp2aWV3IiwiYXJlYTp2aWV3IiwiYXBwbGljYXRpb246bWFuYWdlbWVudCIsImRpY3Q6bWFuYWdlbWVudDplZGl0IiwiZGljdDptYW5hZ2VtZW50OnJlbW92ZSIsIm1lbnU6dmlldyIsImRpY3Q6bWFuYWdlbWVudCIsIm1lc3NhZ2U6cHVibGlzaCIsInN0YXRpb246bWFuYWdlbWVudCIsIlBMQVRGT1JNX0FETUlOIiwibmVweGlvbjpwbGF0Zm9ybSIsInVzZXI6bWFuYWdlbWVudDphZGQiLCJsb2c6dmlldyIsInN5c3RlbTp2aWV3IiwidGVuYW50OnZpZXciLCJ1c2VyOmluZm86dmlldyIsImRldmVsb3BtZW50OmdlbmVyYXRlOmNvZGUiLCJzZXJ2aWNlOmdvdmVybmFuY2U6bmFjb3M6dmlldyIsInVzZXI6bWFuYWdlbWVudCIsIm9yZzp2aWV3IiwibG9nOm9wdCIsImxvZzpsb2dpbiIsInJvbGU6bWFuYWdlbWVudDpkaXN0cmlidXRpb25fdXNlciIsImRldmVsb3BtZW50OnZpZXciLCJyb2xlOm1hbmFnZW1lbnQ6cmVtb3ZlIiwiZGV2ZWxvcG1lbnQ6Z2F0ZXdheTpibGFja2xpc3QiLCJ1c2VyOm1hbmFnZW1lbnQ6ZWRpdCIsInN0YXRpb246bWFuYWdlbWVudDpyZW1vdmUiLCJzdGF0aW9uOm1hbmFnZW1lbnQ6ZWRpdCIsInRlbmFudDptYW5hZ2VtZW50IiwicmVzb3VyY2U6ZmlsZSIsInVzZXI6bWFuYWdlbWVudDpyZW1vdmUiLCJhdXRoOnZpZXciLCJyb2xlOm1hbmFnZW1lbnQ6YWRkIiwic3RhdGlvbjptYW5hZ2VtZW50OmFkZCJdLCJqdGkiOiI2Mjk0ZjY2Yy1lY2M1LTQ1ODItODAxMS01NTBjZjdmYzk4NmYiLCJjbGllbnRfaWQiOiJjbGllbnQiLCJ0ZW5hbnRfY29kZSI6IjAwMDAifQ.EcOt3JHWD-YHk8VQmW8pSNXz1HmM_7gnzQ-H6jXvgbcZ9k7OOC_lWrTKUuDdS7BIbScstxl3KTCMPfbJ-7widBMTLpmBTPUzPGYz4ABZGWndzVs5UD94tc31aj4fUzxR9EDMokqvoXX1-siqogrrE7FiDiVU9gtrMZAw2XM3gBf39-77ZjLIPTlWrro_sP83XnRqBi3Hj8qLXcdV0NN6lH5ibLSMzKNQ3x3s7m-ap9jYmXTNE_hdb_8I_JVPbOHhd604ZRvJ35HBl7JOfnlEvwhwrBhn0cmcqIq7mvNO2No7pJ5ZDZZMJPAgzCQcQ2in4TOH8qG7jF9-vtCnO06aag";
        System.out.println(getJwtFromToken(token, pubkey));
    }
}
