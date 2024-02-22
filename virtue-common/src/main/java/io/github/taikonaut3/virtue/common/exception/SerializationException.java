package io.github.taikonaut3.virtue.common.exception;

public class SerializationException extends RpcException {

    public SerializationException(String msg, Throwable e) {
        super(msg, e);
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(Throwable msg) {
        super(msg);
    }

}
