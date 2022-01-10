package com.eip.common.web.apiversion.mappinghandler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: ApiVersionCondition
 * Function: 接口多版本处理核心逻辑
 * Date: 2022年01月10 13:37:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {


    private final double apiVersion;

    public ApiVersionCondition(double apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * Api版本参数
     */
    public static final String API_VERSION_PARAM = "api-version";
    /**
     * 默认版本号(未添加注解使用1.0版本号)
     */
    public static final ApiVersionCondition DEFAULT_API_VERSION = new ApiVersionCondition(1.0);
    /**
     * 路径中版本的前缀， 这里用 /v[1-9]/的形式
     */
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)\\.(\\d+)");

    /**
     * 采用最后定义优先原则，方法上定义版本覆盖类上定义版本
     */
    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        String apiVersion = getRequestApiVersion(request);
        Matcher m = VERSION_PREFIX_PATTERN.matcher(apiVersion + ".999");
        if(m.find()){
            apiVersion = m.group(1) + "." + m.group(2);
            //如果请求版本号大于配置版本号，则满足
            if(Double.parseDouble(apiVersion) >= this.apiVersion){
                return this;
            }
        }
        return null;
    }

    /**
     * 优先匹配最新的版本号
     */
    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        return Double.compare(other.getApiVersion(), this.getApiVersion());
    }

    public double getApiVersion() {
        return apiVersion;
    }

    /**
     * 获取请求版本号
     */
    private String getRequestApiVersion(HttpServletRequest request) {
        String apiVersion = request.getHeader(API_VERSION_PARAM);
        if (StringUtils.isBlank(apiVersion)) {
            apiVersion = request.getParameter(API_VERSION_PARAM);
        }
        //未找到版本号，则默认最低版本号
        if (StringUtils.isEmpty(apiVersion)) {
            apiVersion = "v1.0";
        } else if (!apiVersion.startsWith("v")) {
            apiVersion = "v" + apiVersion;
        }
        return apiVersion;
    }
}
