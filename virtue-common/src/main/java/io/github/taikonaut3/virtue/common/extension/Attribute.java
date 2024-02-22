package io.github.taikonaut3.virtue.common.extension;

/**
 * Used store Generic Object.
 *
 * @param <T>
 */
public class Attribute<T> {

    private T attribute;

    /**
     * Get attribute object.
     *
     * @return attribute object
     */
    public T get() {
        return attribute;
    }

    /**
     * Set attribute object.
     *
     * @param attribute
     */
    public void set(T attribute) {
        this.attribute = attribute;
    }
}
