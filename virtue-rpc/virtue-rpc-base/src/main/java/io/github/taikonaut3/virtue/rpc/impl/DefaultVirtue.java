package io.github.taikonaut3.virtue.rpc.impl;

import io.github.taikonaut3.virtue.common.extension.AbstractAccessor;
import io.github.taikonaut3.virtue.common.extension.RpcContext;
import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.config.RemoteCaller;
import io.github.taikonaut3.virtue.config.RemoteService;
import io.github.taikonaut3.virtue.config.Scheduler;
import io.github.taikonaut3.virtue.config.VirtueConfiguration;
import io.github.taikonaut3.virtue.config.config.ApplicationConfig;
import io.github.taikonaut3.virtue.config.config.EventDispatcherConfig;
import io.github.taikonaut3.virtue.config.manager.ConfigManager;
import io.github.taikonaut3.virtue.config.manager.MonitorManager;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import io.github.taikonaut3.virtue.event.EventDispatcher;
import io.github.taikonaut3.virtue.event.EventDispatcherFactory;
import io.github.taikonaut3.virtue.governance.router.Router;
import io.github.taikonaut3.virtue.rpc.ComplexRemoteCaller;
import io.github.taikonaut3.virtue.rpc.ComplexRemoteService;
import io.github.taikonaut3.virtue.transport.Transporter;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.taikonaut3.virtue.common.constant.Components.DEFAULT;

/**
 * Virtue application config
 * manage each config component
 */
@Accessors(fluent = true)
@Getter
@ServiceProvider(DEFAULT)
public class DefaultVirtue extends AbstractAccessor implements Virtue {

    private final ConfigManager configManager;
    private final MonitorManager monitorManager;
    private final List<VirtueConfiguration> configurations;
    private final Scheduler scheduler;
    private String name;
    private EventDispatcher eventDispatcher;

    public DefaultVirtue() {
        name(DEFAULT);
        configManager = new ConfigManager(this);
        monitorManager = new MonitorManager();
        configurations = ExtensionLoader.loadServices(VirtueConfiguration.class);
        scheduler = ExtensionLoader.loadService(Scheduler.class);
        init();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Virtue name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public <T> Virtue proxy(Class<T> interfaceType) {
        ComplexRemoteCaller<T> remoteCaller = new ComplexRemoteCaller<>(this, interfaceType);
        register(remoteCaller);
        return this;
    }

    @Override
    public <T> Virtue wrap(T target) {
        ComplexRemoteService<T> remoteService = new ComplexRemoteService<>(this, target);
        register(remoteService);
        return this;
    }

    @Override
    public void init() {
        RpcContext.getContext().attribute(ATTRIBUTE_KEY).set(this);
        for (VirtueConfiguration configuration : configurations) {
            configuration.initBefore(this);
        }
        initApplicationComponents();
        for (VirtueConfiguration configuration : configurations) {
            configuration.initAfter(this);
        }
    }

    private void initApplicationComponents() {
        ApplicationConfig applicationConfig = configManager.applicationConfig();
        EventDispatcherConfig eventDispatcherConfig = applicationConfig.eventDispatcherConfig();
        EventDispatcherFactory eventDispatcherFactory = ExtensionLoader.loadService(EventDispatcherFactory.class, eventDispatcherConfig.type());
        eventDispatcher = eventDispatcherFactory.get(eventDispatcherConfig.toUrl());
        Router router = ExtensionLoader.loadService(Router.class, applicationConfig.router());
        Transporter transporter = ExtensionLoader.loadService(Transporter.class, applicationConfig.transport());
        attribute(Router.ATTRIBUTE_KEY).set(router);
        attribute(Transporter.ATTRIBUTE_KEY).set(transporter);
    }

    public synchronized void start() {
        RpcContext.getContext().attribute(ATTRIBUTE_KEY).set(this);
        for (VirtueConfiguration configuration : configurations) {
            configuration.startBefore(this);
        }
        configManager.filterManager().executeRules();
        configManager.registryConfigManager().executeRules();
        for (RemoteService<?> remoteService : configManager().remoteServiceManager().remoteServices()) {
            remoteService.start();
        }
        for (RemoteCaller<?> remoteCaller : configManager().remoteCallerManager().remoteCallers()) {
            remoteCaller.start();
        }
        for (VirtueConfiguration configuration : configurations) {
            configuration.startAfter(this);
        }
    }

    @Override
    public void stop() {
        RpcContext.getContext().attribute(ATTRIBUTE_KEY).set(this);
        for (VirtueConfiguration configuration : configurations) {
            configuration.stopBefore(this);
        }

        for (VirtueConfiguration configuration : configurations) {
            configuration.stopAfter(this);
        }
    }

}
