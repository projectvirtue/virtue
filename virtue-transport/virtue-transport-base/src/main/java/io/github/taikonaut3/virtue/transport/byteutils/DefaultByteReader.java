package io.github.taikonaut3.virtue.transport.byteutils;

import java.nio.charset.StandardCharsets;

public final class DefaultByteReader implements ByteReader {

    private final byte[] buffer;

    private int position;

    public DefaultByteReader(byte[] buffer) {
        this.buffer = buffer;
        this.position = 0;
    }

    @Override
    public byte readByte() {
        return buffer[position++];
    }

    @Override
    public boolean readBoolean() {
        return buffer[position++] != 0;
    }

    @Override
    public short readShort() {
        return (short) ((buffer[position++] << 8) | (buffer[position++] & 0xff));
    }

    @Override
    public int readInt() {
        return (buffer[position++] << 24)
                | ((buffer[position++] & 0xff) << 16)
                | ((buffer[position++] & 0xff) << 8)
                | (buffer[position++] & 0xff);
    }

    @Override
    public long readLong() {
        return ((long) buffer[position++] << 56)
                | ((long) (buffer[position++] & 0xff) << 48)
                | ((long) (buffer[position++] & 0xff) << 40)
                | ((long) (buffer[position++] & 0xff) << 32)
                | ((long) (buffer[position++] & 0xff) << 24)
                | ((buffer[position++] & 0xff) << 16)
                | ((buffer[position++] & 0xff) << 8)
                | ((buffer[position++] & 0xff));
    }

    @Override
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public CharSequence readCharSequence(int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(buffer, position, bytes, 0, length);
        position += length;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(buffer, position, bytes, 0, length);
        position += length;
        return bytes;
    }

    @Override
    public int readableBytes() {
        return buffer.length - position;
    }

}