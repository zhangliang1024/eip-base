package com.eip.common.sms.email.mail.domain;

import lombok.Data;

/**
 * ClassName: MultiFile
 * Function: 文件传输
 * Date: 2021年12月08 09:47:28
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class MultiFile {

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件字节数组
     */
    private byte[] input;

}
