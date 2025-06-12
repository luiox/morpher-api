package com.github.luiox.morpher.plugin;

import com.github.luiox.morpher.transformer.AbstractPass;

import java.util.List;

/**
 * 转换器插件接口
 * 必须提供一个无参构造函数
 */
public interface IPassPlugin {

    /**
     * 当插件呗加载的时候被调用
     */
    default void onInitialize() {
    }

    /**
     * 获取插件提供的所有可用转换器
     *
     * @return 插件提供的所有转换器
     */
    List<AbstractPass> getAvailablePasses();
}
