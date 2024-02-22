package io.github.taikonaut3.virtue.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.github.taikonaut3.virtue.common.exception.SerializationException;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.serialization.Serializer;

import static io.github.taikonaut3.virtue.common.constant.Components.Serialize.KRYO;

@ServiceProvider(value = KRYO)
public class KryoSerializer implements Serializer {

    // Kryo is not thread safe. Each thread should have its own Kryo, Input, and Output instances.
    private static final int BUFFER_SIZE = 4096; // 设置较大的缓冲区大小

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object input) throws SerializationException {
        try (Output output = new Output(BUFFER_SIZE, -1)) { // 使用自定义的缓冲区大小
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, input);
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializationException("Failed to serialize object", e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        try (Input input = new Input(bytes)) {
            Kryo kryo = kryoThreadLocal.get();
            return kryo.readObject(input, clazz);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize object", e);
        }
    }

}
