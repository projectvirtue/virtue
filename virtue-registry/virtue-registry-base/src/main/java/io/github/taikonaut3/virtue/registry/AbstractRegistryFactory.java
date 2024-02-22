package io.github.taikonaut3.virtue.registry;

import io.github.taikonaut3.virtue.common.url.URL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRegistryFactory implements RegistryFactory {

    private static final Map<String, Registry> registries = new ConcurrentHashMap<>();

    @Override
    public Registry get(URL url) {
        String uri = url.authority();
        Registry registry = registries.get(uri);
        if (registry == null) {
            registry = create(url);
            registries.put(uri, registry);
        } else {
            if (!registry.isAvailable()) {
                registry.connect(url);
            }
        }
        return registry;
    }

    protected abstract Registry create(URL url);

}
