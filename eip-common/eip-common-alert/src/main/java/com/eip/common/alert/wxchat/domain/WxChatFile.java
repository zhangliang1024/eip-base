package com.eip.common.alert.wxchat.domain;

import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.enums.MessageType;
import lombok.Data;
import java.io.File;

/**
 * @类描述：文件类型消息
 */
@Data
public class WxChatFile extends AlertBasetDTO {


    private String mediaId;

    private File file;

    @Override
    public MessageType getWxChatType() {
        return MessageType.FILE;
    }
}
