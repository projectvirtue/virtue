package io.github.taikonaut3.virtue.config.manager;

import io.github.taikonaut3.virtue.common.util.GenerateUtil;
import io.github.taikonaut3.virtue.config.RemoteService;
import io.github.taikonaut3.virtue.config.ServerCaller;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RemoteService Manager
 */
public class RemoteServiceManager extends AbstractManager<RemoteService<?>> {

    public RemoteServiceManager(Virtue virtue) {
        super(virtue);
    }

    public Collection<RemoteService<?>> remoteServices() {
        return map.values();
    }

    public void register(RemoteService<?> remoteService) {
        register(remoteService.name(), remoteService);
    }

    public ServerCaller<?> getServerCaller(String protocol, String path) {
        return getServerCaller(GenerateUtil.generateCallerIdentification(protocol, path));
    }

    public ServerCaller<?> getServerCaller(String identification) {
        for (RemoteService<?> remoteService : remoteServices()) {
            ServerCaller<?> caller = (ServerCaller<?>) remoteService.getCaller(identification);
            if (caller != null) {
                return caller;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> RemoteService<T> get(Class<T> interfaceType) {
        if (interfaceType.isAnnotationPresent(io.github.taikonaut3.virtue.config.annotation.RemoteService.class)) {
            io.github.taikonaut3.virtue.config.annotation.RemoteService annotation = interfaceType.getAnnotation(io.github.taikonaut3.virtue.config.annotation.RemoteService.class);
            return (RemoteService<T>) get(annotation.value());

        }
        return null;
    }

    public List<ServerCaller<?>> serverCallers() {
        return remoteServices().stream()
                .flatMap(container -> Arrays.stream(container.callers()).map(caller -> (ServerCaller<?>) caller))
                .collect(Collectors.toList());
    }

}
