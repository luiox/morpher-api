package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.info.ClassInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;

import java.util.Map;
import java.util.function.Consumer;

public interface PassHelper {
    @NotNull Map<String, ClassInfo> buildClassInfo(@NotNull IPassContext context);

    void iterateClassNodeWithInfo(@NotNull IPassContext context,
                                  @NotNull Map<String, ClassInfo> infos,
                                  int rflag,
                                  int wflag,
                                  @NotNull Consumer<ClassNode> consumer);

    void addLibPath(@NotNull String path);
}
