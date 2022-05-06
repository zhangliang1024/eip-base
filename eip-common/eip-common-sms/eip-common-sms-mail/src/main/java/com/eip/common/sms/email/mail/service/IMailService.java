package com.eip.common.sms.email.mail.service;

import com.eip.common.sms.email.mail.domain.MailRequest;

/**
 * ClassName: IMailService
 * Function: 保存邮件
 * Date: 2021年12月08 09:47:28
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface IMailService {

    void saveEmail(MailRequest email);

}
