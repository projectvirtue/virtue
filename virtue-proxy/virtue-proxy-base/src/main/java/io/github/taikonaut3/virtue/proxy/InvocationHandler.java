package io.github.taikonaut3.virtue.proxy;

import java.lang.reflect.Method;

/**
 * Invocation handler for handling method invocations on a proxy instance.
 */
@FunctionalInterface
public interface InvocationHandler {

    /**
     * Processes the method invocation on the specified proxy instance and returns the result.
     *
     * @param proxy        the proxy instance that the method was invoked on
     * @param method       the method being invoked
     * @param args         the arguments passed to the method
     * @param superInvoker invoke the original method
     * @return the result of the method invocation
     * @throws Throwable if an error occurs while processing the method invocation
     */
    Object invoke(Object proxy, Method method, Object[] args, SuperInvoker<?> superInvoker) throws Throwable;

}

