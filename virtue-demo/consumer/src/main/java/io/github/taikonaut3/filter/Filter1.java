package io.github.taikonaut3.filter;

import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.Invocation;
import io.github.taikonaut3.virtue.config.filter.Filter;
import io.github.taikonaut3.virtue.config.filter.FilterScope;
import org.springframework.stereotype.Component;

@Component
public class Filter1 implements Filter {

    @Override
    public Object doFilter(Invocation invocation) {
        URL url = invocation.url();
        System.out.println("filter1111111");
        System.out.println(url);
        return invocation.invoke();
    }

    @Override
    public FilterScope scope() {
        return FilterScope.POST;
    }
}
