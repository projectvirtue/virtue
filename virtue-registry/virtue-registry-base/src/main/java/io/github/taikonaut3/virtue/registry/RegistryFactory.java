package io.github.taikonaut3.virtue.registry;

import io.github.taikonaut3.virtue.common.spi.ServiceInterface;
import io.github.taikonaut3.virtue.common.url.ServiceFactory;

import static io.github.taikonaut3.virtue.common.constant.Components.Registry.CONSUL;

/**
 * Represents a registry factory for creating registry instances.
 */
@ServiceInterface(CONSUL)
public interface RegistryFactory extends ServiceFactory<Registry> {

}


