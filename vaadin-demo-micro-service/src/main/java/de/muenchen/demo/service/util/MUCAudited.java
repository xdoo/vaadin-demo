package de.muenchen.demo.service.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fabian.holtkoetter on 08.09.15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MUCAudited {
    String ALL = "all";
    String READ = "read";
    String CREATE = "create";
    String UPDATE = "update";
    String DELETE = "delete";

    String value() default ALL;
}
