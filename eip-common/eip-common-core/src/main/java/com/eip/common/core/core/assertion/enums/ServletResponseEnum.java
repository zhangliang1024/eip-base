package com.eip.common.core.core.assertion.enums;

//import javax.servlet.http.HttpServletResponse;

public enum ServletResponseEnum {

    //METHODARGUMENTNOTVALIDEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "MethodArgumentNotValidException"),
    //METHODARGUMENTTYPEMISMATCHEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "MethodArgumentTypeMismatchException"),
    //MISSINGSERVLETREQUESTPARTEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "MissingServletRequestPartException"),
    //MISSINGPATHVARIABLEEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "MissingPathVariableException"),
    //BINDEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "BindException"),
    //MISSINGSERVLETREQUESTPARAMETEREXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "MissingServletRequestParameterException"),
    //TYPEMISMATCHEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "TypeMismatchException"),
    //SERVLETREQUESTBINDINGEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "ServletRequestBindingException"),
    //HTTPMESSAGENOTREADABLEEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "HttpMessageNotReadableException"),
    //NOHANDLERFOUNDEXCEPTION(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "NoHandlerFoundException"),
    //NOSUCHREQUESTHANDLINGMETHODEXCEPTION(String.valueOf(HttpServletResponse.SC_NOT_FOUND), "NoSuchRequestHandlingMethodException"),
    //HTTPREQUESTMETHODNOTSUPPORTEDEXCEPTION(String.valueOf(HttpServletResponse.SC_METHOD_NOT_ALLOWED), "HttpRequestMethodNotSupportedException"),
    //HTTPMEDIATYPENOTACCEPTABLEEXCEPTION(String.valueOf(HttpServletResponse.SC_NOT_ACCEPTABLE), "HttpMediaTypeNotAcceptableException"),
    //HTTPMEDIATYPENOTSUPPORTEDEXCEPTION(String.valueOf(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE), "HttpMediaTypeNotSupportedException"),
    //CONVERSIONNOTSUPPORTEDEXCEPTION(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),"ConversionNotSupportedException" ),
    //HTTPMESSAGENOTWRITABLEEXCEPTION(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), "HttpMessageNotWritableException"),
    //ASYNCREQUESTTIMEOUTEXCEPTION(String.valueOf(HttpServletResponse.SC_SERVICE_UNAVAILABLE), "AsyncRequestTimeoutException")
    ;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回信息，直接读取异常的message
     */
    private String message;


    ServletResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
