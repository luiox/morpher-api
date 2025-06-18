package com.github.luiox.morpher.transformer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;

public interface IPassContext {
    /**
     * 当前正在处理的ClassNode，只对MethodPass和ClassPass有效
     * @return 当前的ClassNode
     */
    @NotNull ClassNode currentClass();

    /**
     * 设置当前正在处理的ClassNode
     * @param classNode class
     */
    void setCurrentClass(@NotNull ClassNode classNode);

    /**
     * 上一个pass
     * @return 如果此pass是第一个pass，那么上一个pass是null
     */
    default @Nullable AbstractPass prevPass(){
        return null;
    }

    /**
     * 设置之前运行的pass
     * @param pass pass
     */
    void setPrevPass(@NotNull AbstractPass pass);


}
