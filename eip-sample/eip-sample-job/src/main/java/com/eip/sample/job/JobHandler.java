package com.eip.sample.job;

import com.eip.common.job.core.context.XxlJobHelper;
import com.eip.common.job.core.handler.IJobHandler;
import com.eip.common.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobHandler extends IJobHandler {

    @XxlJob(value = "jobHandler")
    public void execute() throws Exception {
        log.info("jobHandler 9006 ProviderApplication start!");
        XxlJobHelper.log("[xxl-job] execute finished。");
    }


}