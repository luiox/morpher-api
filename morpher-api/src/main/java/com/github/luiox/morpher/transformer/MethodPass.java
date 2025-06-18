package com.github.luiox.morpher.transformer;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.MethodNode;

/**
 * 方法级别的Pass
 */
public abstract class MethodPass implements AbstractPass {
    public abstract void run(@NotNull MethodNode methodNode, @NotNull IPassContext context);
}
