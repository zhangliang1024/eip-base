package com.eip.cloud.eip.common.sensitive.enums;

import lombok.Getter;

/**
 * ClassName: AbstractSensitiveStrategy
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Getter
public enum SensitiveStrategy {

    CHINESE_NAME("中文"),
    ID_CARD("身份证"),
    FIXED_PHONE("固定电话"),
    MOBILE_PHONE("手机号"),
    ADDRESS("地址"),
    EMAIL("邮箱"),
    PASSWORD("密码"),
    CAR_LICENSE("车牌"),
    BANK_CARD("银行卡"),
    CUSTOMIZE_KEEP_LENGTH("自定义脱密长度"),
    CUSTOMIZE_WORDS_FILTER(" 敏感字符脱敏"),
    CUSTOMIZE_HANDLER("自定义脱密逻辑");


    private String type;

    SensitiveStrategy(String type) {
        this.type = type;
    }


}
