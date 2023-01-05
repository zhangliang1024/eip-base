package com.eip.common.gray.zuul;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.eip.common.gray.core.handler.GrayServiceHandler;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * ClassName: RequestHeaderGrayServiceHandler
 * Function:
 * Date: 2023年01月04 15:20:50
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class RequestHeaderGrayServiceHandler extends GrayServiceHandler {

    public static final String RIBBON_LANCHER_MAP = "ribbon-lancher-map";

    @Override
    protected String getGrayVersion(String serviceId) {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String header = request.getHeader(RIBBON_LANCHER_MAP);
        if (StringUtils.isNotBlank(header)) {
            JSONObject jsonObject = JSONUtil.parseObj(header);
            if (Objects.nonNull(jsonObject)) {
                String grayVersion = jsonObject.getStr(serviceId);
                if (StringUtils.isNotBlank(grayVersion)) {
                    return grayVersion;
                }
            }
        }
        return null;
    }
}
