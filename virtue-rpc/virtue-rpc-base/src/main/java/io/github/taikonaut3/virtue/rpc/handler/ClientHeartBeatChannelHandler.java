package io.github.taikonaut3.virtue.rpc.handler;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.rpc.event.HeartBeatEvent;
import io.github.taikonaut3.virtue.transport.base.HeartBeatChannelHandler;
import io.github.taikonaut3.virtue.transport.channel.Channel;

public class ClientHeartBeatChannelHandler extends HeartBeatChannelHandler {

    public ClientHeartBeatChannelHandler() {
        addWatchEventKey(Key.ALL_IDLE_TIMES_ATTRIBUTE_KEY);
        addWatchEventKey(Key.WRITE_IDLE_TIMES_ATTRIBUTE_KEY);
    }

    @Override
    public void heartBeat(Channel channel, Object event) {
        URL url = channel.attribute(URL.ATTRIBUTE_KEY).get();
        if (url != null) {
            getEventDispatcher(url).dispatchEvent(new HeartBeatEvent(channel));
        }

    }

}
