package com.eip.cloud.eip.common.sensitive.serializer;

import com.eip.cloud.eip.common.sensitive.annotation.Sensitive2;
import com.eip.cloud.eip.common.sensitive.enums.SensitiveTypeEnum;
import com.eip.cloud.eip.common.sensitive.format.CommonDesensitize;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.Data;

import java.io.IOException;
import java.util.Objects;

@Data
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveTypeEnum typeEnum;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (this.typeEnum) {
            case CHINESE_NAME:
                jsonGenerator.writeString(CommonDesensitize.chineseName(s));
                break;
            case ID_CARD:
                jsonGenerator.writeString(CommonDesensitize.idCardNum(s));
                break;
            case FIXED_PHONE:
                jsonGenerator.writeString(CommonDesensitize.fixedPhone(s));
                break;
            case MOBILE_PHONE:
                jsonGenerator.writeString(CommonDesensitize.mobilePhone(s));
                break;
            case ADDRESS:
                jsonGenerator.writeString(CommonDesensitize.address(s, 8));
                break;
            case EMAIL:
                jsonGenerator.writeString(CommonDesensitize.email(s));
                break;
            case BANK_CARD:
                jsonGenerator.writeString(CommonDesensitize.bankCard(s));
                break;
            case PASSWORD:
                jsonGenerator.writeString(CommonDesensitize.password(s));
                break;
            case CARNUMBER:
                jsonGenerator.writeString(CommonDesensitize.carNumber(s));
                break;
            default:
                jsonGenerator.writeString(s);
                break;
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        // 为空直接跳过
        if (beanProperty != null) {
            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Sensitive2 sensitive = beanProperty.getAnnotation(Sensitive2.class);
                if (sensitive == null) {
                    sensitive = beanProperty.getContextAnnotation(Sensitive2.class);
                }
                //
                if (sensitive != null) {
                    this.setTypeEnum(sensitive.type());
                    return this;
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        } else {
            return serializerProvider.findNullValueSerializer(null);
        }
    }
}