package io.github.taikonaut3.virtue.rpc.config;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.CallArgs;
import io.github.taikonaut3.virtue.config.Invocation;
import io.github.taikonaut3.virtue.config.RemoteService;
import io.github.taikonaut3.virtue.config.ServerCaller;
import io.github.taikonaut3.virtue.config.config.ServerConfig;
import io.github.taikonaut3.virtue.config.filter.Filter;
import io.github.taikonaut3.virtue.config.filter.FilterScope;
import io.github.taikonaut3.virtue.transport.server.Server;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Getter
@Accessors(fluent = true)
public abstract class AbstractServerCaller<T extends Annotation> extends AbstractCaller<T> implements ServerCaller<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServerCaller.class);

    public static final Map<String, Server> hadExportServer = new ConcurrentHashMap<>();

    protected String desc;

    protected AbstractServerCaller(Method method, RemoteService<?> remoteService, String protocol, Class<T> annoType) {
        super(method, remoteService, protocol, annoType);
    }

    @Override
    public void init() {
        doInit();
        ServerConfig serverConfig = virtue.configManager().serverConfigManager().get(protocol);
        if (serverConfig != null) {
            url = createUrl(serverConfig.toUrl());
        } else {
            logger.warn("Can't Find [{}]protocol's ServerConfig", protocol);
        }
    }

    @Override
    public Object call(URL url, CallArgs args) throws RpcException {
        List<Filter> preFilters = FilterScope.PRE.filterScope(filters);
        Invocation invocation = Invocation.create(url, args, inv -> {
            method.setAccessible(true);
            try {
                return method.invoke(remoteService().target(), args.args());
            } catch (IllegalAccessException e) {
                throw new RpcException(e);
            } catch (InvocationTargetException e) {
                throw new RpcException(e.getTargetException());
            }
        });
        Object result = filterChain.filter(invocation, preFilters);
        url.addParameters(this.url.parameters());
        return protocolInstance.createResponse(url, result);

    }

    @Override
    protected URL createUrl(URL serverUrl) {
        serverUrl.replacePaths(pathList());
        serverUrl.addParameters(parameterization());
        serverUrl.addParameter(Key.CLASS, remoteService().target().getClass().getName());
        return serverUrl;
    }

    @Override
    public RemoteService<?> remoteService() {
        return (RemoteService<?>) container();
    }

}
