package com.github.luiox.morpher.transformer;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.MethodNode;

/**
 * 方法级别的Pass抽象类。
 * <p>
 * 继承此类可实现对单个方法节点的转换逻辑。
 */
public abstract class MethodPass implements AbstractPass {
    /**
     * 执行方法级Pass的主逻辑。
     *
     * @param methodNode 方法节点
     * @param context    Pass上下文
     */
    public abstract void run(@NotNull MethodNode methodNode, @NotNull IPassContext context);
}
