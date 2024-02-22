package io.github.taikonaut3.virtue.common.exception;


public class CompressionException extends RpcException{

    public CompressionException(String msg, Throwable e) {
        super(msg, e);
    }

    public CompressionException(String msg) {
        super(msg);
    }

    public CompressionException(Throwable e) {
        super(e);
    }
}
