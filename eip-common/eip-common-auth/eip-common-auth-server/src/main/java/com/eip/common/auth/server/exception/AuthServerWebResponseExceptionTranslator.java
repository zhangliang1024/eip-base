package com.eip.common.auth.server.exception;

import com.eip.common.auth.com.exception.AuthResponseEnum;
import com.eip.common.core.core.protocol.response.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * ClassName: AuthServerWebResponseExceptionTranslator
 * Function: 自定义异常翻译器，针对用户名、密码异常，授权类型不支持的异常进行处理
 * Date: 2022年01月18 11:30:56
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AuthServerWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    /**
     * 业务处理方法，重写这个方法返回客户端信息
     */
    @Override
    public ResponseEntity<ApiResult> translate(Exception e){
        ApiResult result = doTranslateHandler(e);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 根据异常定制返回信息
     * TODO 自己根据业务封装
     */
    private ApiResult doTranslateHandler(Exception e) {
        //初始值，系统错误，
        AuthResponseEnum resultEnum = AuthResponseEnum.UNAUTHORIZED;
        //判断异常，不支持的认证方式
        if(e instanceof UnsupportedGrantTypeException){
            resultEnum = AuthResponseEnum.UNSUPPORTED_GRANT_TYPE;
            //用户名或密码异常
        }else if(e instanceof InvalidGrantException){
            resultEnum = AuthResponseEnum.USERNAME_OR_PASSWORD_ERROR;
        }
        return new ApiResult(resultEnum);
    }


}
