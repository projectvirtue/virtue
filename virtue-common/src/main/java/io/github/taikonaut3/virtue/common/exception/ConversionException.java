package io.github.taikonaut3.virtue.common.exception;

public class ConversionException extends RpcException {

    public ConversionException(String msg, Throwable e) {
        super(msg, e);
    }

    public ConversionException(String msg) {
        super(msg);
    }

    public ConversionException(Throwable msg) {
        super(msg);
    }

}
