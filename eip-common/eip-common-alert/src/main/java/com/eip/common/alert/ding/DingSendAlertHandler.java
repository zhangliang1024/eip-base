package com.eip.common.alert.ding;

import cn.hutool.json.JSONUtil;
import com.eip.common.alert.*;
import com.eip.common.alert.constant.AlertConstant;
import com.eip.common.alert.ding.domain.BaseDingDTO;
import com.eip.common.alert.ding.domain.DingImage;
import com.eip.common.alert.ding.domain.DingMarkdown;
import com.eip.common.alert.ding.domain.DingText;
import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.AlertRequest;
import com.eip.common.alert.BaseDTO;
import com.eip.common.alert.enums.AlertTypeEnum;
import com.eip.common.alert.enums.MessageType;

/**
 * ClassName: DingSendAlertHandler
 * Function: 钉钉通知
 * Date: 2021年12月15 10:20:07
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class DingSendAlertHandler implements SendAlertHandler {

    @Override
    public String getType() {
        return AlertTypeEnum.DING.name();
    }

    @Override
    public void sendAlertMessage(AlertRequest request) {
        String requestUrl = AlertConstant.DING_ROBOT_SERVER_URL + request.getSecretKey();
        BaseDTO baseDTO = baseDTO(request.getBasetDTO());
        String body = JSONUtil.toJsonStr(baseDTO);
        HttpAgent.sendRequest(body, requestUrl);
    }

    private BaseDTO baseDTO(AlertBasetDTO alertBasetDTO) {
        MessageType type = alertBasetDTO.getWxChatType();
        BaseDTO baseDTO = new BaseDTO();
        switch (type) {
            case TEXT:
                baseDTO = new BaseDingDTO((DingText) alertBasetDTO);
                break;
            case IMAGE:
                baseDTO = new BaseDingDTO((DingImage) alertBasetDTO);
                break;
            case MARKDOWN:
                baseDTO = new BaseDingDTO((DingMarkdown) alertBasetDTO);
                break;

        }
        return baseDTO;
    }

}
