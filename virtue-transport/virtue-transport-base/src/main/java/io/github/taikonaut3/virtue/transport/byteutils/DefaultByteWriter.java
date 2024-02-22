package io.github.taikonaut3.virtue.transport.byteutils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class DefaultByteWriter implements ByteWriter {

    private final ByteArrayOutputStream outputStream;

    public DefaultByteWriter() {
        outputStream = new ByteArrayOutputStream();
    }

    @Override
    public void writeByte(byte value) {
        outputStream.write(value);
    }

    @Override
    public void writeBoolean(boolean value) {
        outputStream.write(value ? (byte) 1 : (byte) 0);
    }

    @Override
    public void writeShort(short value) {
        outputStream.write((value >> 8) & 0xff);
        outputStream.write(value & 0xff);
    }

    @Override
    public void writeInt(int value) {
        outputStream.write((value >> 24) & 0xff);
        outputStream.write((value >> 16) & 0xff);
        outputStream.write((value >> 8) & 0xff);
        outputStream.write(value & 0xff);
    }

    @Override
    public void writeInt(int value, int index) {
        // 判断index是否越界
        if (index < 0 || index > outputStream.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        byte[] newBytes = new byte[4];
        newBytes[0] = (byte) (value >>> 24);
        newBytes[1] = (byte) (value >>> 16);
        newBytes[2] = (byte) (value >>> 8);
        newBytes[3] = (byte) value;

        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        if (index == outputStream.size()) {
            // 如果写入位置是在缓冲区的末尾，则直接将新数据写入缓冲区的末尾
            tempStream.write(outputStream.toByteArray(), 0, outputStream.size());
            tempStream.write(newBytes, 0, 4);
        } else {
            // 如果写入位置是在缓冲区中间，则先将写入位置之前的数据写入临时字节数组中
            tempStream.write(outputStream.toByteArray(), 0, index);
            // 然后将新数据写入到临时字节数组中
            tempStream.write(newBytes, 0, 4);
            // 最后再将写入位置之后的数据写入到临时字节数组中
            tempStream.write(outputStream.toByteArray(), index, outputStream.size() - index);
        }

        // 将临时字节数组中的数据写入到缓冲区，更新写入指针位置
        outputStream.reset();
        outputStream.write(tempStream.toByteArray(), 0, tempStream.size());
    }

    @Override
    public void writeLong(long value) {
        outputStream.write((int) ((value >> 56) & 0xff));
        outputStream.write((int) ((value >> 48) & 0xff));
        outputStream.write((int) ((value >> 40) & 0xff));
        outputStream.write((int) ((value >> 32) & 0xff));
        outputStream.write((int) ((value >> 24) & 0xff));
        outputStream.write((int) ((value >> 16) & 0xff));
        outputStream.write((int) ((value >> 8) & 0xff));
        outputStream.write((int) (value & 0xff));
    }

    @Override
    public void writeFloat(float value) {
        writeInt(Float.floatToIntBits(value));
    }

    @Override
    public void writeDouble(double value) {
        writeLong(Double.doubleToLongBits(value));
    }

    @Override
    public void writeBytes(byte[] bytes) {
        outputStream.write(bytes, 0, bytes.length);
    }

    @Override
    public void writeCharSequence(CharSequence value) {
        byte[] bytes = value.toString().getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes, 0, bytes.length);
    }

    @Override
    public int writableBytes() {
        return outputStream.size();
    }

    @Override
    public byte[] toBytes() {
        return outputStream.toByteArray();
    }

}