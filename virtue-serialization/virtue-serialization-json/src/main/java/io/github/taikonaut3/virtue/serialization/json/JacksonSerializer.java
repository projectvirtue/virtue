package io.github.taikonaut3.virtue.serialization.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.github.taikonaut3.virtue.common.exception.ConversionException;
import io.github.taikonaut3.virtue.common.exception.SerializationException;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.serialization.Serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import static io.github.taikonaut3.virtue.common.constant.Components.Serialize.JSON;

@ServiceProvider(JSON)
public class JacksonSerializer implements Serializer {

    private final ObjectMapper objectMapper;

    public JacksonSerializer() {
        this.objectMapper = JsonMapper.builder().configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true).build();
    }

    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    @Override
    public byte[] serialize(Object input) throws SerializationException {
        if (input == null) {
            return new byte[0];
        }
        try {
            return objectMapper.writeValueAsBytes(input);
        } catch (Throwable e) {
            throw new SerializationException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        if (clazz == String.class) {
            return (T) new String(bytes);
        }
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public Object[] convert(Object[] args, Type[] types) throws ConversionException {
        Object[] objects = new Object[types.length];
        try {
            // 遍历 Object[] 数组并匹配相应类型
            for (int i = 0; i < args.length; i++) {
                Object deserializedObject = args[i];
                Type objectType = types[i];
                // 将反序列化的对象转换为指定类型
                Object typedObject = objectMapper.convertValue(deserializedObject, objectMapper.constructType(objectType));
                objects[i] = typedObject;
            }
        } catch (Exception e) {
            throw new ConversionException(e);
        }
        return objects;
    }

    @Override
    public Object convert(Object arg, Type type) throws ConversionException {
        try {
            if (arg.getClass() == String.class) {
                if (type == String.class) {
                    return arg;
                }
                TypeFactory typeFactory = objectMapper.getTypeFactory();
                JavaType javaType = typeFactory.constructType(type);
                return objectMapper.readValue((String) arg, javaType);
            }
            return objectMapper.convertValue(arg, objectMapper.constructType(type));
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }

}
