package io.github.taikonaut3.virtue.config;

import io.github.taikonaut3.virtue.config.annotation.Options;
import io.github.taikonaut3.virtue.config.config.RegistryConfig;

import java.util.List;

/**
 * Mapping {@link io.github.taikonaut3.virtue.config.annotation.Options}
 */
public interface CallOptions extends CommonConfig {

    /**
     * Supports async call,The default is false.
     * If true,Then the returnType should be {@link java.util.concurrent.CompletableFuture}
     */
    boolean async();

    /**
     * The default is false,Unit: ms.
     *
     * @return The timeout value
     */
    int timeout();

    /**
     * Used {@link io.github.taikonaut3.virtue.governance.directory.Directory}.
     * Default is "default" {@link io.github.taikonaut3.virtue.governance.directory.DefaultDirectory}
     */
    String directory();

    /**
     * Used {@link io.github.taikonaut3.virtue.governance.router.Router}.
     * Default is "weight" {@link io.github.taikonaut3.virtue.governance.router.WeightRouter}
     */
    String router();

    /**
     * Used {@link io.github.taikonaut3.virtue.governance.loadbalance.LoadBalance}.
     * Default is "random" {@link io.github.taikonaut3.virtue.governance.loadbalance.RandomLoadBalance}.
     */
    String loadBalance();

    /**
     * Used {@link io.github.taikonaut3.virtue.governance.faulttolerance.FaultTolerance}.
     * Default is "failFast" {@link io.github.taikonaut3.virtue.governance.faulttolerance.FailFast}.
     */
    String faultTolerance();

    /**
     * If {@link Options#faultTolerance()} is "failRetry",
     * Number of retries called when an exception occurred.
     */
    int retires();

    /**
     * Gets the direct URL for making the remote service call.
     *
     * @return the direct url
     */
    String directUrl();

    /**
     * Determines whether to reuse the client configuration from the current protocol.
     */
    boolean multiplex();

    /**
     * Gets the client config for the call.
     *
     * @return clientConfig instance
     */
    String clientConfig();

    /**
     * Add RegistryConfigs
     *
     * @param configs
     */
    void addRegistryConfig(RegistryConfig... configs);

    /**
     * Gets a list of registry configurations from the {@link Options#registries()}.
     */
    List<RegistryConfig> registryConfigs();

}

