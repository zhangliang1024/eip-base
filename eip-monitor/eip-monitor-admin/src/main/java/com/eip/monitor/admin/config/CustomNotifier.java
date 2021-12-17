package com.eip.monitor.admin.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.eip.common.alert.AlertHandlerFactory;
import com.eip.common.alert.AlertRequest;
import com.eip.common.alert.SendAlertHandler;
import com.eip.common.alert.config.prop.AlertProperties;
import com.eip.common.alert.ding.domain.DingMarkdown;
import com.eip.common.alert.ding.template.DingAlertTemplate;
import com.eip.common.alert.enums.AlertTypeEnum;
import com.eip.common.alert.wxchat.domain.WxChatMarkdown;
import com.eip.common.alert.wxchat.template.WxChatAlertTemplate;
import com.eip.common.sms.mail.domain.MailRequest;
import com.eip.common.sms.mail.service.MailSendService;
import com.google.common.base.Joiner;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: CustomNotifier
 * Function: 自定义通知
 * Date: 2021年12月15 09:32:56
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class CustomNotifier extends AbstractEventNotifier {

    //消息模板
    private static final String template = "服务名称 : %s(%s) \r\n服务状态:%s (%s) \r\n服务地址:%s";

    public static final String DEFAULT_TITLE = "服务上下线变更通知";


    @Autowired(required = false)
    private AlertHandlerFactory handlerFactory;
    @Autowired(required = false)
    private MailSendService mailSendService;
    @Value("${spring.mail.to:xxx@126.com}")
    private String to;
    @Value("${eip.mail.enabled:true}")
    private boolean enabled;
    @Value("${spring.profiles.active:default}")
    private String env;

    public CustomNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            String message = Strings.EMPTY;
            if (event instanceof InstanceStatusChangedEvent) {
                InstanceId instanceId = event.getInstance();
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                String instanceName = instance.getRegistration().getName();
                String serviceUrl = instance.getRegistration().getServiceUrl();
                log.info("Instance {} ({}) is {}", instanceName, instanceId, status);
                switch (status) {
                    case "DOWN":
                        message = "健康检查没通过";
                        status = status + " (健康检查失败)";
                        break;
                    case "OFFLINE":
                        message = "服务离线";
                        status = status + " (服务离线)";
                        break;
                    case "UP":
                        message = "服务上线";
                        status = status + " (服务上线)";
                        break;
                    case "UNKNOWN":
                        message = "服务未知状态";
                        status = status + " (服务未知状态)";
                        break;
                    default:
                        break;
                }
                message = String.format(template, instanceName, instanceId, status, message, serviceUrl);
                this.sendMessage(instanceName, status, instanceId, serviceUrl);
            } else {
                log.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(),event.getType());
            }

        });
    }

    /**
     * Function: 发送消息
     * <p>
     * Date: 2021/12/16 17:43
     *
     * @author 张良
     * @param:
     * @return:
     */
    private void sendMessage(String instanceName, String status, InstanceId instanceId, String serviceUrl) {
        log.info("[monitor-admin] send health check message ！");
        AlertProperties alertProp = handlerFactory.getAlertProperties();
        List<String> listReceive = StrUtil.split(alertProp.getReceives(), ',');
        String receives = Joiner.on(", @").join(listReceive);

        String name = alertProp.getType().name();
        AlertTypeEnum type = AlertTypeEnum.valueOf(name);
        switch (type) {
            case DING:
                String message = tmplate(DingAlertTemplate.DING_SERVER_NOTIFY_TXT, instanceName, status, instanceId, serviceUrl, receives);
                if (alertProp.getDing().isEnabled()) {
                    sendDing(message, alertProp);
                }
                if (enabled) {
                    message = emailTmplate(DingAlertTemplate.EMAIL_SERVER_NOTIFY_TXT, instanceName, status, instanceId, serviceUrl, serviceUrl, receives);
                    sendEmail(message, instanceName);
                }
                break;
            case WXCHAT:
                message = tmplate(WxChatAlertTemplate.WX_CHART_SERVER_NOTIFY_TXT, instanceName, status, instanceId, serviceUrl, receives);
                if (alertProp.getDing().isEnabled()) {
                    sendWxChat(message, alertProp);
                }
                if (enabled) {
                    message = emailTmplate(WxChatAlertTemplate.EMAIL_SERVER_NOTIFY_TXT, instanceName, status, instanceId, serviceUrl, serviceUrl, receives);
                    sendEmail(message, instanceName);
                }
                break;
            case LARK:
                break;
            default:
                break;
        }
    }

    private String tmplate(String template, String instanceName, String status, InstanceId instanceId, String serviceUrl, String receives) {
        String message = String.format(template,
                env.toUpperCase(),
                instanceName,
                instanceId,
                status,
                serviceUrl,
                receives,
                DateUtil.now());
        return message;
    }

    private String emailTmplate(String template, String instanceName, String status, InstanceId instanceId, String hrefUrl, String serviceUrl, String receives) {
        String message = String.format(template,
                env.toUpperCase(),
                instanceName,
                instanceId,
                status,
                hrefUrl,
                serviceUrl,
                receives,
                DateUtil.now());
        return message;
    }

    private void sendDing(String message, AlertProperties alertProp) {
        AlertRequest request = new AlertRequest();
        DingMarkdown markdown = new DingMarkdown();
        markdown.setText(message);
        markdown.setTitle(DEFAULT_TITLE);

        request.setBasetDTO(markdown);
        request.setSecretKey(alertProp.getDing().getSecretKey());
        request.setType(alertProp.getType().name());
        SendAlertHandler messageHandler = handlerFactory.getSendHandler(alertProp.getType().name());
        messageHandler.sendAlertMessage(request);

    }

    private void sendWxChat(String message, AlertProperties alertProp) {
        AlertRequest request = new AlertRequest();
        WxChatMarkdown wxChatMarkdown = new WxChatMarkdown();
        wxChatMarkdown.setContent(message);

        request.setBasetDTO(wxChatMarkdown);
        request.setSecretKey(alertProp.getWxChat().getSecretKey());
        request.setType(alertProp.getType().name());
        SendAlertHandler messageHandler = handlerFactory.getSendHandler(alertProp.getType().name());
        messageHandler.sendAlertMessage(request);
    }

    private void sendEmail(String message, String instanceName) {
        MailRequest mail = new MailRequest();
        mail.setSubject(DEFAULT_TITLE + "-" + instanceName);
        mail.setText(message);
        mail.setTo(to);
        mail.setHtml(true);
        mailSendService.sendMail(mail);
    }

}
