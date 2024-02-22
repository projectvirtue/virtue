package io.github.taikonaut3.virtue.proxy.bytebuddy;

import io.github.taikonaut3.virtue.proxy.InvocationHandler;
import io.github.taikonaut3.virtue.proxy.SuperInvoker;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class MethodInterceptor implements InvocationHandler {

    private final Object target;

    private final InvocationHandler invocationHandler;

    public MethodInterceptor(Object target, InvocationHandler invocationHandler) {
        this.target = target;
        this.invocationHandler = invocationHandler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, SuperInvoker<?> superInvoker) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        return invocationHandler.invoke(proxy, method, args, superInvoker);
    }

    @Override
    public String toString() {
        return target.toString();
    }

    public class InterfaceInterceptor {

        @RuntimeType
        public Object intercept(@This Object proxy, @Origin Method method, @AllArguments Object[] args) throws Throwable {
            return invoke(proxy, method, args, null);
        }
    }

    public class InstanceInterceptor {

        @RuntimeType
        public Object intercept(@This Object proxy, @Origin Method method, @AllArguments Object[] args, @SuperCall Callable<?> callable) throws Throwable {
            return invoke(proxy, method, args, callable::call);
        }

    }

}