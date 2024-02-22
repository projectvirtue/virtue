package io.github.taikonaut3.virtue.transport.codec;

import io.github.taikonaut3.virtue.common.exception.CodecException;

/**
 * Encoding and Decoding network message.
 */
public interface Codec {

    /**
     * Encodes the message into a byte array.
     *
     * @param message The message to be encoded.
     * @return The encoded byte array.
     * @throws CodecException
     */
    byte[] encode(Object message) throws CodecException;

    /**
     * Decodes the  byte array into a message.
     *
     * @param bytes The byte array to be decoded.
     * @return The decoded message.
     * @throws CodecException
     */
    Object decode(byte[] bytes) throws CodecException;

}