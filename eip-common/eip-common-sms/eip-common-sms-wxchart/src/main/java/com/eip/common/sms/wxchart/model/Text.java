package com.eip.common.sms.wxchart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @类描述：文字类型消息
 * @version：V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Text implements Serializable {

    /**
     * 内容
     */
    private String content;
    /**
     * userid列表
     */
    private List<String> mentioned_list;
    /**
     * 手机号列表列表
     */
    private List<String> mentioned_mobile_list;
    /**
     * @所有人
     */
    private boolean isAtAll;

}
