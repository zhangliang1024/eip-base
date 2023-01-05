package com.eip.common.gray.core.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: GrayServiceHandlerChain
 * Function:
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

    public void handle(String serviceId){
        for (GrayServiceHandler handler : this.handlers) {
            boolean success = handler.handle(serviceId);
            if(success){
                break;
            }
        }
    }

}
