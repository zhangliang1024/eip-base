package com.eip.sample.sms.web;

import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.sms.email.mail.domain.MailRequest;
import com.eip.common.sms.email.mail.domain.MultiFile;
import com.eip.common.sms.email.mail.service.MailSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: WxBotController
 * Function: 邮件发送
 * Date: 2022年12月28 17:12:40
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@RestController
@RequestMapping("sms/send")
public class MailController {

    @Autowired
    private MailSendService mailSendService;

    @GetMapping("email")
    public ApiResult sendEmail() {
        log.info("send emial begin");
        return this.mailSendService.sendMail(buildEmail());
    }

    @PostMapping("email")
    public ApiResult sendEmail(@RequestBody MailRequest email) {
        log.info("send emial begin");
        return mailSendService.sendMail(email);
    }

    @PostMapping("email/file")
    public ApiResult sendEmailFile(MultipartFile[] files) {
        // HTML 文本
        StringBuilder text = new StringBuilder();
        text.append("<html><head></head>");
        text.append("<body><p>各位老师好：</p></body>");
        text.append("<body><p>本邮件为系统自动发送，如有问题请联系Hello迈迪技术部。</p></body>");
        text.append("<body><p>祝好！</p></body>");
        text.append("</html>");
        MailRequest mail = MailRequest.builder()
                .from("493478xxx@qq.com")
                .to("zhangxxxx1024_job@126.com")
                .subject("附件发送")
                .text(text.toString())
                .html(true)
                .build();
        try {
            List<MultiFile> list = new ArrayList<>();
            for (MultipartFile file : files) {
                MultiFile multiFile = new MultiFile();
                multiFile.setFileName(file.getOriginalFilename());
                multiFile.setInput(file.getBytes());
                multiFile.setFileType(file.getContentType());
                list.add(multiFile);
            }
            MultiFile[] array = list.toArray(new MultiFile[list.size()]);
            mail.setMultiFiles(array);
        } catch (Exception e) {
            throw new RuntimeException("send email mutifile error : " + e.getMessage());
        }
        ApiResult apiResult = mailSendService.sendMail(mail);
        return ApiResult.ok();
    }


    public static MailRequest buildEmail() {
        return MailRequest.builder()
                .from("49347xxxx@qq.com")
                .to("zhangxxxx1024_job@126.com")
                .subject("测试主题")
                .text("test send emial")
                .build();
    }

}
