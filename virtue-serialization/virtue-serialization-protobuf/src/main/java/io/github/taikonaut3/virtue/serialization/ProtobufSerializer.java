package io.github.taikonaut3.virtue.serialization;

import io.github.taikonaut3.virtue.common.exception.SerializationException;

public class ProtobufSerializer implements Serializer{
    @Override
    public byte[] serialize(Object input) throws SerializationException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        return null;
    }
}
