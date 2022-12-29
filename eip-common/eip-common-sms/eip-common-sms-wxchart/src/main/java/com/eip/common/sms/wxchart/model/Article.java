package com.eip.common.sms.wxchart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @类描述：图文类型消息
 * @version：V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {

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