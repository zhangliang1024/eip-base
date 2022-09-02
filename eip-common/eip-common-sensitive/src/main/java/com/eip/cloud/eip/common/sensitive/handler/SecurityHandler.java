package com.eip.cloud.eip.common.sensitive.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface SecurityHandler<A extends Annotation> {

    boolean support(Field field);

    A acquire(Field field);

    String handleEncrypt(String source, A annotation);

    String handleDecrypt(String source, A annotation);
}