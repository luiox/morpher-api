package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;

public class NotStep implements IMatchStep {

    IMatchStep step;

    public NotStep(IMatchStep step) {
        this.step = step;
    }

    public static NotStep of(IMatchStep step) {
        return new NotStep(step);
    }

    @Override
    public boolean tryMatch(@NotNull AbstractInsnNode abstractInsnNode, @NotNull MatchContext matchContext) {
        return !step.tryMatch(abstractInsnNode, matchContext);
    }

    @Override
    public int getMatchedSize() {
        return step.getMatchedSize();
    }
}
