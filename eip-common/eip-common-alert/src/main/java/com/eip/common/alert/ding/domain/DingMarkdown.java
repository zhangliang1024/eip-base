package com.eip.common.alert.ding.domain;

import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.enums.MessageType;
import lombok.Data;

/**
 * ClassName: DingMarkdown
 * Function:
 * Date: 2021年12月16 10:28:21
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class DingMarkdown extends AlertBasetDTO {

    @Override
    public MessageType getWxChatType() {
        return MessageType.MARKDOWN;
    }

    private String title;

    private String text;

}
