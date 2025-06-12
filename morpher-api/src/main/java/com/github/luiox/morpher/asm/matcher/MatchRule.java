package com.github.luiox.morpher.asm.matcher;

import com.github.luiox.morpher.asm.matcher.step.AnyMultiStep;
import com.github.luiox.morpher.asm.matcher.step.IMatchStep;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.InsnList;

import java.util.ArrayList;
import java.util.List;

/**
 * 匹配规则
 * 修改的策略是可选的，如果使用matcher的apply自动应用修改，那么就必须设置修改策略
 * 如果仅仅使用matcher的match方法，那么可以不设置修改策略
 */
public class MatchRule {

    private final List<IMatchStep> steps = new ArrayList<>();
    private IModifyStrategy strategy = null;

    public MatchRule() {
    }

    public @NotNull MatchRule setStrategy(@NotNull IModifyStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public @NotNull IModifyStrategy getStrategy() {
        return strategy;
    }

    public @NotNull MatchRule addStep(@NotNull IMatchStep step) {
        steps.add(step);
        return this;
    }

    public @NotNull List<IMatchStep> getSteps() {
        return steps;
    }

    int matchedSize = 0;

    public boolean tryMatch(@NotNull InsnList list, int currentPos, MatchContext context) {
        for (int i = 0; i < steps.size(); ) {
            var step = steps.get(i);
            if (step instanceof AnyMultiStep) {
                // 如果是匹配任意多条指令，先尝试匹配下一个
                if (i + 1 < steps.size()) {
                    var nextStep = steps.get(i + 1);
                    if (nextStep.tryMatch(list.get(currentPos), context)) {
                        // 如果匹配成功就直接算是下一个指令匹配成功的
                        currentPos += step.getMatchedSize();
                        // 并且跳过当前的AnyMultiStep和匹配成功的下一个指令
                        i += 2;
                        continue;
                    }
                }
            }
            // 如果是普通情况，或者是AnyMultiStep之后的一个匹配失败，那么就让AnyMultiStep再匹配一个
            if (!step.tryMatch(list.get(currentPos), context)) {
                return false;
            }
            currentPos += step.getMatchedSize();
            i++;
        }
        return true;
    }

    public int getMatchedSize() {
        int total = 0;
        for (IMatchStep step : steps) {
            total += step.getMatchedSize();
        }
        return total;
    }
}
