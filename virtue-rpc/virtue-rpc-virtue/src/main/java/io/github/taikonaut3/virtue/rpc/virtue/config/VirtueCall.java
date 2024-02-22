package io.github.taikonaut3.virtue.rpc.virtue.config;

import io.github.taikonaut3.virtue.config.annotation.CallerFactoryProvider;
import io.github.taikonaut3.virtue.config.annotation.Config;
import io.github.taikonaut3.virtue.config.annotation.Options;

import java.lang.annotation.*;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.VIRTUE;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CallerFactoryProvider(VIRTUE)
public @interface VirtueCall {

    String service() default "";

    String callMethod() default "";

    Options options() default @Options;

    Config config() default @Config;
}
