package com.eip.common.sms.wxchart.common;

/**
 * ClassName: MsgType
 * Function: 消息类型
 * Date: 2022年12月28 15:25:15
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum MsgType {

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
     * 文件
     */
    FILE("file"),
    /**
     * 新闻
     */
    NEWS("news");

    private String typeName;

    public String getTypeName() {
        return this.typeName;
    }

    MsgType(String typeName) {
        this.typeName = typeName;
    }
}
