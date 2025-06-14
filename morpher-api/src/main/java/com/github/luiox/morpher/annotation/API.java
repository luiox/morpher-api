package com.github.luiox.morpher.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface API {
    APIStatus status() default APIStatus.Internal;

    String since() default "1.0";

    String until() default "";
}
