package io.github.taikonaut3.virtue.boot;

import io.github.taikonaut3.virtue.config.manager.Virtue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties
public class VirtueAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Virtue.class)
    public Virtue virtue() {
        return Virtue.getDefault();
    }

    @Bean
    public VirtueConfigurationProperties virtueConfigurationProperties() {
        return new VirtueConfigurationProperties();
    }

    @Bean
    public SpringBootVirtueStarter springBootvirtueStarter() {
        return new SpringBootVirtueStarter();
    }

}
