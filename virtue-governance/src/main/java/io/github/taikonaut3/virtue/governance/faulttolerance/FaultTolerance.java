package io.github.taikonaut3.virtue.governance.faulttolerance;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.spi.ServiceInterface;
import io.github.taikonaut3.virtue.config.Invocation;

import static io.github.taikonaut3.virtue.common.constant.Components.FaultTolerance.FAIL_RETRY;

/**
 * Represents a fault tolerance interface.
 */
@ServiceInterface(FAIL_RETRY)
public interface FaultTolerance {

    /**
     * Performs an operation with fault tolerance.
     *
     * @param invocation the invocation to be performed.
     * @return use invocation.invoke()
     * @throws RpcException
     */
    Object operation(Invocation invocation) throws RpcException;
}

