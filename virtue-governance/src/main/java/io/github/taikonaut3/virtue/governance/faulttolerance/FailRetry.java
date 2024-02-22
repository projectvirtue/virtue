package io.github.taikonaut3.virtue.governance.faulttolerance;

import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.taikonaut3.virtue.common.constant.Components.FaultTolerance.FAIL_RETRY;

@ServiceProvider(FAIL_RETRY)
public class FailRetry extends AbstractFaultTolerance {

    private static final Logger logger = LoggerFactory.getLogger(FailRetry.class);

    @Override
    public Object doOperation(Invocation invocation) throws RpcException {
        URL url = invocation.url();
        int retries = Integer.parseInt(url.getParameter(Key.RETRIES, String.valueOf(Constant.DEFAULT_RETIRES)));
        for (int start = 0; start <= retries; start++) {
            try {
                return invocation.invoke();
            } catch (Exception e) {
                logger.error("调用服务出现异常: " + e.getMessage() + ",开始重试第 " + start + " 次", e);
            }
        }
        throw new RpcException("调用服务出现异常,重试次数: " + retries);
    }

}
