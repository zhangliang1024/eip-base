package com.eip.common.web.advice;

import com.eip.common.core.core.annotation.ExceptionCode;
import com.eip.common.core.core.assertion.enums.ArgumentResponseEnum;
import com.eip.common.core.core.assertion.enums.BaseResponseEnum;
import com.eip.common.core.core.assertion.enums.ServletResponseEnum;
import com.eip.common.core.core.exception.BaseRuntimeException;
import com.eip.common.core.core.exception.BusinessRuntimeException;
import com.eip.common.core.core.exception.i18n.I18nMessageSource;
import com.eip.common.core.core.protocol.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.Field;

/**
 * 全局异常处理
 */
@Slf4j
@ResponseBody
@ControllerAdvice
@ConditionalOnWebApplication
@ConditionalOnMissingBean(GlobalExceptionHandler.class)
public class GlobalExceptionHandler {

    /**
     * 生产环境
     */
    private final static String ENV_PROD = "prod";

    @Autowired
    private I18nMessageSource i18nMessageSource;

    @Value("${spring.profiles.active:dev}")
    private String profile;

    /**
     * 获取国际化消息
     */
    public String getI18nMessage(BaseRuntimeException e) {
        String code = "response." + e.getResponseEnum().toString();
        String message = i18nMessageSource.getMessage(code, e.getArgs());
        if (message == null || message.isEmpty()) {
            return e.getMessage();
        }
        return message;
    }

    /**
     * 捕获业务异常
     */
    @ExceptionHandler(value = BusinessRuntimeException.class)
    public ApiResult handleBusinessException(BaseRuntimeException e) {
        log.error("[Global] Business Exception : {}", e.getMessage(), e);
        return ApiResult.error(e.getResponseEnum().getCode(), getI18nMessage(e));
    }

    @ExceptionHandler(value = BaseRuntimeException.class)
    public ApiResult handleBaseException(BaseRuntimeException e) {
        log.error("[Global] Base Exception : {}", e.getMessage(), e);
        return ApiResult.error(e.getResponseEnum().getCode(), getI18nMessage(e));
    }

    /**
     * 未到达Controller层的相关异常
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public ApiResult handleServletException(Exception e) {
        log.error("[Global] Handle exception : {}", e.getMessage(), e);
        String code = BaseResponseEnum.SERVER_ERROR.getCode();
        try {
            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
            code = servletExceptionEnum.getCode();
        } catch (IllegalArgumentException e1) {
            log.error("[Global] Illegal Exception : class [{}] not defined in enums {}", e.getClass().getName(), ServletResponseEnum.class.getName());
        }
        if (ENV_PROD.equals(profile)) {
            // 为生产环境时, 不适合把具体的异常信息展示给用户, 比如404.
            code = BaseResponseEnum.SERVER_ERROR.getCode();
            BaseRuntimeException baseRuntimeException = new BaseRuntimeException(BaseResponseEnum.SERVER_ERROR);
            String message = getI18nMessage(baseRuntimeException);
            return ApiResult.error(code, message);
        }
        return ApiResult.error(code, e.getMessage());
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(value = BindException.class)
    public ApiResult handleBindException(BindException e) {
        log.error("[Global] Parameter binding exception : {}", e.getMessage(), e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验(Valid)异常，将校验失败的所有异常组合成一条错误信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult handleValidException(MethodArgumentNotValidException e) throws NoSuchFieldException {
        // 从异常对象中拿到错误信息
        String defaultErrorMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        Class<?> parameterType = e.getParameter().getParameterType();
        String fieldName = e.getBindingResult().getFieldError().getField();
        Field field = parameterType.getDeclaredField(fieldName);
        ExceptionCode annotation = field.getAnnotation(ExceptionCode.class);
        // 有注解的话就返回注解的响应信息
        if (annotation != null) {
            String errorMsg = annotation.message();
            if (StringUtils.isBlank(errorMsg)) {
                errorMsg = defaultErrorMsg;
            }
            return ApiResult.error(annotation.value(), errorMsg);
        }
        log.error("[Global] Parameter binding exception : {}", e);
        return wrapperBindingResult(e.getBindingResult());
    }


    private ApiResult wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
        }
        return ApiResult.error(ArgumentResponseEnum.VALID_ERROR.getCode(), msg.substring(2));
    }


    /**
     * 捕获任意异常
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult handleException(Exception e) {
        log.error("[Global] Exception : {}", e.getMessage(), e);
        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
            String code = BaseResponseEnum.SERVER_ERROR.getCode();
            BaseRuntimeException baseRuntimeException = new BaseRuntimeException(BaseResponseEnum.SERVER_ERROR);
            String message = getI18nMessage(baseRuntimeException);
            return ApiResult.error(code, message);
        }
        return ApiResult.error(BaseResponseEnum.SERVER_ERROR.getCode(), e.getMessage());
    }

}
