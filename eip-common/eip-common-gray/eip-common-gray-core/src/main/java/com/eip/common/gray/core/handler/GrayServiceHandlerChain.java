package com.eip.common.gray.core.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: GrayServiceHandlerChain
 * Function: tag配置责任链
 * Date: 2023年01月04 15:15:47
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class GrayServiceHandlerChain {

    public List<GrayServiceHandler> handlers = new ArrayList<>();

    public void addHandler(GrayServiceHandler handler){
        this.handlers.add(handler);
    }

    /**
     * 支持责任链模式，进行tag配置。默认实现Header模式，从请求头中获取tag
     */
    public void handle(String serviceId){
        for (GrayServiceHandler handler : this.handlers) {
            boolean success = handler.handle(serviceId);
            if(success){
                break;
            }
        }
    }

}
