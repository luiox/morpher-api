package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;

public interface IMatchStep {
    // 返回是否匹配
    boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx);

    // 获取匹配的指令数量
    default int getMatchedSize() {
        return 1;
    }
}
