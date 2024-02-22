package io.github.taikonaut3.virtue.config.manager;

import io.github.taikonaut3.virtue.common.util.StringUtil;
import io.github.taikonaut3.virtue.config.config.ClientConfig;

/**
 * ClientConfig Manager
 */
public class ClientConfigManager extends AbstractManager<ClientConfig> {

    public ClientConfigManager(Virtue virtue) {
        super(virtue);
    }

    public void register(ClientConfig clientConfig) {
        String name = StringUtil.isBlank(clientConfig.name()) ? clientConfig.type() : clientConfig.name();
        register(name, clientConfig);
    }

}
