package com.eip.common.alert.wxchat.domain;

import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.enums.MessageType;
import lombok.Data;

import java.util.List;

/**
 * @类描述：文字类型消息
 */
@Data
public class WxChatText extends AlertBasetDTO {

    @Override
    public MessageType getWxChatType() {
        return MessageType.TEXT;
    }

    /**
     * 内容
     */
    private String content;
    /**
     * userid列表
     */
    private List<String> mentionedList;
    /**
     * 手机号列表列表
     */
    private List<String> mentionedMobileList;
    /**
     * @所有人
     */
    private boolean isAtAll;


}
