package io.github.taikonaut3.virtue.config;

import io.github.taikonaut3.virtue.common.url.URL;

import java.util.function.Function;

/**
 * The basic Invocation implementation class
 */
public class CallInvocation implements Invocation {

    private final CallArgs args;

    private URL url;

    private Function<Invocation, Object> invoke;

    public CallInvocation(URL url, CallArgs args, Function<Invocation, Object> invoke) {
        this.url = url;
        this.args = args;
        this.invoke = invoke;
    }

    @Override
    public URL url() {
        return url;
    }

    @Override
    public CallArgs callArgs() {
        return args;
    }

    @Override
    public Object invoke() {
        if (invoke != null) {
            return invoke.apply(this);
        }
        return null;
    }

    @Override
    public Invocation revise(URL url) {
        if (url != null) this.url = url;
        return this;
    }

    @Override
    public Invocation revise(Function<Invocation, Object> invoke) {
        this.invoke = invoke;
        return this;
    }

    @Override
    public Invocation revise(URL url, Function<Invocation, Object> invoke) {
        revise(url).revise(invoke);
        return this;
    }
}
