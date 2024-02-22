package io.github.taikonaut3.virtue.config.config;

import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.Parameter;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.NetUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true)
@Getter
@Setter
public class EventDispatcherConfig extends UrlTypeConfig {


    @Parameter(Key.BUFFER_SIZE)
    private int bufferSize = Constant.DEFAULT_BUFFER_SIZE;

    public EventDispatcherConfig() {
        type(Constant.DEFAULT_EVENT_DISPATCHER);
    }

    @Override
    public URL toUrl() {
        return new URL(type, NetUtil.getLocalHost(), 0, parameterization());
    }
}
