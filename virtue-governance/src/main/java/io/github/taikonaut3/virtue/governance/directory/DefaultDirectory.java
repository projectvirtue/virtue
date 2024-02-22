package io.github.taikonaut3.virtue.governance.directory;

import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.CollectionUtil;
import io.github.taikonaut3.virtue.config.Invocation;
import io.github.taikonaut3.virtue.registry.Registry;
import io.github.taikonaut3.virtue.registry.RegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.taikonaut3.virtue.common.constant.Components.DEFAULT;

/**
 * must open virtue-registry
 */
@ServiceProvider(DEFAULT)
public class DefaultDirectory implements Directory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDirectory.class);

    @Override
    public List<URL> list(Invocation invocation, URL... registryConfigs) {
        logger.debug("director......");
        ArrayList<URL> remoteServiceUrls = new ArrayList<>();
        URL url = invocation.url();
        for (URL registryUrl : registryConfigs) {
            RegistryFactory registryFactory = ExtensionLoader.loadService(RegistryFactory.class, registryUrl.protocol());
            Registry registry = registryFactory.get(registryUrl);
            List<URL> discoverUrls = registry.discover(url);
            if (discoverUrls != null && !discoverUrls.isEmpty()) {
                for (URL discoverUrl : discoverUrls) {
                    CollectionUtil.addToList(remoteServiceUrls,
                            (existUrl, newUrl) -> Objects.equals(existUrl.address(), newUrl.address()),
                            discoverUrl);
                }
            }
        }
        return remoteServiceUrls;
    }

    @Override
    public void destroy() {

    }

}
