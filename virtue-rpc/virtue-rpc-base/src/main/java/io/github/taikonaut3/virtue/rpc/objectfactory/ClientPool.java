package io.github.taikonaut3.virtue.rpc.objectfactory;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.exception.SourceException;
import io.github.taikonaut3.virtue.common.extension.RpcContext;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.transport.Transporter;
import io.github.taikonaut3.virtue.transport.channel.ChannelHandler;
import io.github.taikonaut3.virtue.transport.client.Client;
import io.github.taikonaut3.virtue.transport.codec.Codec;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClientPool
 */
public class ClientPool {

    private static final Logger logger = LoggerFactory.getLogger(ClientPool.class);

    private final GenericObjectPool<Client> clientPool;

    private final Map<String, Client> customClients = new ConcurrentHashMap<>();

    public ClientPool(Transporter transporter, Codec codec, ChannelHandler handler) {
        GenericObjectPoolConfig<Client> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(100);    // 设置最大对象数量
        poolConfig.setMinIdle(50);      // 设置最小空闲对象数量
        poolConfig.setMaxIdle(50);      // 设置最大空闲对象数量
        clientPool = new GenericObjectPool<>(new ClientFactory(transporter, codec, handler), poolConfig);
    }

    public Client get(URL url) {
        boolean isMultiplex = url.getBooleanParameter(Key.MULTIPLEX, true);
        try {
            if (isMultiplex) {
                RpcContext.getContext().attribute(URL.ATTRIBUTE_KEY).set(url);
                return clientPool.borrowObject();
            } else {
                return getCustom(url);
            }
        } catch (Exception e) {
            logger.error("Get Client fail for: " + url, e);
            throw new SourceException("Get Client fail for: " + url);
        }
    }

    private Client getCustom(URL url) throws Exception {
        String key = url.uri();
        Client client;
        client = customClients.get(key);
        if (client == null) {
            synchronized (this) {
                if (customClients.get(key) == null) {
                    client = clientPool.getFactory().makeObject().getObject();
                    customClients.put(key, client);
                }
            }
        } else if (!client.isActive()) {
            client.connect();
        }
        return client;
    }

    public void returnClient(Client client) {
        clientPool.returnObject(client);
    }
}
