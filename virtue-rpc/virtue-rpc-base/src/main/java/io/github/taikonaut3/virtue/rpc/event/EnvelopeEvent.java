package io.github.taikonaut3.virtue.rpc.event;

import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.event.AbstractEvent;
import io.github.taikonaut3.virtue.transport.Envelope;

public abstract class EnvelopeEvent<T extends Envelope> extends AbstractEvent<T> {

    private final URL url;

    protected EnvelopeEvent(T envelope) {
        super(envelope);
        this.url = envelope.url();

    }

    public URL getUrl() {
        return url;
    }

    public String getId() {
        return String.valueOf(source().id());
    }

}
