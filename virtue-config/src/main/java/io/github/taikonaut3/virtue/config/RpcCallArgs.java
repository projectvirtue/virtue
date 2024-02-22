package io.github.taikonaut3.virtue.config;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * The basic CallArgs implementation class
 */
@Data
@Accessors(fluent = true, chain = true)
public class RpcCallArgs implements CallArgs, Serializable {

    private Object[] args;

    private transient Type returnType;

    private transient Type[] parameterTypes;

    private transient Caller<?> caller;

    public RpcCallArgs() {

    }

    public RpcCallArgs(Caller<?> caller, Object[] args) {
        this.args = args;
        this.caller = caller;
        this.returnType = caller.returnType();
        this.parameterTypes = caller.method().getGenericParameterTypes();

    }

    public Object[] getArgs() {
        return args();
    }

    public void setArgs(Object[] args) {
        args(args);
    }
}
