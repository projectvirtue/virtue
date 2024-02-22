package io.github.taikonaut3.virtue.common.spi;

import java.lang.annotation.*;

/**
 * Use the annotation on the Implementation class of the SPI Interface
 * Indicate that the current class is available for SPI Selection.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceProvider {

    /**
     * Current Implementation class name.
     *
     * @return name
     */
    String value() default "";

    /**
     * Specifies which interface to provide implementation.
     *
     * @default: for all @ServiceInterface modified interfaces
     */
    Class<?>[] interfaces() default {};

}
