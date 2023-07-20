package com.eip.cloud.sc.feign;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * ClassName: SampleRetryer
 * Function:
 * Date: 2023年07月07 10:56:27
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class SampleRetryer implements Retryer {


    @Override
    public void continueOrPropagate(RetryableException e) {
        log.error("Retryer throw : {}",e);
    }

    @Override
    public Retryer clone() {
        Retryer retryer = new Retryer.Default(100, SECONDS.toMillis(1), 5);
        return retryer;
    }
}
