package io.github.taikonaut3.virtue.rpc.virtue;

import io.github.taikonaut3.virtue.rpc.virtue.envelope.VirtueRequest;
import io.github.taikonaut3.virtue.rpc.virtue.envelope.VirtueResponse;
import io.github.taikonaut3.virtue.rpc.protocol.ProtocolParser;
import io.github.taikonaut3.virtue.config.CallArgs;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.Response;

/**
 * VirtueProtocolParser
 */
public class VirtueProtocolParser implements ProtocolParser {

    @Override
    public CallArgs parseRequestBody(Request request) {
        VirtueRequest virtueRequest = (VirtueRequest) request.message();
        return (CallArgs) virtueRequest.getBody();
    }

    @Override
    public Object parseResponseBody(Response response) {
        VirtueResponse virtueResponse = (VirtueResponse) response.message();
        return virtueResponse.getBody();
    }
}
