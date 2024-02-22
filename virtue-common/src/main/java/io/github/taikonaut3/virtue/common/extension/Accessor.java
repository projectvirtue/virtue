package io.github.taikonaut3.virtue.common.extension;

/**
 * Object accessor.
 *
 * @see AttributeKey
 */
public interface Accessor {

    /**
     * Get attribute by attributeKey.
     *
     * @param key
     * @param <T>
     * @return attribute
     */
    <T> Attribute<T> attribute(AttributeKey<T> key);

    /**
     * Remove attribute by attributeKey's name.
     *
     * @param key
     * @return current instance
     */
    Accessor remove(String key);

}
