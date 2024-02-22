//package io.github.taikonaut3.virtue.registry.zookeeper;
//
//import io.github.taikonaut3.virtue.common.constant.Constant;
//import io.github.taikonaut3.virtue.common.constant.Key;
//import io.github.taikonaut3.virtue.config.CallArgs;
//import io.github.taikonaut3.virtue.common.url.URL;
//import io.github.taikonaut3.virtue.common.util.GenerateUtil;
//import io.github.taikonaut3.virtue.registry.AbstractRegistry;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.framework.recipes.cache.CuratorCache;
//import org.apache.curator.retry.RetryNTimes;
//import org.apache.zookeeper.data.Stat;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//public class ZookeeperRegistry extends AbstractRegistry {
//
//    private final String servicesDir = Constant.SERVICES_DIR;
//
//    // 存储监听器，用于监听URL
//    private final Map<String, CuratorCache> curatorCacheMap = new ConcurrentHashMap<>();
//
//    private CuratorFramework curatorFramework;
//
//    public ZookeeperRegistry(URL url) {
//        super(url);
//    }
//
//    @Override
//    public void connect(URL url) {
//        int connectTimeout = url.getIntParameter(Key.CONNECT_TIMEOUT);
//        curatorFramework = CuratorFrameworkFactory.builder()
//                .connectionTimeoutMs(connectTimeout)
//                .connectString(url.getAddress())
//                .sessionTimeoutMs(url.getIntParameter(Key.SESSION_TIMEOUT))
//                .retryPolicy(new RetryNTimes(url.getIntParameter(Key.RETRIES), url.getIntParameter(Key.RETRY_INTERVAL)))
//                .build();
//        curatorFramework.start();
//        try {
//            boolean connected = curatorFramework.blockUntilConnected(connectTimeout, TimeUnit.MILLISECONDS);
//            if (!connected) {
//                throw new IllegalStateException("zookeeper not connected, the address is: " + url);
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public boolean isConnected() {
//        return curatorFramework.getZookeeperClient().isConnected();
//    }
//
//    @Override
//    protected void registerUrl(URL url) {
//
//    }
//
//    @Override
//    protected void registerService(URL url) {
//        String path = servicesDir + GenerateUtil.generateKey(url);
//        createNode(path, url.toString());
//    }
//
//    @Override
//    protected URL doDiscover(CallArgs callData) {
//        String registryPath = servicesDir + GenerateUtil.generateKey(callData);
//        try {
//            String urlStr = new String(curatorFramework.getData().forPath(registryPath));
//            return URL.valueOf(urlStr);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void subscribe(URL url) {
//        String path = GenerateUtil.generateKey(url);
//        CuratorCache curatorCache = curatorCacheMap.get(path);
//        if (curatorCache == null) {
//            curatorCache = CuratorCache.build(curatorFramework, servicesDir + path, CuratorCache.Options.DO_NOT_CLEAR_ON_CLOSE);
//            curatorCache.start();
//            curatorCache.listenable().addListener(new ZookeeperListener(listener));
//        }
//    }
//
//    @Override
//    public void unSubscribe(URL url) {
//        String path = GenerateUtil.generateKey(url);
//        CuratorCache curatorCache = curatorCacheMap.get(path);
//        curatorCache.close();
//        curatorCacheMap.remove(path);
//    }
//
//    @Override
//    public void destroy() {
//        curatorFramework.close();
//    }
//
//    private void createNode(String path) {
//        try {
//            curatorFramework.create().forPath(path);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void createNode(String path, String data) {
//        try {
//            Stat stat = curatorFramework.checkExists().forPath(path);
//            if (stat == null) {
//                curatorFramework.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
//            } else {
//                curatorFramework.setData().forPath(path, data.getBytes());
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
