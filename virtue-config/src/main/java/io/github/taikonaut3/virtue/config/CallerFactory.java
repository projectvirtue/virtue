package io.github.taikonaut3.virtue.config;

import io.github.taikonaut3.virtue.common.spi.ServiceInterface;

import java.lang.reflect.Method;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.VIRTUE;

/**
 * CallerFactory is used to create different caller for different protocols.
 */
@ServiceInterface(VIRTUE)
public interface CallerFactory {

    /**
     * Create serverCaller.
     *
     * @param method
     * @param remoteService
     * @return serverCaller instance
     */
    ServerCaller<?> createServerCaller(Method method, RemoteService<?> remoteService);

    /**
     * Create clientCaller.
     *
     * @param method
     * @param remoteCaller
     * @return clientCaller instance
     */
    ClientCaller<?> createClientCaller(Method method, RemoteCaller<?> remoteCaller);

}
