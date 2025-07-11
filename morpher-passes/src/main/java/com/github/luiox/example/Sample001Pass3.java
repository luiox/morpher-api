package com.github.luiox.example;

import com.github.luiox.morpher.asm.insn.InsnUtil;
import com.github.luiox.morpher.asm.matcher.MatchRule;
import com.github.luiox.morpher.asm.matcher.PatternMatcher;
import com.github.luiox.morpher.asm.matcher.StepUtil;
import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import com.github.luiox.morpher.transformer.PassInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.MethodNode;

@PassInfo(name = "Sample001Pass3", description = "处理常量折叠的pass")
public class Sample001Pass3 extends MethodPass {

    static PatternMatcher matcher = new PatternMatcher();

    static {
        // ldc(I:v1) - ldc(I:v2) - ixor => ldc(I:v1^v2)
        MatchRule rule1 = new MatchRule()
                .addStep(StepUtil.loadInt())
                .addStep(StepUtil.loadInt())
                .addStep(StepUtil.ixor())
                .setStrategy(ctx -> {
                    var val1 = InsnUtil.getIntValue(ctx.original.get(ctx.startIdx));
                    var val2 = InsnUtil.getIntValue(ctx.original.get(ctx.startIdx + 1));
                    ctx.builder.ldc(val1 ^ val2);
                });
        matcher.addRule(rule1);

        // ineg - ineg => nothing
        MatchRule rule2 = new MatchRule()
                .addStep(StepUtil.iconst_0())
                .addStep(StepUtil.ifeq())
                .setStrategy(ctx -> {
                    // 全部丢掉
                });
        matcher.addRule(rule2);
    }

    @Override
    public void run(@NotNull MethodNode methodNode, @NotNull IPassContext passContext) {
        if(methodNode.instructions == null || methodNode.instructions.size() == 0){
            return;
        }

        int startSize, endSize;
        do{
            startSize = methodNode.instructions.size();
            methodNode.instructions = matcher.apply(methodNode.instructions);
            endSize = methodNode.instructions.size();
        }while (startSize != endSize);
    }
}