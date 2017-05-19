package com.eparty.ccp.redis.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SerializeUtil {

    public static Logger logger = Logger.getLogger(SerializeUtil.class);

    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JSR310Module());
    }

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }
        return null;
    }

    public static String beanToJson(Object o) throws JsonException {

        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            logger.error("bean转json出错", e);
            throw new JsonException("系统异常", e);
        }

    }

    public static <T> T jsonToBean(String s, Class<T> clazz) throws JsonException {

        try {
            return mapper.readValue(s, clazz);
        } catch (Exception e) {
            logger.error("json转化为bean出错", e);
            throw new JsonException("系统异常", e);
        }

    }

    public static <T> List<T> getObjectsFromJson(String in, Class<T> clsT) {
        try {
            return mapper.readValue(in, List.class);
        } catch (Exception e) {
            throw new JsonException("json转为list报错", e);

        }
        // JsonParser parser = mapper.getJsonFactory().createJsonParser(in);
        //
        // JsonNode nodes = parser.readValueAsTree();
        // List<T> list = new ArrayList<T>(nodes.size());
        // for (JsonNode node : nodes) {
        // list.add(mapper.readValue(node, clsT));
        // }
        // return list;

    }

    public static Object deepClone(Object o) throws Exception {
        // 将对象写到流里
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(o);
        // 从流里读出来
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (oi.readObject());
    }

}
