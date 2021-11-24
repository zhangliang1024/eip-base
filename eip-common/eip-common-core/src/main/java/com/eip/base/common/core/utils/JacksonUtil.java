package com.eip.base.common.core.utils;

import com.eip.base.common.core.exception.BaseRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.json.JsonSanitizer;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
public class JacksonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

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
