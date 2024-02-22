package io.github.taikonaut3.virtue.config.config;

import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.Parameter;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.MatchRule;
import io.github.taikonaut3.virtue.config.manager.RegistryConfigManager;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true)
@Getter
@Setter
public class RegistryConfig extends UrlTypeConfig implements MatchRule<RegistryConfig> {

    @Parameter(Key.GLOBAL)
    private boolean global = true;

    @Parameter(Key.USERNAME)
    private String username;

    @Parameter(Key.PASSWORD)
    private String password;

    @Parameter(Key.ENABLE_HEALTH_CHECK)
    private boolean enableHealthCheck = true;

    @Parameter(Key.CONNECT_TIMEOUT)
    private int connectTimeout = Constant.DEFAULT_CONNECT_TIMEOUT;

    @Parameter(Key.SESSION_TIMEOUT)
    private int sessionTimeout = Constant.DEFAULT_SESSION_TIMEOUT;

    @Parameter(Key.RETRIES)
    private int retries = Constant.DEFAULT_RETIRES;

    @Parameter(Key.RETRY_INTERVAL)
    private int retryInterval = Constant.DEFAULT_INTERVAL;

    @Parameter(Key.HEALTH_CHECK_INTERVAL)
    private int healthCheckInterval = Constant.DEFAULT_HEALTH_CHECK_INTERVAL;

    public RegistryConfig() {

    }

    public RegistryConfig(String type, String address) {
        type(type);
        address(address);
    }

    public RegistryConfig(String authority) {
        URL url = URL.valueOf(authority);
        type(url.protocol());
        address(url.address());
    }

    @Override
    public URL toUrl() {
        return new URL(type, host, port, parameterization());
    }

    @Override
    public RegistryConfig addProtocolRule(Virtue virtue, Scope scope, String... regex) {
        RegistryConfigManager manager = virtue.configManager().registryConfigManager();
        manager.addProtocolRule(this, scope, regex);
        return this;
    }

    @Override
    public RegistryConfig addPathRule(Virtue virtue, Scope scope, String... regex) {
        RegistryConfigManager manager = virtue.configManager().registryConfigManager();
        manager.addPathRule(this, scope, regex);
        return this;
    }
}
