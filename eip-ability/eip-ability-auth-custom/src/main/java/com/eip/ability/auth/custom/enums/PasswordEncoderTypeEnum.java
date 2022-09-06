package com.eip.ability.auth.custom.enums;

import lombok.Getter;

/**
 * ClassName: PasswordEncoderTypeEnum
 * Function:
 * Date: 2022年09月05 09:49:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Getter
public enum PasswordEncoderTypeEnum {

    NOOP("{noop}", "无加密明文"),
    BCRYPT("{bcrypt}", "BCRYPT加密");

    private String prefix;

    PasswordEncoderTypeEnum(String prefix, String desc) {
        this.prefix = prefix;
    }
}
