package io.github.taikonaut3.virtue.transport.byteutils;

/**
 * Tool for Reading bytes
 */
public interface ByteReader {

    static ByteReader newReader(byte[] bytes) {
        return new DefaultByteReader(bytes);
    }

    byte readByte();

    boolean readBoolean();

    short readShort();

    int readInt();

    long readLong();

    float readFloat();

    double readDouble();

    CharSequence readCharSequence(int length);

    byte[] readBytes(int length);

    int readableBytes();

}