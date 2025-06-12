package com.github.luiox.morpher.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 插件信息
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginInfo {

    /**
     * 插件的名字，尽可能保证名字的唯一性，名字会区分大小写，如果两个插件的名字一样会引发冲突
     *
     * @return 插件的名字
     */
    String name();

    /**
     * 插件的描述
     *
     * @return 插件的描述
     */
    String desc() default "no description provided";

    /**
     * 插件的版本
     *
     * @return 插件的版本
     */
    String version() default "1.0";

    /**
     * 插件的作者
     *
     * @return 插件的作者
     */
    String author() default "unknown";
}