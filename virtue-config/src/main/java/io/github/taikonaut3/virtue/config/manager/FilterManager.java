package io.github.taikonaut3.virtue.config.manager;

import io.github.taikonaut3.virtue.config.ClientCaller;
import io.github.taikonaut3.virtue.config.ServerCaller;
import io.github.taikonaut3.virtue.config.filter.Filter;

import java.util.List;

/**
 * Filter Manager
 */
public class FilterManager extends AbstractRuleManager<Filter> {

    public FilterManager(Virtue virtue) {
        super(virtue);
    }

    @Override
    protected void doExecuteRules(Filter filter, List<ServerCaller<?>> matchedServerCallers, List<ClientCaller<?>> matchedClientCallers) {
        matchedServerCallers.forEach(ServerCaller::addFilter);
        matchedClientCallers.forEach(ClientCaller::addFilter);
    }

}
