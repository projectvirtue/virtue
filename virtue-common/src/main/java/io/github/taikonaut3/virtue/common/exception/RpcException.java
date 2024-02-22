package io.github.taikonaut3.virtue.common.exception;

public class RpcException extends RuntimeException {

    public RpcException(String msg, Throwable e) {
        super(msg, e);
    }

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(Throwable e) {
        super(e);
    }

}
