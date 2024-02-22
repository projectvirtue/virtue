package io.github.taikonaut3.virtue.config.manager;

import io.github.taikonaut3.virtue.common.util.StringUtil;
import io.github.taikonaut3.virtue.config.ClientCaller;
import io.github.taikonaut3.virtue.config.ServerCaller;
import io.github.taikonaut3.virtue.config.config.RegistryConfig;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RegistryConfig Manager
 */
public class RegistryConfigManager extends AbstractRuleManager<RegistryConfig> {

    public RegistryConfigManager(Virtue virtue) {
        super(virtue);
    }

    public List<RegistryConfig> globalConfigs() {
        return getManagerMap().values().stream().
                filter(RegistryConfig::global)
                .collect(Collectors.toList());
    }

    public void register(RegistryConfig registryConfig) {
        String name = StringUtil.isBlank(registryConfig.name()) ? registryConfig.type() : registryConfig.name();
        register(name, registryConfig);
    }

    @Override
    protected void doExecuteRules(RegistryConfig config, List<ServerCaller<?>> matchedServerCallers, List<ClientCaller<?>> matchedClientCallers) {
        matchedClientCallers.forEach(ClientCaller::addRegistryConfig);
    }

}
