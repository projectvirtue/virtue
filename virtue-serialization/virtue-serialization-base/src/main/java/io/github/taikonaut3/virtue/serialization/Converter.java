package io.github.taikonaut3.virtue.serialization;

import io.github.taikonaut3.virtue.common.exception.ConversionException;

import java.lang.reflect.Type;

/**
 * For cross-system transmission, often used for common serialization to local type.
 */
public interface Converter {

    /**
     * Converts an array of objects to the specified types.
     *
     * @param args The array of objects to convert.
     * @param type The array of types to convert the objects to.
     * @return An array of converted objects.
     * @throws ConversionException if an error occurs during conversion.
     */
    default Object[] convert(Object[] args, Type[] type) throws ConversionException {
        return args;
    }

    /**
     * Converts an object to the specified type.
     *
     * @param arg  The object to convert.
     * @param type The type to convert the object to.
     * @return The converted object.
     * @throws ConversionException if an error occurs during conversion.
     */
    default Object convert(Object arg, Type type) throws ConversionException {
        return arg;
    }

}


