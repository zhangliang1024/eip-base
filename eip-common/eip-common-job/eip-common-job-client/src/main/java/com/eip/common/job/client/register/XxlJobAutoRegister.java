package com.eip.common.job.client.register;

import com.eip.common.job.client.register.anno.XxlAutoRegister;
import com.eip.common.job.client.register.model.XxlJobGroup;
import com.eip.common.job.client.register.model.XxlJobInfo;
import com.eip.common.job.client.register.service.JobGroupService;
import com.eip.common.job.client.register.service.JobInfoService;
import com.eip.common.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class XxlJobAutoRegister implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Autowired
    private JobGroupService jobGroupService;
    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 注册执行器
        addJobGroup();
        // 注册任务
        addJobInfo();
    }

    /**
     * 自动注册执行器
     */
    private void addJobGroup() {
        if (jobGroupService.preciselyCheck()) {
            return;
        }
        if (jobGroupService.autoRegisterGroup()) {
            log.info("eip-xxl-job auto register xxl-job group success!");
        } else {
            throw new RuntimeException("eip-xxl-job auto register xxl-job group filed");
        }
    }

    private void addJobInfo() {
        List<XxlJobGroup> jobGroups = jobGroupService.getJobGroup();
        XxlJobGroup xxlJobGroup = jobGroups.get(0);

        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            Map<Method, XxlJob> annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(), new MethodIntrospector.MetadataLookup<XxlJob>() {
                @Override
                public XxlJob inspect(Method method) {
                    return AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class);
                }
            });

            for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                XxlJob xxlJob = methodXxlJobEntry.getValue();

                // 自动注册
                if (executeMethod.isAnnotationPresent(XxlAutoRegister.class)) {
                    XxlAutoRegister xxlAutoRegister = executeMethod.getAnnotation(XxlAutoRegister.class);
                    List<XxlJobInfo> jobInfo = jobInfoService.getJobInfo(xxlJobGroup.getId(), xxlJob.value());
                    if (!jobInfo.isEmpty()) {
                        // 因为是模糊查询，需要再判断一次
                        Optional<XxlJobInfo> first = jobInfo.stream().filter(xxlJobInfo -> xxlJobInfo.getExecutorHandler().equals(xxlJob.value())).findFirst();
                        if (first.isPresent()) continue;
                    }

                    XxlJobInfo xxlJobInfo = createXxlJobInfo(xxlJobGroup, xxlJob, xxlAutoRegister);
                    Integer jobInfoId = jobInfoService.addJobInfo(xxlJobInfo);
                }
            }
        }
    }

    // 设置job状态默认开启
    private XxlJobInfo createXxlJobInfo(XxlJobGroup xxlJobGroup, XxlJob xxlJob, XxlAutoRegister xxlAutoRegister) {
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobGroup(xxlJobGroup.getId());
        xxlJobInfo.setJobDesc(xxlAutoRegister.jobDesc());
        xxlJobInfo.setAuthor(xxlAutoRegister.author());
        xxlJobInfo.setScheduleType("CRON");
        xxlJobInfo.setScheduleConf(xxlAutoRegister.cron());
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setExecutorHandler(xxlJob.value());
        xxlJobInfo.setExecutorRouteStrategy(xxlAutoRegister.executorRouteStrategy());
        xxlJobInfo.setMisfireStrategy("DO_NOTHING");
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(0);
        xxlJobInfo.setExecutorFailRetryCount(0);
        xxlJobInfo.setGlueRemark("GLUE代码初始化");
        xxlJobInfo.setTriggerStatus(xxlAutoRegister.triggerStatus());
        return xxlJobInfo;
    }
}