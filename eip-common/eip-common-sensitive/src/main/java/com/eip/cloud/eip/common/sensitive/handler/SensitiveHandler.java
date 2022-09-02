package com.eip.cloud.eip.common.sensitive.handler;

import com.eip.cloud.eip.common.sensitive.annotation.Sensitive;
import com.eip.cloud.eip.common.sensitive.format.Desensitize;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class SensitiveHandler implements SecurityHandler<Sensitive> {

    private Desensitize desensitize;

    @Override
    public boolean support(Field field) {
        Sensitive annotation = field.getAnnotation(Sensitive.class);
        return annotation != null && annotation.required();
    }

    @Override
    public Sensitive acquire(Field field) {
        Sensitive annotation = null;
        if (field != null) {
            annotation = field.getAnnotation(Sensitive.class);
        }
        return annotation;
    }

    @Override
    public String handleEncrypt(String source, Sensitive annotation) {
        return desensitize.format(source, annotation);
    }

    @Override
    public String handleDecrypt(String source, Sensitive annotation) {
        return source;
    }
}