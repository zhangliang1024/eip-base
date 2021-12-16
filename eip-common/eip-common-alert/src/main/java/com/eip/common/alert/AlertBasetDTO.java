package com.eip.common.alert;

import com.eip.common.alert.enums.MessageType;
import lombok.Data;

/**
 * ClassName: AlertBasetDTO
 * Function:
 * Date: 2021年12月16 11:41:00
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public abstract class AlertBasetDTO {

    public abstract MessageType getWxChatType();

}
