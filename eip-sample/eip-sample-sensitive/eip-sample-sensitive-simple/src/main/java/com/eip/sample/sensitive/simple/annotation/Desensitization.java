package com.eip.sample.sensitive.simple.annotation;

import com.eip.sample.sensitive.simple.serialize.ConvertDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description: 数据脱敏
 * 1、默认不传部位、不传显示*号数量时字段全部脱敏
 *
 * 原始字符串 adminis 总长度从0计算 总数6
 * index=(0,2) size = 1 下标即从0到2以内的字符标注“ * ”，size=1 则只填充一个* size 不能超过截取字符
 * index=(2,3) size = 2 下标即从2到3以内的字符标注“ * ”，size=2 则只填充二个* size 不能超过截取字符
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = ConvertDesensitization.class)
public @interface Desensitization {

    /**
     * 传入的下标索引
     * 规则 第一位起始下标 第二位是结束下标 默认值6位下标
     **/
    int[] index() default {0, 6};

    /**
     * 需要脱敏的字符长度
     * 规则 输入 3 ：则根据index下标索引对应脱敏3个字符 默认6个长度脱敏
     **/
    int size() default 6;


}