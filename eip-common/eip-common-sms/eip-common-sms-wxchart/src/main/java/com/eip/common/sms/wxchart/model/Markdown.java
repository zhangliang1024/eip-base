package com.eip.common.sms.wxchart.model;

import com.eip.common.sms.wxchart.common.MarkdownConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @类描述：markdown类型消息
 * @version：V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Markdown implements Serializable {

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
