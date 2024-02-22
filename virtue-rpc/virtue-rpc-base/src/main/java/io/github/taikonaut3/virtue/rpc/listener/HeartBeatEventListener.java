package io.github.taikonaut3.virtue.rpc.listener;

import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.config.ClientConfig;
import io.github.taikonaut3.virtue.event.EventListener;
import io.github.taikonaut3.virtue.rpc.event.HeartBeatEvent;
import io.github.taikonaut3.virtue.transport.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Close idle connections according to config
 * {@link ClientConfig#spareCloseTimes}
 * to reduce resource waste
 */
public class HeartBeatEventListener implements EventListener<HeartBeatEvent> {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatEventListener.class);

    @Override
    public void onEvent(HeartBeatEvent event) {
        Channel channel = event.source();
        URL url = channel.attribute(URL.ATTRIBUTE_KEY).get();
        AtomicInteger readIdleRecord = channel.attribute(Key.READER_IDLE_TIMES_ATTRIBUTE_KEY).get();
        AtomicInteger writeIdleRecord = channel.attribute(Key.WRITE_IDLE_TIMES_ATTRIBUTE_KEY).get();
        AtomicInteger allIdlerRecord = channel.attribute(Key.ALL_IDLE_TIMES_ATTRIBUTE_KEY).get();
        int spareCloseTimes = Constant.DEFAULT_SPARE_CLOSE_TIMES;
        if (url != null) {
            spareCloseTimes = url.getIntParameter(Key.SPARE_CLOSE_TIMES, Constant.DEFAULT_SPARE_CLOSE_TIMES);
        }
        logger.trace("Received Event({})", event.getClass().getSimpleName());
        logger.trace("readIdleRecord:{},writeIdleRecord:{},allIdlerRecord:{}", readIdleRecord, writeIdleRecord, allIdlerRecord);
        if (allIdlerRecord != null) {
            int allIdleTimes = allIdlerRecord.incrementAndGet();
            if (readIdleRecord != null) {
                int readIdleTimes = readIdleRecord.incrementAndGet();
                if (readIdleTimes > spareCloseTimes && allIdleTimes > spareCloseTimes) {
                    channel.close();
                }
            }
            if (writeIdleRecord != null) {
                int writeIdleTimes = writeIdleRecord.incrementAndGet();
                if (writeIdleTimes > spareCloseTimes && allIdleTimes > spareCloseTimes) {
                    channel.close();
                }
            }
        }
    }

}
