package com.github.luiox.morpher.transformer;

import org.jetbrains.annotations.NotNull;

/**
 * 转换器接口
 */
public interface AbstractPass {
    /**
     * 初始化，真正一开始的初始化，在所有转换之前
     *
     * @param context 上下文
     */
    default void doInitialization(@NotNull IPassContext context) {
    }

    /**
     * 在所有转换之后统一的清理操作
     *
     * @param context 上下文
     */
    default void doFinalization(@NotNull IPassContext context) {
    }
}
