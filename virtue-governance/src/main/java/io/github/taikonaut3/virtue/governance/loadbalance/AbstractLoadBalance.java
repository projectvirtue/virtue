package io.github.taikonaut3.virtue.governance.loadbalance;

import io.github.taikonaut3.virtue.common.exception.SourceException;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.Invocation;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public URL select(Invocation invocation, List<URL> urls) {
        if (urls.isEmpty()) {
            throw new SourceException("Not available Service Urls");
        }
        if (urls.size() == 1) {
            return urls.get(0);
        }
        return doSelect(invocation, urls);
    }

    protected abstract URL doSelect(Invocation invocation, List<URL> urls);

}
