package com.eip.common.alert.ding.domain;

import com.eip.common.alert.BaseDTO;
import com.eip.common.alert.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * ClassName: BaseDingDTO
 * Function:
 * Date: 2021年12月16 11:01:29
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class BaseDingDTO extends BaseDTO {

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private DingText text;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private DingMarkdown markdown;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private DingImage image;


    public BaseDingDTO(DingText text){
        this.msgtype = MessageType.TEXT.getType();
        this.text = text;
    }

    public BaseDingDTO(DingMarkdown markdown){
        this.msgtype = MessageType.MARKDOWN.getType();
        this.markdown = markdown;
    }

    public BaseDingDTO(DingImage image){
        this.msgtype = MessageType.PHOTO.getType();
        this.image = image;
    }
}
