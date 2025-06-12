package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.function.Predicate;

public class SimpleStep implements IMatchStep {
    private final int opcode;
    private final Predicate<AbstractInsnNode> additional;

    public SimpleStep(int opcode) {
        this(opcode, _ -> true);
    }

    public SimpleStep(int opcode, @NotNull Predicate<AbstractInsnNode> condition) {
        this.opcode = opcode;
        this.additional = condition;
    }

    @Override
    public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
        return now.getOpcode() == opcode && additional.test(now);
    }
}
