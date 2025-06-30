package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class OrStep implements IMatchStep {

    List<IMatchStep> steps;

    public OrStep(List<IMatchStep> steps) {
        this.steps = steps;
    }

    public static OrStep of(List<IMatchStep> steps) {
        return new OrStep(steps);
    }

    @Override
    public boolean tryMatch(@NotNull AbstractInsnNode abstractInsnNode, @NotNull MatchContext matchContext) {
        for (var step : steps) {
            if (step.tryMatch(abstractInsnNode, matchContext)) {
                // 只要有一个步骤匹配，就返回true
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMatchedSize() {
        int size = 0;
        for (var step : steps) {
            size += step.getMatchedSize();
        }
        return size;
    }
}
