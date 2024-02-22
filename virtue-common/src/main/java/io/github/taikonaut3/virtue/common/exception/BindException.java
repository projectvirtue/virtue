package io.github.taikonaut3.virtue.common.exception;

public class BindException extends NetWorkException {

    public BindException(String msg, Throwable e) {
        super(msg, e);
    }

    public BindException(String msg) {
        super(msg);
    }

    public BindException(Throwable msg) {
        super(msg);
    }

}
