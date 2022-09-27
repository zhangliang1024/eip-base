package com.eip.cloud.eip.common.sensitive.handler;

import com.eip.cloud.eip.common.sensitive.annotation.Sensitive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

/**
 * ClassName: SensitiveWrapper
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWrapper {

    /**
     * 字段
     */
    private Field field;
    /**
     * 字段值
     */
    private String fieldValue;
    /**
     * 脱密注解
     */
    private Sensitive sensitive;
}
