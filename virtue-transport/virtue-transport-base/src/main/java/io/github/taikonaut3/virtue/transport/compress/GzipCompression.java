package io.github.taikonaut3.virtue.transport.compress;

import io.github.taikonaut3.virtue.common.exception.CompressionException;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static io.github.taikonaut3.virtue.common.constant.Components.Compression.GZIP;

@ServiceProvider(GZIP)
public class GzipCompression implements Compression {

    @Override
    public byte[] compress(byte[] data) throws CompressionException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutput = new GZIPOutputStream(baos)) {
            gzipOutput.write(data);
        } catch (IOException e) {
            throw new CompressionException(e);
        }
        return baos.toByteArray();
    }

    @Override
    public byte[] decompress(byte[] compressedData) throws CompressionException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gzipInput = new GZIPInputStream(new ByteArrayInputStream(compressedData))) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInput.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new CompressionException(e);
        }
        return baos.toByteArray();
    }
}
