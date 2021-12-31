package com.eip.common.metrics;

import com.eip.common.metrics.ctx.ServletContext;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: AspectAop
 * Function:
 * Date: 2021年12月17 15:21:33
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */

@Slf4j
@Aspect
@Component
public class MetricsAspectHandler {

    @Autowired
    private MeterRegistry registry;

    private static final Map<String, Counter> COUNTER_IOC = new ConcurrentHashMap<>();

    private static final Map<String, Timer> TIMER_IOC = new ConcurrentHashMap<>();

    private final ThreadLocal<String> REQUEST_URL = new ThreadLocal<>();

    private final ThreadLocal<Long> BEGIN_TIME = new ThreadLocal<>();

    //@Pointcut("execution(public * com.eip.*.*.controller.*.*(..)) " + "&& @annotation(Metrics)")
    @Pointcut("@annotation(com.eip.common.metrics.Metrics)")
    private void pointCut() {
    }

    @PostConstruct
    public void init() {

    }

    @Before("pointCut()")
    public void doBefore() {
        HttpServletRequest request = ServletContext.request();
        String ipAddr = getRemoteHost(request);
        String requestUrl = request.getRequestURL().toString();

        Timer timer = TIMER_IOC.get(requestUrl);
        if(Objects.isNull(timer)){
            timer = Timer.builder("app_requests_cost_" + requestUrl)
                    .description("app reqeust cost time").tag("reqeust_const","monitor").register(registry);
            TIMER_IOC.put(requestUrl,timer);
        }

        Counter counter = COUNTER_IOC.get(requestUrl);
        if (Objects.isNull(counter)) {
            counter = registry.counter("app_requests_" + requestUrl, "counter", "monitor");
            COUNTER_IOC.put(requestUrl, counter);
        }
        REQUEST_URL.set(requestUrl);
        BEGIN_TIME.set(System.currentTimeMillis());
    }

    @AfterReturning(pointcut = "pointCut()")
    public void doAftereReturning() {
        Long beginTime = BEGIN_TIME.get();
        long cost = System.currentTimeMillis() - beginTime;
        log.info("请求执行时间：" + cost);
        String requestUrl = REQUEST_URL.get();
        Counter counter = COUNTER_IOC.get(requestUrl);
        counter.increment();
        Timer timer = TIMER_IOC.get(requestUrl);
        timer.record(cost,TimeUnit.MILLISECONDS);
    }

    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");

        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
