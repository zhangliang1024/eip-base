package com.eip.common.alert.wxchat.domain;

import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.enums.MessageType;
import lombok.Data;

/**
 * @类描述：markdown类型消息
 */
@Data
public class WxChatMarkdown extends AlertBasetDTO {

    @Override
    public MessageType getWxChatType() {
        return MessageType.MARKDOWN;
    }

    /**
     * 内容
     */
    private String content;

    public String contentConverter(MarkdownConverter converter){
        StringBuffer content = new StringBuffer();
        for (String item : converter.getItems()) {
            content.append(item + "\n");
        }
        return content.toString();
    }
}
