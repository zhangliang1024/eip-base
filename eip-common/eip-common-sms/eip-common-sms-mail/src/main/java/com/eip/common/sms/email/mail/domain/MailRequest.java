package com.eip.common.sms.email.mail.domain;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: MailRequest
 * Function: 邮件发送对象
 * Date: 2021年12月08 09:47:28
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class MailRequest implements Serializable {

    /**
     * 邮件id
     */
    private String id;
    /**
     * 邮件发送人
     */
    private String from;
    /**
     * 邮件接收人（多个邮箱则用逗号","隔开）
     */
    private String to;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String text;
    /**
     * 邮件内容支持html
     */
    private boolean html;
    /**
     * 发送时间
     */
    private Date sentDate;
    /**
     * 抄送（多个邮箱则用逗号","隔开）
     */
    private String cc;
    /**
     * 密送（多个邮箱则用逗号","隔开）
     */
    private String bcc;
    /**
     * 邮件附件
     */
    private MultiFile[] multiFiles;

}
