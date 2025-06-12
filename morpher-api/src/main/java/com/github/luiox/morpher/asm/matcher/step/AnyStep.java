package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.function.Predicate;

public class AnyStep implements IMatchStep {
    private Predicate<AbstractInsnNode> additional;

    public AnyStep(@NotNull Predicate<AbstractInsnNode> condition) {
        this.additional = condition;
    }

    @Override
    public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
        return additional.test(now);
    }
}
