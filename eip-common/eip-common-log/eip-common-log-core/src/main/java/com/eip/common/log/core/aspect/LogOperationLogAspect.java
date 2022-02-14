package com.eip.common.log.core.aspect;

import com.eip.common.core.auth.AuthUserContext;
import com.eip.common.core.auth.AuthUserDetail;
import com.eip.common.log.core.context.LogRecordContext;
import com.eip.common.log.core.exception.LogOperateEnum;
import com.eip.common.log.core.function.LogFunctionRegistrar;
import com.eip.common.core.log.LogOperationDTO;
import com.eip.common.log.core.service.LogService;
import com.eip.common.log.core.service.NativeLogListener;
import com.eip.common.core.utils.JacksonUtil;
import com.eip.common.log.core.annotation.LogOperation;
import com.eip.common.web.utils.HttpServletContext;
import com.eip.common.web.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * ClassName: OperationLogAspect
 * Function:
 * Date: 2022年02月11 13:08:39
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Aspect
public class LogOperationLogAspect {

    @Value("${spring.application.name:defaultApplication}")
    private String applicationName;

    @Autowired(required = false)
    private LogService logService;

    @Autowired(required = false)
    private NativeLogListener logListener;

    private final SpelExpressionParser parser = new SpelExpressionParser();

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(com.eip.common.log.core.annotation.LogOperation)")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object result;
        StopWatch watch = new StopWatch();
        LogOperationDTO operation = null;
        try {
            watch.start();
            result = point.proceed();
            operation = resolveExpress(point, result);
        } catch (Throwable e) {
            //方法执行异常，自定义上线文未写入；但是任然需要初始化其它变量
            operation = resolveExpress(point, null);
            operation.setSuccess(false);
            operation.setException(e.getMessage());
            throw e;
        } finally {
            watch.stop();
            //记录执行时间
            operation.setExecutionTime(watch.getTotalTimeMillis());

            //发送本地监听
            if (logListener != null) {
                logListener.createLog(operation);
            }
            //发送数据管道
            if (logService != null) {
                logService.createLog(operation);
            }
            // 清除变量上下文
            LogRecordContext.clearContext();
        }
        return result;
    }

    private LogOperationDTO resolveExpress(ProceedingJoinPoint point, Object result) {
        //获取请求参数和请求方法
        Object[] arguments = point.getArgs();
        Method method = getMethod(point);
        LogOperation logOperation = method.getAnnotation(LogOperation.class);

        //记录日志
        LogOperationDTO operation = new LogOperationDTO();

        //日志级别
        operation.setEventLevel(logOperation.level().getValue());
        //事件类型
        operation.setEventType(logOperation.eventType().getValue());
        //操作类型
        operation.setOperateType(logOperation.operateType().getValue());

        //业务类型
        String businessType = logOperation.businessType();
        validate(businessType, LogOperateEnum.BUSINESS_TYPE_EMPTY);
        operation.setBusinessType(businessType);

        //操作模块
        String operateModule = logOperation.operateModule();
        validate(operateModule, LogOperateEnum.OPERATE_MODULE_EMPTY);
        operation.setOperateModule(operateModule);

        //子系统
        operation.setOperateSubsystem(applicationName);

        //获取表达式
        String businessIdSpel = logOperation.businessId();
        String logMessageSpel = logOperation.logMessage();

        //方法参数
        String[] params = discoverer.getParameterNames(method);
        StandardEvaluationContext context = LogRecordContext.getContext();
        //注册自定义函数
        LogFunctionRegistrar.register(context);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                context.setVariable(params[i], arguments[i]);
            }
        }
        //businessId 处理
        if (StringUtils.isNotBlank(businessIdSpel)) {
            Expression expression = parser.parseExpression(businessIdSpel);
            String businessId = expression.getValue(context, String.class);
            operation.setBusinessId(businessId);
        }
        //logMessage 处理
        if (StringUtils.isNotBlank(logMessageSpel)) {
            Expression expression = parser.parseExpression(logMessageSpel);
            Object logMessageObj = expression.getValue(context, Object.class);
            String logMessage = JacksonUtil.objectToStr(logMessageObj);
            operation.setLogMessage(logMessage);
        }

        operation.setLogId(UUID.randomUUID().toString());
        operation.setSuccess(true);
        operation.setOperateDate(new Date());
        operation.setResult(JacksonUtil.objectToStr(result));

        //当前操作人员
        AuthUserDetail authUser = AuthUserContext.get();
        operation.setOperator(authUser.getUserName());

        HttpServletRequest request = HttpServletContext.getRequest();
        if (!Objects.isNull(request)) {
            //接口地址
            String requestURI = request.getRequestURI();
            operation.setRequestURI(requestURI);

            //请求IP
            String ipAddr = IPUtil.getIpAddr(request);
            operation.setIp(ipAddr);
        } else {
            log.warn("[operation-log] - httpRequest is null ...");
        }
        return operation;
    }

    private Method getMethod(ProceedingJoinPoint point) {
        Method method = null;
        try {
            Signature signature = point.getSignature();
            MethodSignature ms = (MethodSignature) signature;
            Object target = point.getTarget();
            method = target.getClass().getMethod(ms.getName(), ms.getParameterTypes());
        } catch (Exception e) {
            log.error("[operation-log] aspect get methdo error - {}", e);
        }
        return method;
    }

    private void validate(Object object, LogOperateEnum operateEnum) {
        operateEnum.assertNotNull(object);
        if (object instanceof String) {
            String regex = "['\"]";
            Pattern pa = Pattern.compile(regex);
            Matcher ma = pa.matcher((String) object);
            if (ma.find()) {
                throw new IllegalArgumentException("包含非法字符'" + ma.group(0) + "'");
            }
        }
    }

}
