package com.eip.common.core.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * ClassName: SensitiveSerialize
 * Function:
 * Date: 2022年04月12 17:53:21
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

    /**
     * 脱敏类型
     */
    private SensitiveTypeEnum sensitiveTypeEnum;

    /**
     * 前几位不脱敏
     */
    private Integer prefixNoMaskLen;

    /**
     * 最后几位不脱敏
     */
    private Integer suffixNoMaskLen;

    /**
     * 用什么打码
     */
    private String symbol;

    @Override
    public void serialize(final String origin, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        switch (sensitiveTypeEnum) {
            case CUSTOMER:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, symbol));
                break;
            case NAME:
                jsonGenerator.writeString(DesensitizedUtils.chineseName(origin));
                break;
            case ID_NUM:
                jsonGenerator.writeString(DesensitizedUtils.idCardNum(origin));
                break;
            case PHONE_NUM:
                jsonGenerator.writeString(DesensitizedUtils.mobilePhone(origin));
                break;
            default:
                throw new IllegalArgumentException("unknown sensitive type enum " + sensitiveTypeEnum);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
                if (sensitive == null) {
                    sensitive = beanProperty.getContextAnnotation(Sensitive.class);
                }
                if (sensitive != null) {
                    return new SensitiveSerialize(sensitive.type(), sensitive.prefixNoMaskLen(),sensitive.suffixNoMaskLen(), sensitive.symbol());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }

}

