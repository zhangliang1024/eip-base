package com.eip.common.alert.enums;

import lombok.Getter;

/**
 * @类描述：消息类型
 * @version：V1.0
 */
@Getter
public enum MessageType {

    /**
     * 文本类型
     */
    TEXT("text"),
    /**
     * Markdown
     */
    MARKDOWN("markdown"),
    /**
     * 图片
     */
    IMAGE("image"),
    /**
     * 图片
     */
    PHOTO("photo"),
    /**
     * 文件
     */
    FILE("file"),
    /**
     * 新闻
     */
    NEWS("news")
    ;

    private String type;


    MessageType(String type) {
        this.type = type;
    }
}
