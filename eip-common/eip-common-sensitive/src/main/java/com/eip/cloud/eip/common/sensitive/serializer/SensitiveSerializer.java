package com.eip.cloud.eip.common.sensitive.serializer;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.eip.cloud.eip.common.sensitive.annotation.IgnoreSensitive;
import com.eip.cloud.eip.common.sensitive.annotation.Sensitive;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveHandler;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveWrapper;
import com.eip.cloud.eip.common.sensitive.resolve.HandlerMethodResolver;
import com.eip.cloud.eip.common.sensitive.utils.HandlerMethodUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * ClassName: SensitiveSerializer
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class SensitiveSerializer extends JsonSerializer<String> {

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public void serialize(final String value, final JsonGenerator jsonGenerator, final SerializerProvider provider) throws IOException {
        if (Objects.isNull(value)) {
            jsonGenerator.writeNull();
            return;
        }

        HandlerMethodResolver handlerMethodResolver = SpringUtil.getBean(HandlerMethodResolver.class);
        HandlerMethod handlerMethod = handlerMethodResolver.resolve();
        if (ObjectUtils.isEmpty(handlerMethod)) {
            jsonGenerator.writeString(value);
            return;
        }

        IgnoreSensitive ignoreSensitive = HandlerMethodUtil.getAnnotation(handlerMethod, IgnoreSensitive.class);
        if (Objects.isNull(ignoreSensitive)) {
            String currentName = jsonGenerator.getOutputContext().getCurrentName();
            Object currentValue = jsonGenerator.getCurrentValue();
            Class<?> currentValueClass = currentValue.getClass();
            Field field = ReflectUtil.getField(currentValueClass, currentName);
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (Objects.nonNull(sensitive)) {
                Class<? extends SensitiveHandler> handlerClass = sensitive.value();
                SensitiveHandler handler = ReflectUtil.newInstance(handlerClass);
                String finalValue = handler.handler(new SensitiveWrapper(field, value, sensitive));
                jsonGenerator.writeString(finalValue);
                return;
            }
        }

        jsonGenerator.writeString(value);
    }
}
