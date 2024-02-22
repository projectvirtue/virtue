package io.github.taikonaut3.virtue.transport.byteutils;

/**
 * Tool for Writing bytes
 */
public interface ByteWriter {

    static ByteWriter newWriter() {
        return new DefaultByteWriter();
    }

    void writeByte(byte value);

    void writeBoolean(boolean value);

    void writeShort(short value);

    void writeInt(int value);

    void writeInt(int value, int index);

    void writeLong(long value);

    void writeFloat(float value);

    void writeDouble(double value);

    void writeBytes(byte[] bytes);

    void writeCharSequence(CharSequence value);

    int writableBytes();

    byte[] toBytes();

    default ByteWriter writeHeadInt(int value) {
        writeInt(value, 0);
        return this;
    }

    default ByteWriter writeLength() {
        return writeHeadInt(writableBytes());
    }

}