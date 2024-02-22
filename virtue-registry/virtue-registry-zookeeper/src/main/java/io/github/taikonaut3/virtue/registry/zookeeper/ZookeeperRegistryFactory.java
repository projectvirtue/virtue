package io.github.taikonaut3.virtue.registry.zookeeper;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.registry.AbstractRegistryFactory;
import io.github.taikonaut3.virtue.registry.Registry;

import static io.github.taikonaut3.virtue.common.constant.Components.Registry.ZOOKEEPER;

@ServiceProvider(ZOOKEEPER)
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    @Override
    protected Registry create(URL url) {
        return null;
        // return new ZookeeperRegistry(url);
    }

}
