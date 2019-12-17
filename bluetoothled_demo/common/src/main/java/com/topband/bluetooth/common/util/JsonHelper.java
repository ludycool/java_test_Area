package com.topband.bluetooth.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    final static String FORMATTER_STR = "yyyy-MM-dd HH:mm:ss";
    /**
     * 001.json转换成对象
     *
     * @param c
     * @param jsonStr
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T fromJson(Class<T> c, String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(FORMATTER_STR));
        try {
            return mapper.readValue(jsonStr, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> listFromJson(Class<T> c, String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (jsonStr != null && !jsonStr.equals("")) {
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, c);
                return mapper.readValue(jsonStr, listType);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复杂转换 例如： sonHelper.fromJson(new TypeReference<List<T>>(){},str);
     *
     * @param c
     * @param jsonStr
     * @param <T>
     * @return
     */
    public static <T> T fromJson(TypeReference<T> c, String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (jsonStr != null && !jsonStr.equals("")) {
                return mapper.readValue(jsonStr, c);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 002.对象转换成json
     * @param:传入对象
     * @return:json字符串
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(FORMATTER_STR));
        return mapper.writeValueAsString(obj);
    }

    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return
     */
    public static Map<String, Object> object2Map(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(FORMATTER_STR));
        Map<String, Object> map = new HashMap<>();
        try {
            String jsonStr = mapper.writeValueAsString(obj);
            map = mapper.readValue(jsonStr, map.getClass());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Map转成实体对象
     *
     * @param map map实体对象包含属性
     * @param
     * @return
     */
    public static <T> T map2bject(Class<T> c, Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(FORMATTER_STR));
        try {
            String jsonStr = mapper.writeValueAsString(map);
            return mapper.readValue(jsonStr, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}