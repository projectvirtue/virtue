package io.github.taikonaut3.virtue.boot;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({VirtuePostProcessorRegister.class})
public @interface EnableVirtue {

    String[] scanBasePackages() default {};

    String[] clientScan() default {};

    String[] serverScan() default {};

}
