package io.github.taikonaut3.virtue.common.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Accessor abstract class.
 */
public abstract class AbstractAccessor implements Accessor {

    protected Map<AttributeKey<?>, Attribute<?>> accessor = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> Attribute<T> attribute(AttributeKey<T> key) {
        Attribute<?> attribute = accessor.get(key);
        if (attribute == null) {
            attribute = new Attribute<T>();
            accessor.put(key, attribute);
        }
        return (Attribute<T>) attribute;
    }

    @Override
    public Accessor remove(String key) {
        AttributeKey<Object> attributeKey = AttributeKey.get(key);
        accessor.remove(attributeKey);
        return this;
    }
}
