package com.eip.sample.log.service;

import com.eip.common.log.core.annotation.LogOperation;
import com.eip.common.log.core.context.LogRecordContext;
import com.eip.sample.log.domain.LogDTO;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    /**
     * 测试OperationLog
     */
    //@LogOperation(businessId = "#testClass.testId", businessType = "testType1", logMessage = "#testFunc(#testClass.testId)")
    @LogOperation(businessId = "#log.id", businessType = "businessType",
            logMessage = "'用户将旧值' + #old + '更改为新值' + #log.str",
            operateModule = "一级菜单名称-二级菜单名称-执行操作(根据品类种类查询工序)")
    public LogDTO serviceFunc(LogDTO log, boolean isSuccess) throws Exception {
        LogRecordContext.putVariables("old", "oldValue");
        if (isSuccess) {
            return log;
        } else {
            throw new Exception("testError");
        }
    }
    /**
     * 2022-02-11 17:05:45.182  INFO 66916 --- [nio-8080-exec-1] c.e.a.l.listener.TestCustomLogListener   :
     * TestCustomLogListener 本地接收到日志
     * [
     *   LogOperationDTO(logId=69904dc0-7490-47fd-8d49-87008fc53135, businessId=1, eventType=BUSINESS, businessType=testType3, operateType=OPERATION,
     *   logMessage="用户将旧值oldValue更改为新值str", eventLevel=LOW, operateModule=, ip=null, operator=null, success=true,
     *   result="LogDTO(testId=1, testStr=str, testList=[1, 2, 3])", exception=null, operateDate=Fri Feb 11 17:05:45 CST 2022, executionTime=767)
     * ]
     */

    /**
     * 测试自定义函数
     * @param str
     * @return
     */
    public String serviceFunc2(String str) {
        return "Func:" + str;
    }
}
