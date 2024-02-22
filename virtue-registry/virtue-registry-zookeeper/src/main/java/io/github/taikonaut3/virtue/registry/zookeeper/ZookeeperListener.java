//package io.github.taikonaut3.virtue.registry.zookeeper;
//
//import io.github.taikonaut3.virtue.common.url.URL;
//import io.github.taikonaut3.virtue.registry.RegistryListener;
//import org.apache.curator.framework.recipes.cache.ChildData;
//import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
//
//public class ZookeeperListener implements CuratorCacheListener {
//
//    private final RegistryListener listener;
//
//    public ZookeeperListener(RegistryListener listener) {
//        this.listener = listener;
//    }
//
//    @Override
//    public void event(Type type, ChildData oldData, ChildData data) {
//        switch (type) {
//            case NODE_CHANGED -> handleChanged(oldData, data);
//            case NODE_CREATED -> handleCreated(data);
//            case NODE_DELETED -> handleDeleted(oldData);
//        }
//    }
//
//    private void handleChanged(ChildData oldData, ChildData newData) {
//        listener.listenChanged(getUrl(oldData), getUrl(newData));
//    }
//
//    private void handleCreated(ChildData data) {
//        if (data.getData().length > 0) {
//            listener.listenCreated(getUrl(data));
//        }
//    }
//
//    private void handleDeleted(ChildData data) {
//        listener.listenDelete(getUrl(data));
//    }
//
//    private URL getUrl(ChildData data) {
//        String urlStr = new String(data.getData());
//        return URL.valueOf(urlStr);
//    }
//
//}
