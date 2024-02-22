package io.github.taikonaut3.virtue.config.annotation;

import io.github.taikonaut3.virtue.common.constant.Components;

import java.lang.annotation.*;

/**
 * Common config for the client and the server.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Config {

    /**
     * The group.
     */
    String group() default "";

    /**
     * The version.
     */
    String version() default "";

    /**
     * Serialization type.
     */
    String serialize() default Components.Serialize.KRYO;

    String compression() default Components.Compression.GZIP;

    /**
     * Invoke Filter.
     * @see io.github.taikonaut3.virtue.config.filter.Filter
     */
    String[] filters() default {"jjj"};

    /**
     * The filter chain for {@link io.github.taikonaut3.virtue.config.Caller}
     */
    String filterChain() default Components.DEFAULT;
}

