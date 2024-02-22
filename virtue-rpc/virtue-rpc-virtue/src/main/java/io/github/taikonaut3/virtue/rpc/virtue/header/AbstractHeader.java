package io.github.taikonaut3.virtue.rpc.virtue.header;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.constant.Mode;
import io.github.taikonaut3.virtue.common.constant.ModeContainer;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHeader implements Header {

    private int allowMaxSize;

    private volatile Map<String, String> extendData = new HashMap<>();

    protected AbstractHeader(Mode envelopeMode, Mode protocolMode) {
        addExtendData(Key.ENVELOPE, envelopeMode.name());
        addExtendData(Key.PROTOCOL, protocolMode.name());
    }

    @Override
    public int getAllowMaxSize() {
        return 0;
    }

    @Override
    public int getLength() {
        return fixDataToBytes().length + extendDataToBytes().length;
    }

    @Override
    public Mode getEnvelopeMode() {
        String envelopeName = getExtendData(Key.ENVELOPE);
        return ModeContainer.getMode(Key.ENVELOPE, envelopeName);
    }

    @Override
    public Mode getProtocolMode() {
        String protocolName = getExtendData(Key.PROTOCOL);
        return ModeContainer.getMode(Key.PROTOCOL, protocolName);
    }

    @Override
    public void addExtendData(Map<String, String> extendData) {
        this.extendData = extendData;
    }

    @Override
    public void addExtendData(String key, String value) {
        extendData.put(key, value);
    }

    @Override
    public Map<String, String> getExtendsData() {
        return extendData;
    }

    @Override
    public String getExtendData(String key) {
        return extendData.get(key);
    }

    @Override
    public byte[] extendDataToBytes() {
        return serializer().serialize(extendData);
    }

}
