package io.github.taikonaut3.virtue.common.constant;

import io.github.taikonaut3.virtue.common.extension.AttributeKey;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Key constant.
 */
public interface Key {

    String UNIQUE_ID = "uniqueId";

    String APPLICATION = "application";

    String SERVICE = "service";

    String PROTOCOL = "protocol";

    String EVENT_DISPATCHER = "eventDispatcher";

    String BUFFER_SIZE = "bufferSize";

    String SERVER_EVENT_DISPATCHER = "serverEventDispatcher";

    String PROTOCOL_VERSION = "protocolVersion";

    String CLASS = "class";

    String BODY_TYPE = "bodyType";

    String REGISTERS = "registers";

    String ONEWAY = "oneway";

    String RETRY_INTERVAL = "retryInterval";

    String HEALTH_CHECK_INTERVAL = "healthCheckInterval";

    String ENABLED = "enabled";

    String VERSION = "version";

    String GROUP = "group";

    String WEIGHT = "weight";

    String MSG_TYPE = "msgType";

    String TIMEOUT = "timeout";

    String KEEP_ALIVE_TIMEOUT = "keepAliveTimeout";

    String SPARE_CLOSE_TIMES = "spareCloseTimes";

    String HEARTBEAT_LOG_ENABLE = "heartbeatLogEnable";

    String READER_IDLE_TIMES = "readeIdleTimes";

    String WRITE_IDLE_TIMES = "writeIdleTimes";

    String ALL_IDLE_TIMES = "allIdleTimes";

    String SO_BACKLOG = "soBacklog";

    String MAX_CONNECTIONS = "maxConnections";

    String MAX_THREADS = "maxThreads";

    String ASYNC = "async";

    String LAZY_DISCOVER = "lazyDiscover";

    String PROXY = "proxy";

    String TRANSPORTER = "transporter";

    String DISCOVER_URL = "discoverUrl";

    String MULTIPLEX = "multiplex";

    String SERIALIZE = "serialize";

    String LOAD_BALANCE = "loadBalance";

    String DIRECTORY = "directory";

    String ROUTER = "router";

    String RETRIES = "retries";

    String REQUEST = "request";

    String RESPONSE = "response";

    String FAULT_TOLERANCE = "faultTolerance";

    String CHECK = "check";

    String TIMESTAMP = "timestamp";

    String DYNAMIC = "dynamic";

    String SUBSCRIBE = "subscribe";

    String MAX_RECEIVE_SIZE = "maxReceiveSize";

    String CLIENT_MAX_RECEIVE_SIZE = "clientMaxReceiveSize";

    String MAX_HEADER_SIZE = "maxHeaderSize";

    String SSL = "ssl";

    String CONNECT_TIMEOUT = "connectTimeout";

    String COMPRESSION = "compression";

    String KEEPALIVE = "keepAlive";

    String GLOBAL = "global";

    String URL = "url";

    String HTTP_REQUEST_WRAPPER = "httpRequestWrapper";

    String ENVELOPE = "envelope";
    String RESPONSE_CODE = "responseCode";

    String INVOKER = "invoker";

    String RETURN_TYPE = "returnType";

    String PASSWORD = "password";

    String ENABLE_HEALTH_CHECK = "enableHealthCheck";

    String USERNAME = "username";

    String SESSION_TIMEOUT = "sessionTimeout";

    String INTERVAL = "interval";

    String DESCRIPTION = "description";

    String CLIENT = "client";
    String HTTP_METHOD = "httpMethod";

    String VIRTUE = "virtue";
    AttributeKey<AtomicInteger> ALL_IDLE_TIMES_ATTRIBUTE_KEY = AttributeKey.get(ALL_IDLE_TIMES);
    AttributeKey<AtomicInteger> WRITE_IDLE_TIMES_ATTRIBUTE_KEY = AttributeKey.get(WRITE_IDLE_TIMES);
    AttributeKey<AtomicInteger> READER_IDLE_TIMES_ATTRIBUTE_KEY = AttributeKey.get(READER_IDLE_TIMES);
    String VERTX = "vertx";
}
