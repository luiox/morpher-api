package com.github.luiox.example;

import com.github.luiox.morpher.asm.matcher.MatchRule;
import com.github.luiox.morpher.asm.matcher.PatternMatcher;
import com.github.luiox.morpher.asm.matcher.StepUtil;
import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import com.github.luiox.morpher.transformer.PassInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodNode;

@PassInfo(name = "Sample001Pass1", description = "处理a^a的pass")
public class Sample001Pass1 extends MethodPass {

    static PatternMatcher matcher = new PatternMatcher();

    static {
        // load(I) - dup - ixor => iconst_0
        MatchRule rule1 = new MatchRule()
                .addStep(StepUtil.loadInt())
                .addStep(StepUtil.dup())
                .addStep(StepUtil.ixor())
                .setStrategy(ctx -> {
                    ctx.builder.iconst_0();
                });
        matcher.addRule(rule1);

        // iconst_0 - ifeq => goto
        MatchRule rule2 = new MatchRule()
                .addStep(StepUtil.iconst_0())
                .addStep(StepUtil.ifeq())
                .setStrategy(ctx -> {
                   var ifeqNode = (JumpInsnNode)ctx.original.get(ctx.startIdx + 1);
                   ctx.builder.gotoo(ifeqNode.label);
                });
        matcher.addRule(rule2);
    }

    @Override
    public void run(@NotNull MethodNode methodNode, @NotNull IPassContext passContext) {
        if(methodNode.instructions == null || methodNode.instructions.size() == 0){
            return;
        }
//        methodNode.instructions = matcher.apply(methodNode.instructions);
        int startSize, endSize;
        do{
            startSize = methodNode.instructions.size();
            methodNode.instructions = matcher.apply(methodNode.instructions);
            endSize = methodNode.instructions.size();
        }while (startSize != endSize);
    }
}
