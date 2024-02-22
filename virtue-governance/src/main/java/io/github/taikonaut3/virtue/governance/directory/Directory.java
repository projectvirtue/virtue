package io.github.taikonaut3.virtue.governance.directory;

import io.github.taikonaut3.virtue.common.spi.ServiceInterface;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.Invocation;

import java.util.List;

import static io.github.taikonaut3.virtue.common.constant.Components.DEFAULT;

/**
 * Directory for listing available services from the registry.
 */
@ServiceInterface(DEFAULT)
public interface Directory {

    /**
     * Lists all available services from the registry.
     *
     * @param invocation      the invocation to be used for the service lookup
     * @param registryConfigs the registry configurations to be used for the service lookup
     * @return a list of urls representing the available services
     */
    List<URL> list(Invocation invocation, URL... registryConfigs);

    /**
     * Destroys the cache used by the directory.
     */
    void destroy();
}

