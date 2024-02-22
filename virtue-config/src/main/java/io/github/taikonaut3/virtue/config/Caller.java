package io.github.taikonaut3.virtue.config;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.GenerateUtil;
import io.github.taikonaut3.virtue.config.manager.Virtue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Method caller wrapper for the client method and server method.
 */
public interface Caller<T extends Annotation> extends CommonConfig, Lifecycle {

    /**
     * The associated annotation instance.
     *
     * @return annotation instance
     */
    T parsedAnnotation();

    /**
     * The protocol.
     *
     * @return current call protocol
     */
    String protocol();

    /**
     * Call url config.
     *
     * @return by method created url
     */
    URL url();

    /**
     * All proxy clients call the entry.
     *
     * @param args
     * @throws RpcException
     * @return method return
     */
    Object call(URL url, CallArgs args) throws RpcException;

    /**
     * The client interface call method.
     */
    Method method();

    /**
     * The client interface returnType.
     *
     * @return method return type
     */
    Type returnType();

    /**
     * The client interface returnClass.
     *
     * @return method return class
     */
    Class<?> returnClass();

    /**
     * Container holding the caller
     *
     * @return container instance
     */
    CallerContainer container();

    /**
     * Gets the path as list associated with the server caller.
     *
     * @return The path as list
     */
    List<String> pathList();

    /**
     * Gets the path associated with the server caller.
     *
     * @return The path
     */
    default String path() {
        return URL.toPath(pathList());
    }

    /**
     * Caller unique identification.
     *
     * @return unique identification
     */
    default String identification() {
        return GenerateUtil.generateCallerIdentification(protocol(), path());
    }

    /**
     * The belong to virtue
     *
     * @return virtue instance
     */
    default Virtue virtue() {
        return container().virtue();
    }

}
