package com.github.luiox.morpher.transformer;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;

/**
 * Class级别的Pass
 */
public abstract class ClassPass implements AbstractPass {
    public abstract void run(@NotNull ClassNode classNode, @NotNull PassContext context);
}
