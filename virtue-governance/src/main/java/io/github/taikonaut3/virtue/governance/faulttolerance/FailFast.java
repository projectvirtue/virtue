package io.github.taikonaut3.virtue.governance.faulttolerance;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.config.Invocation;

import static io.github.taikonaut3.virtue.common.constant.Components.FaultTolerance.FAIL_FAST;

@ServiceProvider(FAIL_FAST)
public class FailFast extends AbstractFaultTolerance{
    @Override
    protected Object doOperation(Invocation invocation) throws RpcException {
        try {
            return invocation.invoke();
        } catch (Exception e) {
            throw new RpcException(e);
        }
    }
}
