package io.github.taikonaut3.virtue.common.extension;

public class RpcContext extends AbstractAccessor {

    private static final ThreadLocal<RpcContext> threadLocal = new ThreadLocal<>();

    public static RpcContext getContext() {
        RpcContext context = threadLocal.get();
        if (context == null) {
            context = new RpcContext();
            threadLocal.set(context);
        }
        return context;
    }

    public RpcContext remove(String key) {
        super.remove(key);
        return this;
    }

    public void clear() {
        accessor.clear();
        threadLocal.remove();
    }

}
