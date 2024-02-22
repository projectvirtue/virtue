package io.github.taikonaut3.virtue.proxy.jdk;

import io.github.taikonaut3.virtue.proxy.InvocationHandler;
import io.github.taikonaut3.virtue.proxy.SuperInvoker;

import java.lang.reflect.Method;

/**
 * JDK InvocationHandler Impl
 */
public class JDKInvocationHandler implements java.lang.reflect.InvocationHandler {

    private final Object target;

    private final InvocationHandler handler;

    public JDKInvocationHandler(Object target, InvocationHandler handler) {
        this.target = target;
        this.handler = handler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        return handler.invoke(proxy, method, args, superInvoker(method, args));

    }

    private SuperInvoker<?> superInvoker(Method method, Object[] args) {
        if (target instanceof Class<?>) {
            return null;
        }
        return () -> method.invoke(target, args);
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
