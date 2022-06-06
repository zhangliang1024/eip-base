package com.eip.common.core.utils;

import com.eip.common.core.core.exception.BaseRuntimeException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.json.JsonSanitizer;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
public class JacksonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**日起格式化*/
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        //对象的所有字段全部列入
        MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        MAPPER.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 将对象转换成json字符串
     */
    public static String objectToStr(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (Exception e) {
            log.error("json parse error : {}",e.getMessage());
            throw new BaseRuntimeException("json parse error");
        }
    }

    /**
     * 将json结果集转化为对象
     */
    public static <T> T jsonToPo(String json, Class<T> bean) {
        String jsonData = JsonSanitizer.sanitize(json);
        try {
            return MAPPER.readValue(jsonData, bean);
        } catch (Exception e) {
            log.error("json parse error : {}",e.getMessage());
            throw new BaseRuntimeException("json parse error");
        }
    }

    /**
     * 将json数据转换成pojo对象list
     */
    public static <T> List<T> jsonToList(String json, Class<T> bean) {
        String jsonData = JsonSanitizer.sanitize(json);
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, bean);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error("json parse error : {}",e.getMessage());
            throw new BaseRuntimeException("json parse error");
        }
    }

}
