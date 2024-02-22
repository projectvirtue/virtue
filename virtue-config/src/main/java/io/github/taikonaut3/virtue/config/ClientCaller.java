package io.github.taikonaut3.virtue.config;

import io.github.taikonaut3.virtue.common.url.URL;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Client caller for making remote service calls.
 *
 * @param <T> The type of annotation used to specify the remote service.
 */
public interface ClientCaller<T extends Annotation> extends Caller<T>, DirectRemoteCall, CallOptions {

    /**
     * Gets the remote caller associated with this client caller.
     *
     * @return remote caller instance
     */
    RemoteCaller<?> remoteCaller();

    /**
     * Convert RegistryConfig to URL
     *
     * @return all registry url config
     */
    List<URL> registryConfigUrls();
}

