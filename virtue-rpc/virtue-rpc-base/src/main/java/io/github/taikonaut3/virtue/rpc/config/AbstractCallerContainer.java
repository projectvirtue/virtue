package io.github.taikonaut3.virtue.rpc.config;

import io.github.taikonaut3.virtue.common.util.AssertUtil;
import io.github.taikonaut3.virtue.config.Caller;
import io.github.taikonaut3.virtue.config.CallerContainer;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Accessors(fluent = true)
public abstract class AbstractCallerContainer implements CallerContainer {
    private static final Logger logger = LoggerFactory.getLogger(AbstractServerCaller.class);

    protected Map<Method, Caller<?>> callerMap = new HashMap<>();

    protected Map<String, Caller<?>> identificationCallerMap = new HashMap<>();

    @Getter
    protected Virtue virtue;

    @Setter
    protected String remoteApplication;

    @Getter
    protected String proxy;

    protected AbstractCallerContainer(Virtue virtue) {
        AssertUtil.notNull(virtue);
        this.virtue = virtue;
    }

    @Override
    public void start() {
        for (Caller<?> caller : callerMap.values()) {
            caller.start();
        }
    }

    @Override
    public Caller<?>[] callers() {
        return callerMap.values().toArray(Caller[]::new);
    }

    @Override
    public Caller<?> getCaller(Method method) {
        return callerMap.get(method);
    }

    @Override
    public String remoteApplication() {
        return remoteApplication;
    }

    @Override
    public Caller<?> getCaller(String identification) {
        return identificationCallerMap.get(identification);
    }
}
