package com.eip.common.alert.wxchat.domain;

import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.enums.MessageType;
import lombok.Data;

/**
 * @类描述：图文类型消息
 */
@Data
public class WxChatNews extends AlertBasetDTO {

    @Override
    public MessageType getWxChatType() {
        return MessageType.NEWS;
    }

    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 链接
     */
    private String url;
    /**
     * 图片链接
     */
    private String picurl;

}