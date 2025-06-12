package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;

public class AnyMultiStep implements IMatchStep {
    private int matchedSize;

    public AnyMultiStep() {
        matchedSize = 0;
    }

    @Override
    public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
        matchedSize++;
        return true;
    }

    @Override
    public int getMatchedSize() {
        return matchedSize;
    }
}
