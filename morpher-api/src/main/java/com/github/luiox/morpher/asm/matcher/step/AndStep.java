package com.github.luiox.morpher.asm.matcher.step;


import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

/**
 * 结合N个步骤的逻辑与操作。
 */
public class AndStep implements IMatchStep {

    List<IMatchStep> steps;

    public AndStep(List<IMatchStep> steps) {
        this.steps = steps;
    }

    public static AndStep of(List<IMatchStep> steps) {
        return new AndStep(steps);
    }

    @Override
    public boolean tryMatch(@NotNull AbstractInsnNode abstractInsnNode, @NotNull MatchContext matchContext) {
        for (var step : steps) {
            if (!step.tryMatch(abstractInsnNode, matchContext)) {
                // 只要有一个步骤不匹配，就返回false
                return false;
            }
        }
        return true;
    }

    @Override
    public int getMatchedSize() {
        return steps.stream()
                .mapToInt(IMatchStep::getMatchedSize)
                .sum();
    }
}
