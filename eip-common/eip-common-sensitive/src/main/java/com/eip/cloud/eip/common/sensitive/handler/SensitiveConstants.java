package com.eip.cloud.eip.common.sensitive.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * ClassName: SensitiveConstants
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SensitiveConstants {

    /**
     * 默认脱敏替换符
     */
    public static final char REPLACER = '*';

    /**
     * 默认不需要打码的长度
     */
    public static final int NO_MASK_LEN = 0;

    /**
     * 不处理脱敏保留的标志
     */
    public static final int NOP_KEEP = -1;

    /**
     * 普通车牌号长度
     */
    public static final int ORDINARY_CAR_LICENSE_LENGTH = 7;

    /**
     * 新能源车牌号长度
     */
    public static final int NEW_ENERGY_CAR_LICENSE_LENGTH = 8;

    /**
     * 错误的银行卡号长度
     */
    public static final int ERROR_BANK_CARD_LENGTH = 9;
}
