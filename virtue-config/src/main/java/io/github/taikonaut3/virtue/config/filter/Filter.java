package io.github.taikonaut3.virtue.config.filter;

import io.github.taikonaut3.virtue.config.Invocation;
import io.github.taikonaut3.virtue.config.MatchRule;
import io.github.taikonaut3.virtue.config.manager.FilterManager;
import io.github.taikonaut3.virtue.config.manager.Virtue;

/**
 * Filter can interceptor rpc call
 */
public interface Filter extends MatchRule<Filter> {

    /**
     * Execute the filter logic.
     *
     * @param invocation
     * @return Should always use {@link Invocation#invoke()} to return,Then can invoke next filter
     */
    Object doFilter(Invocation invocation);

    /**
     * Returns the scope of the filter.
     * By default,
     * For the client, {@link FilterScope#PRE} means it is called before obtaining the actual address,
     * and {@link FilterScope#POST} is only supported by the client and is called after obtaining the actual address;
     * For the server, {@link FilterScope#PRE} means it is called before invoking the target method.
     *
     * @return The scope of the filter.
     */
    default FilterScope scope() {
        return FilterScope.PRE;
    }

    @Override
    default  Filter addProtocolRule(Virtue virtue, Scope scope, String... regex) {
        FilterManager manager = virtue.configManager().filterManager();
        manager.addProtocolRule(this, scope, regex);
        return this;
    }

    @Override
    default Filter addPathRule(Virtue virtue, Scope scope, String... regex) {
        FilterManager manager = virtue.configManager().filterManager();
        manager.addPathRule(this, scope, regex);
        return this;
    }
}

