package com.github.luiox.morpher.transformer;

import org.jetbrains.annotations.NotNull;

/**
 * 全局级别的pass
 */
public abstract class GlobalPass implements AbstractPass {
    // 全局级别的pass
    public abstract void run(@NotNull IPassContext context);
}
