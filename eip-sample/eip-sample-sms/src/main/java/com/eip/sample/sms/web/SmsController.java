package com.eip.sample.sms.web;

import com.eip.common.sms.phone.component.AliyunSmsHelper;
import com.eip.common.sms.phone.vo.SmsMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: Sms
 * Function: SmsController
 * Date: 2022年12月29 14:46:46
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
@RequestMapping("sms/phone")
public class SmsController {

    @Autowired
    private AliyunSmsHelper aliyunSmsHelper;

    public void send() {
        SmsMsgVO sms = new SmsMsgVO();
        // 建议使用唯一流水号，便于后续查询
        sms.setSerialId(String.valueOf(System.currentTimeMillis()));
        // 如有多个号码，依次添加
        sms.addMobile("13895279527");

        // 设置短信签名（可在短信控制台中找到）
        sms.setSignName("某某APP");

        // 设置短信模板编号（可在短信控制台中找到）
        sms.setTemplateCode("SMS_12345678");

        // 例如短信模板为： 尊敬的${name}，您的当前可用余额为${balance}元
        // 设置以下变量
        Map<String, String> smsVars = new HashMap<>();
        smsVars.put("name", "张三");
        smsVars.put("balance", "99.50");
        sms.setVars(smsVars);
        boolean result = aliyunSmsHelper.send(sms);

        // 后续处理逻辑... ...

    }

}
