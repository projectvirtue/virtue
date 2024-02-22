package io.github.taikonaut3.virtue.transport.base;

import io.github.taikonaut3.virtue.common.exception.NetWorkException;
import io.github.taikonaut3.virtue.common.extension.AttributeKey;
import io.github.taikonaut3.virtue.transport.channel.Channel;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Follow-up consideration for extensions
 */
public class HeartBeatChannelHandler extends ChannelHandlerAdapter {

    private final Set<AttributeKey<AtomicInteger>> watchEventKeys = new HashSet<>();

    @Override
    public void received(Channel channel, Object message) throws NetWorkException {
        for (AttributeKey<AtomicInteger> watchEventKey : watchEventKeys) {
            channel.attribute(watchEventKey).set(new AtomicInteger(0));
        }
    }

    @Override
    public void heartBeat(Channel channel, Object event) {
        super.heartBeat(channel, event);
    }

    public HeartBeatChannelHandler addWatchEventKey(AttributeKey<AtomicInteger> key) {
        watchEventKeys.add(key);
        return this;
    }

}
