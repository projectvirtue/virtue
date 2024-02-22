package io.github.taikonaut3.virtue.boot.processor;

import io.github.taikonaut3.virtue.boot.EnableVirtue;
import io.github.taikonaut3.virtue.boot.RemoteCallFactoryBean;
import io.github.taikonaut3.virtue.config.annotation.RemoteCaller;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoteCallerPostProcessor extends PreferentialCreateConfig implements BeanDefinitionRegistryPostProcessor {

    private final static List<Class<? extends Annotation>> REMOTE_CALL_ANNOTATION_TYPES = List.of(
            RemoteCaller.class
    );

    private final String[] remoteCallPackages;


    public RemoteCallerPostProcessor(EnableVirtue enableVirtue) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, enableVirtue.scanBasePackages());
        Collections.addAll(list, enableVirtue.clientScan());
        this.remoteCallPackages = list.toArray(String[]::new);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        RemoteCallScanner remoteCallScanner = new RemoteCallScanner();
        for (String pkg : remoteCallPackages) {
            List<? extends Class<?>> RemoteCallInterfaces = remoteCallScanner.findClasses(pkg);
            for (Class<?> clazz : RemoteCallInterfaces) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RemoteCallFactoryBean.class);
                builder.addConstructorArgValue(clazz);
                AbstractBeanDefinition definition = builder.getBeanDefinition();
                String beanName = "";
                if (clazz.isAnnotationPresent(Qualifier.class)) {
                    beanName = clazz.getAnnotation(Qualifier.class).value();
                }
                if (!StringUtils.hasText(beanName)) {
                    beanName = StringUtils.uncapitalizeAsProperty(clazz.getSimpleName());
                }
                registry.registerBeanDefinition(beanName, definition);

            }
        }
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof RemoteCallFactoryBean<?> remoteCallFactoryBean) {
            remoteCallFactoryBean.setVirtue(virtue());
        }
        return bean;
    }

    private static final class RemoteCallScanner extends ClassPathScanningCandidateComponentProvider {

        public RemoteCallScanner() {
            for (Class<? extends Annotation> annotationType : REMOTE_CALL_ANNOTATION_TYPES) {
                addIncludeFilter(new AnnotationTypeFilter(annotationType));
            }
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            AnnotationMetadata metadata = beanDefinition.getMetadata();
            return metadata.isInterface();
        }

        public List<? extends Class<?>> findClasses(String basePackage) {
            return findCandidateComponents(basePackage).stream().map(beanDefinition -> {
                try {
                    return ClassLoader.getSystemClassLoader().loadClass(beanDefinition.getBeanClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        }

    }

}
