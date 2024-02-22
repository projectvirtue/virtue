package io.github.taikonaut3.virtue.common.exception;

public class CodecException extends NetWorkException {

    public CodecException(String msg, Throwable e) {
        super(msg, e);
    }

    public CodecException(String msg) {
        super(msg);
    }

    public CodecException(Throwable msg) {
        super(msg);
    }

}
