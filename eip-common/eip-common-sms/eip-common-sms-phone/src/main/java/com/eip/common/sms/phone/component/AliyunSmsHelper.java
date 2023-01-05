package com.eip.common.sms.phone.component;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.eip.common.sms.phone.config.AliyunSmsProperties;
import com.eip.common.sms.phone.vo.SmsMsgVO;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * 阿里云短信辅助工具
 *
 * @author yinzl
 */
public class AliyunSmsHelper {
    private static final Logger logger = LoggerFactory.getLogger(AliyunSmsHelper.class);

    /**
     * 短信API产品名称
     */
    private static final String product = "Dysmsapi";

    /**
     * 短信API产品域名
     */
    private static final String domain = "dysmsapi.aliyuncs.com";

    /**
     * 客户端
     */
    private IAcsClient client;

    private final AliyunSmsProperties aliyunSmsProperties;

    private boolean initSuccess;

    public AliyunSmsHelper(AliyunSmsProperties aliyunSmsProperties) {
        this.aliyunSmsProperties = aliyunSmsProperties;
    }

    /**
     * 初始化
     */
    public void init() {
        if (initSuccess) {
            return;
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(aliyunSmsProperties.getAccessKeyId()), "accessKeyId不能为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(aliyunSmsProperties.getAccessKeySecret()), "accessKeySecret不能为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(aliyunSmsProperties.getRegionId()), "regionId不能为空");
        try {
            if (client == null) {
                logger.info("初始化阿里大鱼短信网关");
                IClientProfile profile = DefaultProfile.getProfile(aliyunSmsProperties.getRegionId(), aliyunSmsProperties.getAccessKeyId(), aliyunSmsProperties.getAccessKeySecret());
                try {
                    DefaultProfile.addEndpoint(aliyunSmsProperties.getRegionId(), aliyunSmsProperties.getRegionId(), product, domain);
                } catch (ClientException e) {
                    e.printStackTrace();
                }
                client = new DefaultAcsClient(profile);
                logger.info("初始化阿里大鱼短信网关成功");
            }
            initSuccess = true;
        } catch (Exception e) {
            logger.error("阿里云SMS初始化失败", e);
        }
    }

    /**
     * 发送
     *
     * @param smsMsg 短信参数
     * @return 是否成功
     */
    public boolean send(SmsMsgVO smsMsg) {
        Preconditions.checkArgument(smsMsg != null, "参数为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(smsMsg.getSerialId()), "流水号为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(smsMsg.getTemplateCode()), "短信模板为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(smsMsg.getSignName()), "短信签名为空");
        Preconditions.checkArgument(!CollectionUtils.isEmpty(smsMsg.getMobiles()), "手机号为空");
        Preconditions.checkArgument(smsMsg.getMobiles().size() <= 1000, "手机号不超过1000个");
        Preconditions.checkArgument(smsMsg.getVars() != null, "变量为空");
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(Joiner.on(',').join(smsMsg.getMobiles()));
        request.setSignName(smsMsg.getSignName());
        request.setTemplateCode(smsMsg.getTemplateCode());
        request.setTemplateParam(JSONUtil.toJsonStr(smsMsg.getVars()));
        request.setOutId(smsMsg.getSerialId());
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = client.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && "OK".equalsIgnoreCase(sendSmsResponse.getCode())) {
                // 请求成功
                if (!smsMsg.getSerialId().equals(sendSmsResponse.getBizId())) {
                    logger.error("回执流水号校验未通过! 传入的流水号={}, 返回的流水号={}", smsMsg.getSerialId(), sendSmsResponse.getBizId());
                    return false;
                }
                logger.info("短信发送成功!");
                return true;
            } else {
                logger.error("短信发送失败: {}", sendSmsResponse);
                return false;
            }
        } catch (ClientException e) {
            logger.error("出现异常", e);
            return false;
        }
    }
}
