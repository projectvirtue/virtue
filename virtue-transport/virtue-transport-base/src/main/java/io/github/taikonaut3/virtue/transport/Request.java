package io.github.taikonaut3.virtue.transport;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.extension.AttributeKey;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Accessors(fluent = true)
public class Request implements Envelope {

    public static final AttributeKey<Request> ATTRIBUTE_KEY = AttributeKey.get(Key.REQUEST);

    private static final AtomicLong INCREASE = new AtomicLong(0);

    private Long id;

    private boolean oneway;

    private URL url;

    private Object message;

    public Request() {
    }

    public Request(URL url, Object message) {
        id = INCREASE.getAndIncrement();
        String timestamp = DateUtil.format(LocalDateTime.now(), DateUtil.COMPACT_FORMAT);
        url.addParameter(Key.TIMESTAMP, timestamp);
        url.addParameter(Key.ENVELOPE, Key.REQUEST);
        url.addParameter(Key.UNIQUE_ID, String.valueOf(id));
        this.url = url;
        this.message = message;

    }

}
