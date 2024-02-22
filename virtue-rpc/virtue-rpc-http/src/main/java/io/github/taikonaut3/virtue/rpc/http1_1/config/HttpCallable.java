package io.github.taikonaut3.virtue.rpc.http1_1.config;

import io.github.taikonaut3.virtue.config.annotation.CallerFactoryProvider;
import io.github.taikonaut3.virtue.config.annotation.Config;

import java.lang.annotation.*;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.HTTP;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CallerFactoryProvider(HTTP)
public @interface HttpCallable {

    /**
     * The path of the HTTP request.
     */
    String path() default "";

    /**
     * The method of the HTTP request.
     */
    String method() default "";

    /**
     * The headers of the HTTP request.
     */
    String[] headers() default {};

    /**
     * The query parameters of the HTTP request.
     */
    String[] params() default {};

    Config config() default @Config;
}
