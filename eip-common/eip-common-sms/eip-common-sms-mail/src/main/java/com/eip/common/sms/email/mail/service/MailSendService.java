package com.eip.common.sms.email.mail.service;

import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.sms.email.mail.domain.MailConstant;
import com.eip.common.sms.email.mail.domain.MailRequest;
import com.eip.common.sms.email.mail.domain.MultiFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import java.util.Date;


/**
 * ClassName: MailSendService
 * Function: 发送邮件
 * Date: 2021年12月08 09:47:28
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class MailSendService {

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private IMailService mailService;

    /**
     * 发送邮件
     */
    public ApiResult sendMail(MailRequest email) {
        // 1.检测邮件内容信息完整
        ApiResult validateResult = checkMail(email);
        if (ApiResult.isFailure(validateResult)) {
            return validateResult;
        }
        try {
            // 2.发送邮件
            sendMimeMail(email);
            // 3.保存邮件
            return saveMail(email);
        } catch (Exception e) {
            log.error("Send Email Failed:", e);
            Throwable te = e.getCause();
            // 情况1:收件人的邮箱地址语法错误、邮箱账号不存在
            if (te instanceof SendFailedException) {
                SendFailedException fe = (SendFailedException) te;
                if (fe.getMessage() != null && fe.getMessage().startsWith("Invalid Addresses")) {
                    String errdetail = fe.getCause().getMessage() == null ? "" : fe.getCause().getMessage();
                    return ApiResult.error(MailConstant.SendStatus.FAILED_FOREVER, (MailConstant.SendStatus.failForeverMsgPrefix + errdetail).trim());
                }
            }
            // 情况2:收件人的邮箱地址非法
            if (te instanceof AddressException) {
                AddressException ae = (AddressException) te;
                if (ae.getMessage() != null && ae.getMessage().startsWith("Illegal address")) {
                    return ApiResult.error(MailConstant.SendStatus.FAILED_FOREVER, (MailConstant.SendStatus.failForeverMsgPrefix + ae.getMessage()).trim());
                }
            }
            return ApiResult.error(MailConstant.SendStatus.FAILED_RETRY, MailConstant.SendStatus.failRetryMsgPrefix + e.getMessage());
        }
    }

    /**
     * 检测邮件信息
     */
    private ApiResult checkMail(MailRequest email) {
        // 1.1 主题为空不能发送
        if (StringUtils.isEmpty(email.getSubject())) {
            return ApiResult.error(MailConstant.SendStatus.FAILED_FOREVER, MailConstant.SendStatus.failForeverMsgPrefix + "Subject is empty.");
        }
        // 1.2 收件人地址为空不能发送
        if (StringUtils.isEmpty(email.getTo())) {
            return ApiResult.error(MailConstant.SendStatus.FAILED_FOREVER, MailConstant.SendStatus.failForeverMsgPrefix + "Receiver address is empty.");
        }
        // 1.3 邮件内容为空不能发送
        if (StringUtils.isEmpty(email.getText())) {
            return ApiResult.error(MailConstant.SendStatus.FAILED_FOREVER, MailConstant.SendStatus.failForeverMsgPrefix + "Mail text body is empty.");
        }
        // 1.4 收件人地址错误不能发送
        if (email.getTo().trim().indexOf("@") <= 0) {
            return ApiResult.error(MailConstant.SendStatus.FAILED_FOREVER, MailConstant.SendStatus.failForeverMsgPrefix + "Receiver address is illegal.");
        }
        return ApiResult.ok();
    }

    /**
     * 构建复杂邮件信息类
     */
    private void sendMimeMail(MailRequest email) throws MessagingException {
        // 2.1 true表示支持复杂类型
        MimeMessageHelper messageHelper = new MimeMessageHelper(this.mailSender.createMimeMessage(), true, MailConstant.ENCODING_UTF_8);
        // 2.2 邮件主题
        messageHelper.setSubject(email.getSubject());
        // 2.3 邮件发信人
        if (StringUtils.isEmpty(email.getFrom())) {
            email.setFrom(getMailSendFrom());
        } else {
            email.setFrom(email.getFrom());
        }
        messageHelper.setFrom(email.getFrom());

        // 2.4 邮件收信人
        if (email.getTo().contains(";")) {
            String[] tos = email.getTo().replaceAll("\\s", "").split(";");
            messageHelper.setTo(tos);
        } else if (email.getTo().contains(",")) {
            String[] tos = email.getTo().replaceAll("\\s", "").split(",");
            messageHelper.setTo(tos);
        } else {
            messageHelper.setTo(email.getTo());
        }

        // 2.5 设置CC抄送人
        if (!StringUtils.isEmpty(email.getCc())) {
            // 支持以分号分隔
            if (email.getCc().contains(";")) {
                String[] ccs = email.getCc().replaceAll("\\s", "").split(";");
                messageHelper.setCc(ccs);
                // 支持以逗号分隔
            } else if (email.getCc().contains(",")) {
                String[] ccs = email.getCc().replaceAll("\\s", "").split(",");
                messageHelper.setCc(ccs);
            } else {
                messageHelper.setCc(email.getCc());
            }
        }

        // 2.6 设置BCC密送人
        if (!StringUtils.isEmpty(email.getBcc())) {
            // 支持以分号分隔
            if (email.getBcc().contains(";")) {
                String[] ccs = email.getBcc().replaceAll("\\s", "").split(";");
                messageHelper.setBcc(ccs);
                // 支持以逗号分隔
            } else if (email.getBcc().contains(",")) {
                String[] ccs = email.getBcc().replaceAll("\\s", "").split(",");
                messageHelper.setBcc(ccs);
            } else {
                messageHelper.setBcc(email.getBcc());
            }
        }

        // 2.7 邮件内容
        // 启用html
        messageHelper.setText(email.getText(), email.isHtml());

        // 2.8 添加邮件附件
        if (email.getMultiFiles() != null) {
            for (MultiFile multiFile : email.getMultiFiles()) {
                messageHelper.addAttachment(multiFile.getFileName(), new ByteArrayResource(multiFile.getInput()), multiFile.getFileType());
            }
        }

        // 2.9 发送时间
        if (StringUtils.isEmpty(email.getSentDate())) {
            email.setSentDate(new Date());
            messageHelper.setSentDate(email.getSentDate());
        }

        // 2.10 正式发送邮件
        this.mailSender.send(messageHelper.getMimeMessage());
        log.info("[eip-sms-email] - Send Email Success ：{} -> {}", email.getFrom(), email.getTo());
    }

    /**
     * 保存邮件
     */
    private ApiResult saveMail(MailRequest email) {
        // 将邮件保存到数据库
        this.mailService.saveEmail(email);
        log.info("[eip-sms-email] - Save Email Success");
        return ApiResult.ok(MailConstant.SendStatus.successMsgPrefix + "Sender " + getMailSendFrom());
    }

    /**
     * 邮件发信人从配置项读取
     */
    public String getMailSendFrom() {
        return this.mailSender.getJavaMailProperties().getProperty("from");
    }
}
