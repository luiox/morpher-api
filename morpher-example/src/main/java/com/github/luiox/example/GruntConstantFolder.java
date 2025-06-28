package com.github.luiox.example;

import com.github.luiox.morpher.asm.insn.InsnUtil;
import com.github.luiox.morpher.asm.matcher.MatchRule;
import com.github.luiox.morpher.asm.matcher.PatternMatcher;
import com.github.luiox.morpher.asm.matcher.StepUtil;
import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GruntConstantFolder extends MethodPass {
    private static final Logger logger = LoggerFactory.getLogger(GruntConstantFolder.class);

    static PatternMatcher matcher = new PatternMatcher();

    static {
        // ldc(I:v) - i2l => ldc(J:v)
        MatchRule rule1 = new MatchRule()
                .addStep(StepUtil.loadInt())
                .addStep(StepUtil.i2l())
                .setStrategy(ctx -> {
                    var insn = ctx.original.get(ctx.startIdx);
                    var val = InsnUtil.getIntValue(insn);
                    ctx.builder.ldc((long) val);
                });
        matcher.addRule(rule1);

        // ldc(J:v) - l2i => ldc(I:v)
        MatchRule rule2 = new MatchRule()
                .addStep(StepUtil.loadLong())
                .addStep(StepUtil.l2i())
                .setStrategy(ctx -> {
                    var insn = ctx.original.get(ctx.startIdx);
                    var val = InsnUtil.getLongValue(insn);
                    ctx.builder.ldc((int) val);
                });
        matcher.addRule(rule2);
        // ldc(J:v1) - ldc(J:v2) - lxor => ldc(J:v1^v2)
        MatchRule rule3 = new MatchRule()
                .addStep(StepUtil.loadLong())
                .addStep(StepUtil.loadLong())
                .addStep(StepUtil.lxor())
                .setStrategy(ctx -> {
                    var val1 = InsnUtil.getLongValue(ctx.original.get(ctx.startIdx));
                    var val2 = InsnUtil.getLongValue(ctx.original.get(ctx.startIdx + 1));
                    ctx.builder.ldc(val1 ^ val2);
                });
        matcher.addRule(rule3);
        // ldc(I:v1) - ldc(I:v2) - ixor => ldc(I:v1^v2)
        MatchRule rule4 = new MatchRule()
                .addStep(StepUtil.loadInt())
                .addStep(StepUtil.loadInt())
                .addStep(StepUtil.ixor())
                .setStrategy(ctx -> {
                    var val1 = InsnUtil.getIntValue(ctx.original.get(ctx.startIdx));
                    var val2 = InsnUtil.getIntValue(ctx.original.get(ctx.startIdx + 1));
                    ctx.builder.ldc(val1 ^ val2);
                });
        matcher.addRule(rule4);
    }

    @Override
    public void run(@NotNull MethodNode methodNode, @NotNull IPassContext context) {
        if (methodNode.instructions == null || methodNode.instructions.size() == 0) {
            return;
        }

        int startSize;
        int endSize;
        do {
            startSize = methodNode.instructions.size();
            methodNode.instructions = matcher.apply(methodNode.instructions);
            endSize = methodNode.instructions.size();

//            logger.info("Reduced method size from " + startSize + " to " + endSize);
        } while (startSize != endSize);
    }
}
