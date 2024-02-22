package io.github.taikonaut3;

import io.github.taikonaut3.filter.Filter1;
import io.github.taikonaut3.filter.Filter2;
import io.github.taikonaut3.model.ParentObject;
import io.github.taikonaut3.virtue.boot.EnableVirtue;
import io.github.taikonaut3.virtue.config.config.RegistryConfig;
import io.github.taikonaut3.virtue.config.config.ServerConfig;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.VIRTUE;
import static io.github.taikonaut3.virtue.common.constant.Components.Registry.CONSUL;

@SpringBootApplication
@EnableVirtue(scanBasePackages = "io.github.taikonaut3")
public class ConsumerMain {
    public static void main(String[] args) {
        //pringApplication.run(ConsumerMain.class, args);
        simpleTest();
    }

    public static void simpleTest() {
        Virtue virtue = Virtue.getDefault();
        Consumer consumer = virtue.applicationName("consumer")
                .register(new RegistryConfig("consul://127.0.0.1:8500"))
                .register("filter1", new Filter1())
                .register("filter2",new Filter2())
                .router("^virtue://.*/345/list",":2333")
                //.register(new RegistryConfig("nacos://127.0.0.1:8848"))
                .proxy(Consumer.class)
                .remoteCaller(Consumer.class)
                .get();
        virtue.start();
        for (int i = 0; i < 50; i++) {
            long start = System.currentTimeMillis();
            List<ParentObject> list = consumer.list(ParentObject.getObjList());
            long end = System.currentTimeMillis();
            System.out.println("耗时:" + (end - start) + list);
        }
//        String hello = consumer.hello("world");
//        String hello1 = consumer.hello("world2");
//        System.out.println(hello+hello1);
//        CompletableFuture<String> future = consumer.helloAsync("worldAsync");
//        String s = null;
//        try {
//            s = future.get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(s);
    }

    @Bean
    public ServerConfig serverConfig() {
        return new ServerConfig(VIRTUE, 2999);
    }

//    @Bean
//    public RegistryConfig nacosRegistryConfig() {
//        RegistryConfig registryConfig = new RegistryConfig();
//        registryConfig.type(NACOS);
//        registryConfig.host("127.0.0.1");
//        registryConfig.port(8848);
//        return registryConfig;
//    }

    @Bean
    public RegistryConfig consulRegistryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.type(CONSUL);
        registryConfig.host("127.0.0.1");
        registryConfig.port(8500);
        return registryConfig;
    }
}