package io.github.taikonaut3.virtue.boot;

import io.github.taikonaut3.virtue.config.manager.Virtue;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 启动 virtue
 */
public class SpringBootVirtueStarter implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Virtue virtue = event.getApplicationContext().getBean(Virtue.class);
        virtue.start();
    }

}
