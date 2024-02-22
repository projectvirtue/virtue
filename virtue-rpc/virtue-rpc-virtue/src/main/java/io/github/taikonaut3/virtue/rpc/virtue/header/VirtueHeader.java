package io.github.taikonaut3.virtue.rpc.virtue.header;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.constant.Mode;
import io.github.taikonaut3.virtue.common.constant.ModeContainer;
import io.github.taikonaut3.virtue.transport.byteutils.ByteReader;
import io.github.taikonaut3.virtue.transport.byteutils.ByteWriter;

public class VirtueHeader extends AbstractHeader {

    private static final int magic = 888;

    public VirtueHeader(Mode serializeMode, Mode envelopeMode, Mode protocolMode) {
        super(envelopeMode, protocolMode);
        setSerializeMode(serializeMode);
    }

    public static VirtueHeader parse(byte[] bytes) {
        ByteReader reader = ByteReader.newReader(bytes);
        // 魔数位
        int magic = reader.readInt();
        if (VirtueHeader.magic != magic) {
            throw new IllegalArgumentException("parse ProtocolHeader error");
        }
        // 协议版本
        Mode protocolMode = ModeContainer.getMode(Key.PROTOCOL, reader.readByte());
        // 消息类型
        Mode envelopeMode = ModeContainer.getMode(Key.ENVELOPE, reader.readByte());
        // 序列化方式
        Mode serializeMode = ModeContainer.getMode(Key.SERIALIZE, reader.readByte());
        return new VirtueHeader(serializeMode, envelopeMode, protocolMode);
    }

    public Mode getSerializeMode() {
        String serializeName = getExtendData(Key.SERIALIZE);
        return ModeContainer.getMode(Key.SERIALIZE, serializeName);
    }

    public void setSerializeMode(Mode serializeMode) {
        addExtendData(Key.SERIALIZE, serializeMode.name());
    }

    @Override
    public byte[] fixDataToBytes() {
        ByteWriter writer = ByteWriter.newWriter();
        // 魔数位
        writer.writeInt(magic);
        // 协议版本
        writer.writeByte(getProtocolMode().type());
        // 消息类型
        writer.writeByte(getEnvelopeMode().type());
        // 序列化方式
        writer.writeByte(getSerializeMode().type());
        return writer.toBytes();
    }

}
