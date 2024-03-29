package com.eip.common.core.core.assertion;

import cn.hutool.core.util.StrUtil;
import com.eip.common.core.core.exception.BaseRuntimeException;

import java.util.Collection;
import java.util.Map;

/**
 * 枚举类异常断言，提供简单的方式判断条件，并在条件满足时抛出异常
 * 错误码和错误信息定义在枚举的枚举类中，在本断言方法中，传递错误信息需要的参数
 */
public interface Assert {

    /**
     * 创建异常
     * @param args
     */
    BaseRuntimeException newException(Object... args);

    /**
     * 创建异常
     * @param cause
     * @param args
     */
    BaseRuntimeException newException(Throwable cause, Object... args);

    /**
     * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * @param obj 待判断对象
     */
    default void assertNotNull(Object obj){
        if (obj == null){
            throw newException();
        }
    }

    /**
     * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     * @param obj 待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertNotNull(Object obj, Object... args){
        if (obj == null){
            throw newException(args);
        }
    }

    /**
     * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     * @param str 待判断字符串
     */
    default void assertNotEmpty(String str){
        if (StrUtil.isBlank(str)){
            throw newException();
        }
    }

    /**
     * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     * @param str 待判断字符串
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(String str, Object... args){
        if (null == str || "".equals(str.trim())){
            throw newException();
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小为0，则抛出异常
     * @param arrays 待判断数组
     */
    default void assertNotEmpty(Object[] arrays){
        if (arrays == null || arrays.length == 0) {
            throw newException();
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小为0，则抛出异常
     * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     * @param arrays 待判断数组
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Object[] arrays, Object... args){
        if (arrays == null || arrays.length == 0) {
            throw newException(args);
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小为0，则抛出异常
     * @param c 待判断集合
     */
    default void assertNotEmpty(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            throw newException();
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小为0，则抛出异常
     * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     * @param c 待判断集合
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Collection<?> c, Object args) {
        if (c == null || c.isEmpty()) {
            throw newException(args);
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小为0，则抛出异常
     * @param map 待判断Map
     */
    default void assertNotEmpty(Map<?, ?> map) {
        if (map ==  null || map.isEmpty()) {
            throw newException();
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小为0，则抛出异常
     * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     * @param map 待判断Map
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Map<?, ?> map, Object... args) {
        if (map ==  null || map.isEmpty()) {
            throw newException(args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     * @param expression 待判断布尔变量
     */
    default void assertIsFalse(boolean expression) {
        if (expression) {
            throw newException();
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     * @param expression 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsFalse(boolean expression, Object... args) {
        if (expression) {
            throw newException(args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     * @param expression 待判断布尔变量
     */
    default void assertIsTrue(boolean expression) {
        if (!expression) {
            throw newException();
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     * @param expression 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsTrue(boolean expression, Object... args) {
        if (!expression) {
            throw newException(args);
        }
    }

    /**
     * 断言对象<code>obj</code>为null。如果对象<code>obj</code>不为null，则抛出异常
     * @param obj 待判断对象
     */
    default void assertIsNull(Object obj) {
        if (obj != null) {
            throw newException();
        }
    }

    /**
     * <p>断言对象<code>obj</code>为null。如果对象<code>obj</code>不为null，则抛出异常
     *异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     * @param obj 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsNull(Object obj, Object... args) {
        if (obj != null) {
            throw newException(args);
        }
    }

    /**
     * 直接抛出异常
     */
    default void assertFail(){
        throw newException();
    }

    /**
     * 直接抛出异常
     * @param args message占位符对应的参数列表
     */
    default void assertFail(Object... args){
        throw newException(args);
    }

    /**
     * 直接抛出异常，并包含原异常信息
     * 当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时，必须须传递原始异常，作为新异常的cause
     * @param t 原始异常
     */
    default void assertFail(Throwable t){
        throw newException();
    }

    /**
     * 直接抛出异常，并包含原异常信息
     * 当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时，必须传递原始异常，作为新异常的cause
     * @param t 原始异常
     * @param args message占位符对应的参数列表
     */
    default void assertFail(Throwable t, Object... args) {
        throw newException(t, args);
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。
     * 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2  待判断对象
     */
    default void assertEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null) {
            throw newException();
        }
        if (!o1.equals(o2)) {
            throw newException();
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。
     * 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2  待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertEquals(Object o1, Object o2, Object... args) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null) {
            throw newException(args);
        }
        if (!o1.equals(o2)) {
            throw newException(args);
        }
    }

}
