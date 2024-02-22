package io.github.taikonaut3.virtue.config.manager;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.StringUtil;
import io.github.taikonaut3.virtue.config.config.ServerConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ServerConfig Manager
 */
public class ServerConfigManager extends AbstractManager<ServerConfig> {

    private final List<String> neededOpenProtocols = new LinkedList<>();

    public ServerConfigManager(Virtue virtue) {
        super(virtue);
    }

    /**
     * Register a ServerConfig. The protocol type is unique.
     *
     * @param config
     */
    public void register(ServerConfig config) {
        register(config.type(), config);
    }

    public void neededOpenProtocol(String protocol) {
        if (!neededOpenProtocols.contains(protocol)) {
            neededOpenProtocols.add(protocol);
        }
    }

    public List<URL> neededOpenServer() {
        ArrayList<URL> urls = new ArrayList<>();
        for (String neededOpenProtocol : neededOpenProtocols) {
            ServerConfig serverConfig = get(neededOpenProtocol);
            if (serverConfig != null) {
                URL url = serverConfig.toUrl();
                url.attribute(Virtue.ATTRIBUTE_KEY).set(virtue);
                url.addParameter(Key.VIRTUE, virtue.name());
                String applicationName = virtue.applicationName();
                if (!StringUtil.isBlank(applicationName)) {
                    url.addParameter(Key.APPLICATION, applicationName);
                }
                urls.add(url);
            }
        }
        return urls;
    }
}
