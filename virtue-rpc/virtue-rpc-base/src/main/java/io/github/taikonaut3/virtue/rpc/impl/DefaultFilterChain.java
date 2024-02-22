package io.github.taikonaut3.virtue.rpc.impl;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.config.Invocation;
import io.github.taikonaut3.virtue.config.filter.Filter;
import io.github.taikonaut3.virtue.config.filter.FilterChain;

import java.util.List;

import static io.github.taikonaut3.virtue.common.constant.Components.DEFAULT;

@ServiceProvider(DEFAULT)
public class DefaultFilterChain implements FilterChain {

    @Override
    public Object filter(Invocation invocation, List<Filter> filters) {
        Invocation filterInvocation = Invocation.create(
                invocation.url(),
                invocation.callArgs(),
                Invocation::invoke
        );
        return doFilter(invocation, filterInvocation, filters, 0);
    }

    private Object doFilter(Invocation invocation, Invocation filterInvocation, List<Filter> filters, int index) {
        if (filters == null || index == filters.size()) {
            return invocation.invoke();
        }
        Filter filter = filters.get(index);
        return filter.doFilter(filterInvocation.revise(inv -> doFilter(invocation, inv, filters, index + 1)));
    }
}