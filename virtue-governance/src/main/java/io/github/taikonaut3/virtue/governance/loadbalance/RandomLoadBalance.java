package io.github.taikonaut3.virtue.governance.loadbalance;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.Invocation;

import java.util.List;
import java.util.Random;

import static io.github.taikonaut3.virtue.common.constant.Components.LoadBalance.RANDOM;

/**
 * "随机"负载均衡策略:
 * 随机地将请求分配到服务器集群中的任意一台服务器
 * 这种策略简单快速，但可能会导致服务器负载不均衡
 */
@ServiceProvider(RANDOM)
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected URL doSelect(Invocation invocation, List<URL> urls) {
        Random random = new Random();
        int index = random.nextInt(urls.size());
        return urls.get(index);
    }

}
