package io.github.taikonaut3.virtue.rpc.virtue.envelope;

import io.github.taikonaut3.virtue.rpc.virtue.header.Header;
import io.github.taikonaut3.virtue.common.url.URL;

import java.io.Serializable;

/**
 * NetWork Transmission carrier
 * Comprise:
 * 1、Header Data {@link Header}
 * 2、Body Data
 */
public interface VirtueEnvelope extends Serializable {

    /**
     * Returns the header of this message.
     *
     * @return the header of the message
     */
    Header header();

    /**
     * Sets the header of this message.
     *
     * @param header the header to be set for the message
     */
    void setHeader(Header header);

    /**
     * Returns the body of this message.
     *
     * @return the body of the message
     */
    Object getBody();

    /**
     * Sets the body of this message.
     *
     * @param body the body to be set for the message
     */
    void setBody(Object body);

    /**
     * This URL may not be what you would expect
     *
     * @return
     */
    URL getUrl();

}
