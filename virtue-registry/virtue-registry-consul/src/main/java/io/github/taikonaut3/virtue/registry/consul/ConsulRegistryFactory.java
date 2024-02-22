package io.github.taikonaut3.virtue.registry.consul;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.registry.AbstractRegistryFactory;
import io.github.taikonaut3.virtue.registry.Registry;

import static io.github.taikonaut3.virtue.common.constant.Components.Registry.CONSUL;

/**
 * Use Vertx-Consul-Client
 */
@ServiceProvider(CONSUL)
public class ConsulRegistryFactory extends AbstractRegistryFactory {

    @Override
    protected Registry create(URL url) {
        return new ConsulRegistry(url);
    }

}
