package io.github.taikonaut3.virtue.config;

import java.lang.reflect.Type;

/**
 * Encapsulate the method call parameters.
 */
public interface CallArgs {

    /**
     * Get the arguments of the method call.
     *
     * @return all args
     */
    Object[] args();

    /**
     * Get the return type of the method.
     *
     * @return {@link Caller#returnType()}
     */
    Type returnType();

    /**
     * Get the parameter types of the method.
     *
     * @return {@link Caller#method()java.lang.reflect.Method#getGenericParameterTypes()}
     */
    Type[] parameterTypes();

    /**
     * Get the caller object for the method.
     *
     * @return current caller
     */
    Caller<?> caller();

}

