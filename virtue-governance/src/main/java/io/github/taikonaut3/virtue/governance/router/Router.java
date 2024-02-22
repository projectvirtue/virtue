package io.github.taikonaut3.virtue.governance.router;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.extension.AttributeKey;
import io.github.taikonaut3.virtue.common.spi.ServiceInterface;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.Invocation;

import java.util.List;

import static io.github.taikonaut3.virtue.common.constant.Components.DEFAULT;

/**
 * Router interface.
 */
@ServiceInterface(DEFAULT)
public interface Router {

    AttributeKey<Router> ATTRIBUTE_KEY = AttributeKey.get(Key.ROUTER);

    /**
     * Routes the list of URLs based on the given call arguments.
     *
     * @param invocation the invocation to be performed
     * @return the list of routed urls.
     */
    List<URL> route(Invocation invocation, List<URL> urls);
}

