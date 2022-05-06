package com.eip.common.sms.email.mail.service.Impl;

import com.eip.common.sms.email.mail.domain.MailRequest;
import com.eip.common.sms.email.mail.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: MultiFile
 * Function: 保存邮件
 * Date: 2021年12月08 09:47:28
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service
public class MailServiceImpl implements IMailService {


    @Override
    public void saveEmail(MailRequest email) {
        log.debug("save emial to the database , need to do it yourself");
    }
}
