package io.github.taikonaut3.virtue.rpc.listener;

import io.github.taikonaut3.virtue.common.executor.RpcThreadPool;
import io.github.taikonaut3.virtue.rpc.RpcFuture;
import io.github.taikonaut3.virtue.rpc.event.EnvelopeEventListener;
import io.github.taikonaut3.virtue.rpc.event.ResponseEvent;
import io.github.taikonaut3.virtue.transport.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class ResponseEventListener extends EnvelopeEventListener<ResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ResponseEventListener.class);

    public ResponseEventListener() {
        super(RpcThreadPool.defaultCPUExecutor("handle-response"));
    }

    @Override
    protected void handEnvelopeEvent(ResponseEvent event) {
        logger.debug("Received Event({})", event.getClass().getSimpleName());
        String id = String.valueOf(event.source().id());
        RpcFuture future = RpcFuture.getFuture(id);
        Response response = event.source();
        if (future != null) {
            future.response(response);
            Type returnType = future.returnType();
            if (returnType == response.getClass()) {
                future.complete(response);
            } else if (returnType == response.message().getClass()) {
                future.complete(response.message());
            } else {
                future.complete(event.getBody());
            }
        } else {
            logger.error("ResponseFuture({}) Can't exist", id);
        }
    }

}
