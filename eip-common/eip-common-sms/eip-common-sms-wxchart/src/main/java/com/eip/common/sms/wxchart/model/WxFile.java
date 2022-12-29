package com.eip.common.sms.wxchart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @类描述：文件类型消息
 * @version：V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxFile {

    private String media_id;

    private java.io.File file;

}
