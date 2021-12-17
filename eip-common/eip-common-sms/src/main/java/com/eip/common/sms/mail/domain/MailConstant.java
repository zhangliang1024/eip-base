package com.eip.common.sms.mail.domain;

/**
 * ClassName: MailConstant
 * Function: sms配置
 * Date: 2021年12月08 09:47:28
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class MailConstant {

    /**
     * 邮件发送状态
     **/
    public final class SendStatus {
        /**
         * 邮件发送状态: 待发送
         **/
        public static final String WAIT = "101";
        /**
         * 邮件发送状态: 发送中
         **/
        public static final String SENDING = "102";
        /**
         * 邮件发送状态: 发送成功
         **/
        public static final String SUCCESS = "200";
        /**
         * 邮件发送状态: 发送失败,稍后重试发送
         **/
        public static final String FAILED_RETRY = "201";
        /**
         * 邮件发送状态: 发送失败,不再重试发送
         **/
        public static final String FAILED_FOREVER = "202";
        /**
         * 邮件发送状态: 成功前缀
         **/
        public static final String successMsgPrefix = "[Success] : ";
        /**
         * 邮件发送状态: 失败重试前缀
         **/
        public static final String failRetryMsgPrefix = "[FailRetry] : ";
        /**
         * 邮件发送状态: 失败不再重试前缀
         **/
        public static final String failForeverMsgPrefix = "[FailForever] : ";
    }

    /**
     * 邮件发送分页
     **/
    public final static int pageSize = 100;
    /**
     * Ringbuffer缓冲区大小
     **/
    public final static int buffSize = 512; //必须是2的倍数
    /**
     * 编码字符集
     */
    public static final String ENCODING_UTF_8 = "UTF-8";
    /**
     * 流传输格式
     */
    public static final String CONTENT_TYPE_STREA = "application/vnd.ms-excel;charset=UTF-8";
}
