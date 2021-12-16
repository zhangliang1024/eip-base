package com.eip.common.alert.wxchat;

import cn.hutool.json.JSONUtil;
import com.eip.common.alert.*;
import com.eip.common.alert.constant.AlertConstant;
import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.AlertRequest;
import com.eip.common.alert.BaseDTO;
import com.eip.common.alert.enums.AlertTypeEnum;
import com.eip.common.alert.enums.MessageType;
import com.eip.common.alert.wxchat.domain.*;
import org.apache.commons.lang3.StringUtils;
import java.util.Collections;
import java.util.Objects;


/**
 * ClassName: WeChatSendAlertHandler
 * Function: 企业微信告警
 * Date: 2021年12月15 11:41:54
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class WeChatSendAlertHandler implements SendAlertHandler {


    @Override
    public String getType() {
        return AlertTypeEnum.WXCHAT.name();
    }

    @Override
    public void sendAlertMessage(AlertRequest request) {
        String requestUrl = AlertConstant.WE_CHAT_ROBOT_SERVER_URL + request.getSecretKey();
        BaseDTO baseDTO = baseDTO(request.getBasetDTO());
        doHttpSend(baseDTO, requestUrl);
    }

    private void doHttpSend(BaseDTO baseDTO, String requestUrl) {
        //文件类型消息且mediaId为空，则先上传文件获取mediaId
        if (MessageType.FILE.getType().equals(baseDTO.getMsgtype())) {
            WxChatBaseDTO chatBaseDTO = (WxChatBaseDTO) baseDTO;
            WxChatFile file = chatBaseDTO.getFile();
            if (Objects.nonNull(file) && StringUtils.isBlank(file.getMediaId())) {
                String uploadRrl = AlertConstant.WE_CHAT_ROBOT_SERVER_URL.replace("send", "upload_media") + "&type=file";
                String mediaId = HttpAgent.sendRequest(file.getFile(), uploadRrl);
                file.setMediaId(mediaId);
            }
        }
        String body = JSONUtil.toJsonStr(baseDTO);
        HttpAgent.sendRequest(body, requestUrl);
    }

    public BaseDTO baseDTO(AlertBasetDTO alertBaseService) {
        MessageType type = alertBaseService.getWxChatType();
        BaseDTO baseDTO = new BaseDTO();
        switch (type) {
            case TEXT:
                baseDTO = text((WxChatText) alertBaseService);
                break;
            case IMAGE:
                baseDTO = new WxChatBaseDTO((WxChatImage) alertBaseService);
                break;
            case FILE:
                baseDTO = new WxChatBaseDTO((WxChatFile) alertBaseService);
                break;
            case NEWS:
                baseDTO = new WxChatBaseDTO((WxChatNews) alertBaseService);
                break;
            case MARKDOWN:
                baseDTO = new WxChatBaseDTO((WxChatMarkdown) alertBaseService);
                break;

        }
        return baseDTO;
    }

    public WxChatBaseDTO text(WxChatText text) {
        //@所有人
        if (text.isAtAll()) {
            text.setMentionedList(Collections.singletonList("@all"));
        }
        WxChatBaseDTO baseMessage = new WxChatBaseDTO(text);
        return baseMessage;
    }

}
