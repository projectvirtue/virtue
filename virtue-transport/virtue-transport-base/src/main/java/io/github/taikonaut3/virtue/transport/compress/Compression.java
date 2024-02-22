package io.github.taikonaut3.virtue.transport.compress;

import io.github.taikonaut3.virtue.common.exception.CompressionException;
import io.github.taikonaut3.virtue.common.spi.ServiceInterface;

import static io.github.taikonaut3.virtue.common.constant.Components.Compression.GZIP;

/**
 * Compression interface.
 */
@ServiceInterface(GZIP)
public interface Compression {

    /**
     * compress date
     *
     * @param data
     * @return
     * @throws CompressionException
     */
    byte[] compress(byte[] data) throws CompressionException;

    /**
     * decompress compressedData
     *
     * @param compressedData
     * @return
     * @throws CompressionException
     */
    byte[] decompress(byte[] compressedData) throws CompressionException;
}
