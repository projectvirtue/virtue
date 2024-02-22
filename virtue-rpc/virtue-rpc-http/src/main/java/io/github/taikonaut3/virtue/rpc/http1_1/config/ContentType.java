package io.github.taikonaut3.virtue.rpc.http1_1.config;

import io.netty.handler.codec.http.HttpHeaderValues;

/**
 * Http Content-Type
 */
public interface ContentType {
    String APPLICATION_JSON = HttpHeaderValues.APPLICATION_JSON.toString();
    String APPLICATION_X_WWW_FORM_URLENCODED = HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString();
    String MULTIPART_FORM_DATA = HttpHeaderValues.MULTIPART_FORM_DATA.toString();
    String TEXT_PLAIN = HttpHeaderValues.TEXT_PLAIN.toString();

    String APPLICATION_OCTET_STREAM = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString();
}

