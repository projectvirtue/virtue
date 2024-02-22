package io.github.taikonaut3.virtue.common.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used get attribute by generic.
 *
 * @param <T>
 */
public class AttributeKey<T> {

    private static final Map<String, AttributeKey<?>> KEY_POOL = new ConcurrentHashMap<>();

    private final String name;

    private AttributeKey(String name) {
        this.name = name;
    }

    /**
     * Get an attributeKey by name, if it doesn't exist, create it and put it into the pool.
     *
     * @param name
     * @param <T>
     * @return attributeKey instance
     */
    @SuppressWarnings("unchecked")
    public static <T> AttributeKey<T> get(String name) {
        AttributeKey<?> key = KEY_POOL.get(name);
        if (key == null) {
            key = new AttributeKey<T>(name);
            KEY_POOL.put(name, key);
        }
        return (AttributeKey<T>) key;
    }

    public String name() {
        return name;
    }

}
