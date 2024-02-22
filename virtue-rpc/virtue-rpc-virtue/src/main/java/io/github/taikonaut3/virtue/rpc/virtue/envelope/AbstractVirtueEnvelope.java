package io.github.taikonaut3.virtue.rpc.virtue.envelope;

import io.github.taikonaut3.virtue.rpc.virtue.header.Header;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;

import java.io.Serial;

public abstract class AbstractVirtueEnvelope implements VirtueEnvelope {

    @Serial
    private static final long serialVersionUID = 1L;

    private Header header;

    private Object body;

    private URL url;

    public AbstractVirtueEnvelope() {
    }

    public AbstractVirtueEnvelope(Header header, Object body) {
        setHeader(header);
        setBody(body);
    }

    @Override
    public Header header() {
        return header;
    }

    @Override
    public void setHeader(Header header) {
        this.header = header;
        url = URL.valueOf(header.getExtendData(Key.URL));
    }

    @Override
    public Object getBody() {
        return body;
    }

    @Override
    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    public String getId() {
        return header.getExtendData(Key.UNIQUE_ID);
    }

    public void setId(String id) {
        header.addExtendData(Key.UNIQUE_ID, String.valueOf(id));
    }

    @Override
    public String toString() {
        return getId();
    }

}
