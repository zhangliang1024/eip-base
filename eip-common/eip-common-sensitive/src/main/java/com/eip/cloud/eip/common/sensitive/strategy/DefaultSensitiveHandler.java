package com.eip.cloud.eip.common.sensitive.strategy;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.eip.cloud.eip.common.sensitive.annotation.Sensitive;
import com.eip.cloud.eip.common.sensitive.annotation.SensitiveFilter;
import com.eip.cloud.eip.common.sensitive.enums.SensitiveStrategy;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveConstants;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveHandler;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveWrapper;
import com.eip.cloud.eip.common.sensitive.utils.SensitiveUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

/**
 * ClassName: DefaultSensitiveHandler
 * Function: 默认处理逻辑
 * Date: 2022年09月27 16:38:05
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class DefaultSensitiveHandler implements SensitiveHandler {


    public String handler(SensitiveWrapper sensitiveWrapper) {
        Sensitive sensitive = sensitiveWrapper.getSensitive();
        SensitiveStrategy strategy = sensitive.type();
        switch (strategy) {
            case CHINESE_NAME:
                return SensitiveUtil.chineseName(sensitiveWrapper.getFieldValue(), sensitive.symbol());
            case ID_CARD:
                return SensitiveUtil.idCardNum(sensitiveWrapper.getFieldValue(), 1, 2, sensitive.symbol());
            case FIXED_PHONE:
                return SensitiveUtil.fixedPhone(sensitiveWrapper.getFieldValue(), sensitive.symbol());
            case MOBILE_PHONE:
                return SensitiveUtil.mobilePhone(sensitiveWrapper.getFieldValue(), sensitive.symbol());
            case ADDRESS:
                return SensitiveUtil.address(sensitiveWrapper.getFieldValue(), 8, sensitive.symbol());
            case EMAIL:
                return SensitiveUtil.email(sensitiveWrapper.getFieldValue(), sensitive.symbol());
            case PASSWORD:
                return SensitiveUtil.password(sensitiveWrapper.getFieldValue(), sensitive.symbol());
            case CAR_LICENSE:
                return SensitiveUtil.carLicense(sensitiveWrapper.getFieldValue(), sensitive.symbol());
            case BANK_CARD:
                return SensitiveUtil.bankCard(sensitiveWrapper.getFieldValue(), sensitive.symbol());
            case CUSTOMIZE_WORDS_FILTER:
                return customizeWordsFilter(sensitiveWrapper);
            case CUSTOMIZE_KEEP_LENGTH:
                return customizeKeepLength(sensitiveWrapper);
            case CUSTOMIZE_HANDLER:
                return customizeHandler(sensitiveWrapper);
        }
        return null;
    }

    public String customizeWordsFilter(SensitiveWrapper sensitiveWrapper) {
        String fieldValue = sensitiveWrapper.getFieldValue();
        Field field = sensitiveWrapper.getField();
        Sensitive sensitive = sensitiveWrapper.getSensitive();
        SensitiveFilter filterWords = field.getAnnotation(SensitiveFilter.class);
        if (ObjectUtils.isEmpty(filterWords)) {
            log.warn("{} not has @SensitiveFilter, will ignore sensitive it.", field.getName());
            return fieldValue;
        }

        char replacer = sensitive.symbol();
        String[] words = filterWords.value();
        if (!ObjectUtils.isEmpty(words)) {
            for (String filterWord : words) {
                if (fieldValue.contains(filterWord)) {
                    String replacers = CharSequenceUtil.repeat(replacer, filterWord.length());
                    fieldValue = fieldValue.replace(filterWord, replacers);
                }
            }
        }
        return fieldValue;
    }

    public String customizeKeepLength(SensitiveWrapper sensitiveWrapper) {
        String fieldValue = sensitiveWrapper.getFieldValue();
        Sensitive sensitive = sensitiveWrapper.getSensitive();
        int prefix = sensitive.prefix();
        int suffix = sensitive.suffix();
        Assert.isTrue(prefix >= SensitiveConstants.NOP_KEEP, "prefix must greater than -1");
        Assert.isTrue(suffix >= SensitiveConstants.NOP_KEEP, "suffix must greater than -1");

        boolean ignorePreKeep = prefix <= 0;
        boolean ignoreSuffixKeep = suffix <= 0;
        if (ignorePreKeep && ignoreSuffixKeep) {
            return fieldValue;
        }

        char replacer = sensitive.symbol();
        return CharSequenceUtil.replace(fieldValue, prefix, fieldValue.length() - suffix, replacer);
    }

    public String customizeHandler(SensitiveWrapper sensitiveWrapper) {
        Sensitive sensitive = sensitiveWrapper.getSensitive();
        Class<? extends SensitiveHandler> handlerClass = sensitive.value();
        SensitiveHandler handler = ReflectUtil.newInstance(handlerClass);
        return handler.customHandler(sensitiveWrapper);
    }

    @Override
    public String customHandler(SensitiveWrapper sensitiveWrapper) {
        return null;
    }
}
