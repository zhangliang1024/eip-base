package com.eip.common.web.valid.extend;

import com.eip.common.web.valid.annotaion.CustomVaild;

/**
 * ClassName: ValidHandler
 * Function: 拓展校验器接口
 * Date: 2022年01月10 14:23:47
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface ValidHandler<T> {

    boolean handler(CustomVaild vaild,T dadta);
}
