package io.github.taikonaut3.virtue.serialization.jdk;

import io.github.taikonaut3.virtue.common.exception.SerializationException;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static io.github.taikonaut3.virtue.common.constant.Components.Serialize.JDK;

@ServiceProvider(value = JDK, interfaces = {Serializer.class})
public class JdkSerializer implements Serializer {

    @Override
    public byte[] serialize(Object input) throws SerializationException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(input);
            return outputStream.toByteArray();
        } catch (Throwable e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] input, Class<T> clazz) throws SerializationException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            Object object = objectInputStream.readObject();
            return clazz.cast(object);
        } catch (Throwable e) {
            throw new SerializationException(e);
        }
    }

}
