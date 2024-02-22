package io.github.taikonaut3.virtue.rpc.virtue.envelope;

import io.github.taikonaut3.virtue.rpc.virtue.header.Header;
import lombok.ToString;

@ToString
public class VirtueRequest extends AbstractVirtueEnvelope {

    public VirtueRequest() {

    }

    public VirtueRequest(Header header, Object body) {
        super(header, body);
    }

}
