package io.github.taikonaut3.filter;

import io.github.taikonaut3.virtue.common.constant.Components;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.Invocation;
import io.github.taikonaut3.virtue.config.filter.Filter;
import org.springframework.stereotype.Component;

@Component
public class Filter2 implements Filter {

    @Override
    public Object doFilter(Invocation invocation) {
        System.out.println("Filter222222");
        System.out.println(invocation.callArgs().caller().url());
        URL url = invocation.url();
        Object arg = invocation.callArgs().args()[0];
        if (arg.equals("world")) {
            url.addParameter(Key.FAULT_TOLERANCE, Components.FaultTolerance.FAIL_RETRY);
        }
        return invocation.invoke();
    }
}
