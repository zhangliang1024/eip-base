package com.eip.common.alert.ding.domain;

import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.enums.MessageType;
import lombok.Data;

/**
 * ClassName: DingText
 * Function:
 * Date: 2021年12月16 10:25:05
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class DingImage extends AlertBasetDTO {

    @Override
    public MessageType getWxChatType() {
        return MessageType.PHOTO;
    }

    private String photoURL;
}
