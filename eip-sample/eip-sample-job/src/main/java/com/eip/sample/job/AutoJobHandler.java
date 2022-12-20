package com.eip.sample.job;

import com.eip.common.job.client.register.anno.XxlAutoRegister;
import com.eip.common.job.core.context.XxlJobHelper;
import com.eip.common.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName: AutoJobHandler
 * Function:
 * Date: 2022年12月20 17:42:06
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class AutoJobHandler {

    @XxlJob(value = "testJob")
    @XxlAutoRegister(cron = "0 0 0 * * ? *", author = "eip-zhliang", jobDesc = "测试job")
    public void testJob() {
        log.info("--------#公众号：码农参上--------");
        XxlJobHelper.log("[auto-job] testJob execute finished");
    }


    @XxlJob(value = "testJob222")
    @XxlAutoRegister(cron = "0/3 * * * * ?", triggerStatus = 1)
    public void testJob2() {
        log.info("--------#作者：Hydra--------");
        XxlJobHelper.log("[auto-job] testJob222 execute finished");
    }

    @XxlJob(value = "testJob444")
    @XxlAutoRegister(cron = "59 59 23 * * ?")
    public void testJob4() {
        log.info("--------hello xxl job--------");
        XxlJobHelper.log("[auto-job] testJob444 execute finished");
    }
}
