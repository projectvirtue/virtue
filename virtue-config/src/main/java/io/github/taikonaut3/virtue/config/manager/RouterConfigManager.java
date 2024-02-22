package io.github.taikonaut3.virtue.config.manager;

import io.github.taikonaut3.virtue.config.config.RouterConfig;
import org.intellij.lang.annotations.Language;

/**
 * RouterConfigManager
 */
public class RouterConfigManager extends AbstractManager<RouterConfig> {
    protected RouterConfigManager(Virtue virtue) {
        super(virtue);
    }

    public void register(@Language("RegExp") String urlRegex, @Language("RegExp") String targetRegex) {
        RouterConfig routerConfig = new RouterConfig(urlRegex).match(targetRegex);
        register(urlRegex, routerConfig);
    }
}
