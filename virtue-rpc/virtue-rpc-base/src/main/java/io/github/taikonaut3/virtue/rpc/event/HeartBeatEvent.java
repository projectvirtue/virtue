package io.github.taikonaut3.virtue.rpc.event;

import io.github.taikonaut3.virtue.event.AbstractEvent;
import io.github.taikonaut3.virtue.transport.channel.Channel;

public class HeartBeatEvent extends AbstractEvent<Channel> {

    public HeartBeatEvent(Channel channel) {
        super(channel);
    }

}
