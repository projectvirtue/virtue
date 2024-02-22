package io.github.taikonaut3.virtue.rpc.protocol;

import io.github.taikonaut3.virtue.config.CallArgs;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.Response;

public interface ProtocolParser {

    CallArgs parseRequestBody(Request request);

    Object parseResponseBody(Response response);

}
