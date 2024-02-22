package io.github.taikonaut3.virtue.registry.nacos;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.registry.AbstractRegistryFactory;
import io.github.taikonaut3.virtue.registry.Registry;

import static io.github.taikonaut3.virtue.common.constant.Components.Registry.NACOS;

/**
 * Use Nacoe-Client
 */
@ServiceProvider(NACOS)
public class NacosRegistryFactory extends AbstractRegistryFactory {
    @Override
    protected Registry create(URL url) {
        return new NacosRegistry(url);
    }
}
