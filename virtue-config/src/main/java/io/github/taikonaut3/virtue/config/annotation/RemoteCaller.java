package io.github.taikonaut3.virtue.config.annotation;

import io.github.taikonaut3.virtue.common.constant.Components;

import java.lang.annotation.*;

/**
 * Indicates a remote caller,
 * Used on interface, remoteCaller can create a proxy for that interface to perform remote invocations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoteCaller {

    /**
     * The remoteApplication name
     */
    String value();

    /**
     * The proxy type
     */
    String proxy() default Components.ProxyFactory.JDK;

    /**
     * Is it only when the first call is made that the registration center is actually connected to Get the available services.
     * The default gets available services when {@link io.github.taikonaut3.virtue.config.ClientCaller} creation is complete.
     */
    boolean lazyDiscover() default false;

}
