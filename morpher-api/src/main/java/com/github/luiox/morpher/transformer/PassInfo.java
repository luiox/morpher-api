package com.github.luiox.morpher.transformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PassInfo {
    /**
     * Pass的名称。
     * @return 名称字符串
     */
    String name();

    /**
     * Pass的描述信息。
     * @return 描述字符串，默认为空
     */
    String description() default "";
}
