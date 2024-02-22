package io.github.taikonaut3.virtue.rpc.virtue.envelope;

import io.github.taikonaut3.virtue.rpc.virtue.header.Header;
import lombok.ToString;

@ToString
public class VirtueResponse extends AbstractVirtueEnvelope {

    public VirtueResponse() {

    }

    public VirtueResponse(Header header, Object payload) {
        super(header, payload);
    }

}
