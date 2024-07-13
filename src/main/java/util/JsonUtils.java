package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * @author 姚轶睿
 * @since 2022/6/24 10:29
 */
public class JsonUtils
{
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
    
    public static <T> String toJsonString(T t)
    {
        if(t == null){
            return null;
        }

        try
        {
            return OBJECT_MAPPER.writeValueAsString(t);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toJavaObject(String json,Class<T> targetClass){
        if(StringUtils.isBlank(json)){
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json,targetClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toJavaObject(String json, TypeReference<T> reference){
        if(StringUtils.isBlank(json)){
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json,reference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * StringEscapeUtils.escapeJson方法会将中文转义
     * 比如：河北，转义成-\u6CB3\u5317
     * 因此这里需要把中文转义回来
     */
    public static String escape(String json){
        if(StringUtils.isBlank(json)){
            return json;
        }

        return toJsonString(toJavaObject(json, HashMap.class));
    }
}
