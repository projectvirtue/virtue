package io.github.taikonaut3.virtue.config.config;

import io.github.taikonaut3.virtue.common.constant.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static io.github.taikonaut3.virtue.common.constant.Components.DEFAULT;
import static io.github.taikonaut3.virtue.common.constant.Components.Transport.NETTY;

@Accessors(fluent = true, chain = true)
@Setter
@Getter
public class ApplicationConfig {

    private String applicationName;

    private int weight = Constant.DEFAULT_WEIGHT;

    private String transport = NETTY;

    private String router = DEFAULT;

    private EventDispatcherConfig eventDispatcherConfig = new EventDispatcherConfig();

}
